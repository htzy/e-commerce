package com.huangshihe.ecommerce.ecommercespark.task.tasks;

import com.huangshihe.ecommerce.common.kits.DigitKit;
import com.huangshihe.ecommerce.common.kits.StringKit;
import com.huangshihe.ecommerce.ecommercehbase.hbasedao.filter.PrefixFuzzyAndTimeFilter;
import com.huangshihe.ecommerce.ecommercehbase.hbasedao.manager.HBaseConnectionManager;
import com.huangshihe.ecommerce.ecommercehbase.hbasedao.util.HBaseDaoUtil;
import com.huangshihe.ecommerce.ecommercespark.task.util.TaskUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FuzzyRowFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Pair;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.tools.cmd.gen.AnyVals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

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
     * 配置.
     */
    private Configuration configuration = null;

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
        */
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

        return scan;
    }

    private Scan test() {
        // \x00\x00\x01b\xEF\xF2\x1E\xC8   => 1524444045000
        // 2018-04-23 08:40:45
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 3, 23, 8, 40, 45);

        String rowPre = StringKit.fillChar(6 + 6 + 6, '?');

        byte[] rowPreByte = Bytes.toBytes(rowPre);
        String rowSuf = StringKit.fillChar(6, '?');
        byte[] rowSufByte = Bytes.toBytes(rowSuf);
        byte[] row = Bytes.add(rowPreByte, Bytes.toBytes(calendar.getTimeInMillis()), rowSufByte);
        System.out.println("1111:" + row.length);
        String infoHexStr = "010101010101" + "010101010101" + "010101010101" +"0000000000000000" + "010101010101";
        byte[] info = Bytes.fromHex(infoHexStr) ;

        Scan scan = new Scan();
        scan.setFilter(new FuzzyRowFilter(Collections.singletonList(Pair.newPair(row, info))));
        return scan;
    }

    public Scan test2() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 3, 22, 0, 0, 0);
        long start = calendar.getTimeInMillis();
//        calendar.add(Calendar.SECOND, 5);
        calendar.set(2018, 3, 23, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0); // 1524412800000
        try {
            Thread.sleep(100); // 加了sleep之后：end：1524412800347
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = calendar.getTimeInMillis();
        System.out.println("end:"+end); //1524412800031
        PrefixFuzzyAndTimeFilter filter = new PrefixFuzzyAndTimeFilter(18, start, end);

        Scan scan = new Scan();
        scan.setFilter(filter);     // scan可以查出来2条，属于
//\x01b\xEE\x15\x5C\x00
        // \x01b\xEE\x15\x5C\x00 => 1524412800000  => 2018-04-23 00:00:00
        // 因为查询时：包头不包尾，标注结果为23号凌晨，为什么会把凌晨的结果带出来？
        // 因为后面还有值，所以这里会把00:00:00的结果带出来
        // 这样理解不对！因为在过滤器里，是直接判断的时间啊！
        // WOCAO! 查询条件指定的end结果是：1524412800031，而数据库中的1524412800000，当然可以查出来了！
        // WOCAO! 为啥这里会多31ms啊！
        // 当sleep之后，ms还会增加，在指定ms为0之后，稳定！
        return scan;
    }

    /**
     * 构造查询配置.
     *
     * @throws IOException 网络或文件异常
     */
    @Override
    public void buildQueryConfiguration() throws IOException {
//        final Scan scan = getQueryScanCondition();
        final Scan scan = test2();
        configuration = HBaseConnectionManager.getInstance().getNewConfiguration();
        // 输入表:原始表
        configuration.set(TableInputFormat.INPUT_TABLE, "t_performance_original_2018-04-23");
        configuration.set(TableInputFormat.SCAN, HBaseDaoUtil.convertScanToString(scan));

    }

    /**
     * 汇聚.
     *
     * @param hBaseRDD rdd
     */
    @Override
    public void aggr(JavaPairRDD<ImmutableBytesWritable, Result> hBaseRDD) {
        if (hBaseRDD == null) {
            LOGGER.error("hBaseRDD is null");
        } else {
            System.out.println("hBaseRDD count:" + hBaseRDD.count());

            System.out.println(hBaseRDD.take(10));

        }
    }

    /**
     * 任务主体.
     */
    @Override
    public void spark() {
        System.setProperty("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        try (JavaSparkContext sc = TaskUtil.createSCEnv("minute")) {
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
