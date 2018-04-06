package com.huangshihe.ecommerce.ecommercespark.task.tasks;

import com.huangshihe.ecommerce.ecommercehbase.hbasedao.manager.HBaseConnectionManager;
import com.huangshihe.ecommerce.ecommercehbase.hbasedao.util.HBaseDaoUtil;
import com.huangshihe.ecommerce.ecommercespark.task.constants.SparkConstants;
import com.huangshihe.ecommerce.ecommercespark.task.util.TaskUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapred.TableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 性能任务，性能任务中会涉及到多个表如：info/hour/day/...
 * 所有的task将统一管理，多个同一类型的task任务不会在同一时间执行，
 * 因为基本所有的汇聚任务task运行前提条件都是稳定的，隔段时间才会执行，如天表：一天汇聚一次。
 * 所以所有任务的配置都需新建，每个任务的配置都不一定一样。
 * 这里为性能天表的汇聚任务。
 * <p>
 * Create Date: 2018-01-01 15:12
 *
 * @author huangshihe
 */
public class PerformanceDayTask implements ISparkTask {
    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceDayTask.class);

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
        scan.setCaching(SparkConstants.SCAN_CACHE_SIZE);
        scan.setBatch(SparkConstants.SCAN_DAY_BATCH_SIZE);

        // TODO 拼rowkey（start/stop）
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
        // 输入数据的表，这里即：小时表=>天表
        configuration.set(TableInputFormat.INPUT_TABLE, SparkConstants.PERFORMANCE_HOUR_TABLE);
        configuration.set(TableInputFormat.SCAN, HBaseDaoUtil.convertScanToString(scan));

    }

    /**
     * 汇聚.
     *
     * @param hBaseRDD rdd
     */
    @Override
    public void aggr(final JavaPairRDD<ImmutableBytesWritable, Result> hBaseRDD) {
        if (hBaseRDD == null) {
            LOGGER.error("[PerformanceDayTask-aggr] hBaseRDD is null");
        } else {
            // TODO 汇聚业务
            JavaPairRDD<ImmutableBytesWritable, Put> total = null;
            configuration.set(TableOutputFormat.OUTPUT_TABLE, SparkConstants.PERFORMANCE_DAY_TABLE);
            // TODO class??
//            configuration.set("mapreduce.outputformat.class", TableOutputFormat.class, OutPutFormat.class);
            total.saveAsNewAPIHadoopDataset(configuration);
        }
    }

    /**
     * 任务主体.
     */
    @Override
    public void spark() {
        try (JavaSparkContext sc = TaskUtil.createSCEnv(SparkConstants.PERFORMANCETASK_APP_NAME)) {
            buildQueryConfiguration();
            final JavaPairRDD<ImmutableBytesWritable, Result> hBaseRDD = sc.newAPIHadoopRDD(
                    configuration, TableInputFormat.class, ImmutableBytesWritable.class, Result.class);
            aggr(hBaseRDD);
        } catch (IOException e) {
            LOGGER.error("[PerformanceDayTask] error, detail: {}", e);
        }
    }

    public static void main(String[] args) {
        LOGGER.info("[PerformanceDayTask] start...");
        new PerformanceDayTask().spark();
        LOGGER.info("[PerformanceDayTask] end...");
    }


}
