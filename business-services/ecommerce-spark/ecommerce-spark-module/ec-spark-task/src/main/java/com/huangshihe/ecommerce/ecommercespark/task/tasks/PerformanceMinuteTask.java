package com.huangshihe.ecommerce.ecommercespark.task.tasks;

import com.huangshihe.ecommerce.common.kits.TimeKit;
import com.huangshihe.ecommerce.ecommercehbase.hbasedao.dao.HBaseDaoImpl;
import com.huangshihe.ecommerce.ecommercehbase.hbasedao.dao.IHBaseDao;
import com.huangshihe.ecommerce.ecommercehbase.hbasedao.filter.PrefixFuzzyAndTimeFilter;
import com.huangshihe.ecommerce.ecommercehbase.hbasedao.manager.HBaseConnectionManager;
import com.huangshihe.ecommerce.ecommercehbase.hbasedao.util.HBaseDaoUtil;
import com.huangshihe.ecommerce.ecommercespark.pipeline.MinutePipeline;
import com.huangshihe.ecommerce.ecommercespark.taskmanager.manager.SparkTaskManager;
import com.huangshihe.ecommerce.ecommercespark.taskmanager.tasks.ISparkTask;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.io.IOException;

/**
 * 分钟表汇聚任务.
 * <p>
 * Create Date: 2018-05-05 16:03
 *
 * @author huangshihe
 */
public class PerformanceMinuteTask implements ISparkTask {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceMinuteTask.class);

    /**
     * task管理.
     */
    private static final SparkTaskManager sparkTaskManager = SparkTaskManager.getInstance();

    /**
     * 配置.
     */
    private Configuration configuration = null;

    private IHBaseDao dao = new HBaseDaoImpl();

    /**
     * 获取查询条件.
     *
     * @return scan
     */
    @Override
    public Scan getQueryScanCondition() {
        final Scan scan = new Scan();
//        scan.setCaching(1000);
        // 当使用filter，则不能再使用setBatch
//        scan.setBatch(60);
//        scan.setCacheBlocks()

        // 这里暂时先手动指定查询时间 2018-04-23 00:00:00 ~ 15:00:00
        /*FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        // 正则表达式过滤器
        String regEx = "[0-9]{6}[0-9]{6}[0-9A-Za-z]{6}([0-9]{8})[0-9A-Za-z]{6}";
        RegexStringComparator rc = new RegexStringComparator(regEx);


        FuzzyRowFilter filter = new FuzzyRowFilter();

        String rowPrefixStr = "??????????????????";
        byte[] rowPrefixByte = Bytes.toBytes(rowPrefixStr);

        String rowSuffixStr = "??????";
        byte[] rowSuffixByte = Bytes.toBytes(rowSuffixStr);


        List<Pair<byte[], byte[]>> pairs = new ArrayList<>(20);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 4 - 1, 23, 0, 0, 0);
        byte[] info = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < 20; i++) {
            long mills = calendar.getTimeInMillis();
            byte[] row = Bytes.add(rowPrefixByte, Bytes.toBytes(mills), rowSuffixByte);
            pairs.add(new Pair<>(row, info));
            calendar.add(Calendar.SECOND, 5);
        }

        scan.setFilter(new FuzzyRowFilter(pairs));
        */
        // \x01b\xEE\x15\x5C\x00 => 1524412800000  => 2018-04-23 00:00:00
        long startTime = TimeKit.toTimeMillSec(2018, 4, 23, 0, 0, 0);
        long stopTime = TimeKit.toTimeMillSec(2018, 4, 23, 0, 5, 0);
        scan.setFilter(new PrefixFuzzyAndTimeFilter(6 + 6 + 6, startTime, stopTime));
        return scan;
    }

    /**
     * 构造查询配置.
     *
     * @throws IOException 网络或文件异常
     */
    @Override
    public void buildQueryConfiguration() throws IOException {
        final Scan scan = getQueryScanCondition();
        configuration = HBaseConnectionManager.getInstance().getNewConfiguration();
        // 输入表:原始表
        configuration.set(TableInputFormat.INPUT_TABLE, "t_performance_original_2018-04-23");
        configuration.set(TableInputFormat.SCAN, HBaseDaoUtil.convertScanToString(scan));
    }

    /**
     * 汇聚.
     * hBaseRDD的key是rowkey，而Result则是整个row（keyvalues）
     *
     * @param hBaseRDD rdd
     */
    @Override
    public void aggr(JavaPairRDD<ImmutableBytesWritable, Result> hBaseRDD) {
        if (hBaseRDD == null) {
            LOGGER.error("hBaseRDD is null");
        } else {
            // count涉及到合并操作，不能随便打印
//            LOGGER.debug("hBaseRDD count:{}", hBaseRDD.count());



            configuration.set(TableOutputFormat.OUTPUT_TABLE, "t_temp");
            try {
                Job job = Job.getInstance(configuration, "create HFile");
                job.setOutputKeyClass(ImmutableBytesWritable.class);
                job.setOutputValueClass(Result.class);
                job.setOutputFormatClass(TableOutputFormat.class);

                if (!dao.isExists("t_temp")) {
                    dao.createTable("t_temp", "t", 7 * 24 * 60 * 60);
                }
                //---------begin，// TODO 论文 下面的方法，会将rdd写成HFile文件，并将HFile文件到hdfs上
                // InvalidJobConfException: Output directory not set.
                //FileOutputFormat.setOutputPath(job, new Path(Constants.SIMULATION_HFILE_DIR + File.separator + "tmp"));
                //hBaseRDD.saveAsNewAPIHadoopDataset(job.getConfiguration());
                //---------end
//                hBaseRDD.saveAsHadoopDataset(job);

                String home = System.getenv("HADOOP_CLASSPATH");
                LOGGER.debug("home::{}", home);

//                JavaPairRDD<ImmutableBytesWritable, Put> rdd = hBaseRDD.mapToPair(new TempMap());
                JavaPairRDD<ImmutableBytesWritable, Put> rdd = MinutePipeline.pipeline(hBaseRDD);
                rdd.saveAsNewAPIHadoopDataset(job.getConfiguration());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 必须定义为static，否则将报：task not serializable
    static class TempMap implements PairFunction<Tuple2<ImmutableBytesWritable, Result>, ImmutableBytesWritable, Put> {
        @Override
        public Tuple2<ImmutableBytesWritable, Put> call(Tuple2<ImmutableBytesWritable, Result> tuple2) throws Exception {
            Put put = new Put(tuple2._1().copyBytes());
            put.addColumn(Bytes.toBytes("t"), Bytes.toBytes("1"), Bytes.toBytes("123"));
            return new Tuple2<>(tuple2._1(), put);
        }
    }


    /**
     * 任务主体.
     */
    @Override
    public void spark() {
        // 指定spark序列化类
//        System.setProperty("spark.serializer", "org.apache.spark.serializer.KryoSerializer");

        try (JavaSparkContext sc = sparkTaskManager.getJavaSparkContext("minute")) {
            buildQueryConfiguration();
            final JavaPairRDD<ImmutableBytesWritable, Result> hBaseRDD = sc.newAPIHadoopRDD(
                    configuration, TableInputFormat.class, ImmutableBytesWritable.class, Result.class);
            aggr(hBaseRDD);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new PerformanceMinuteTask().spark();
    }
}
