package com.huangshihe.ecommerce.ecommercespark.taskmanager.tasks;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.spark.api.java.JavaPairRDD;

import java.io.IOException;

/**
 * spark任务接口
 * <p>
 * Create Date: 2018-01-02 20:20
 *
 * @author huangshihe
 */
public interface ISparkTask {
    /**
     * 获取查询条件.
     *
     * @return scan
     */
    Scan getQueryScanCondition();

    /**
     * 构造查询配置.
     *
     * @throws IOException 网络或文件异常
     */
    void buildQueryConfiguration() throws IOException;

    /**
     * 汇聚.
     *
     * @param hBaseRDD rdd
     */
    void aggr(final JavaPairRDD<ImmutableBytesWritable, Result> hBaseRDD);

    /**
     * 任务主体.
     */
    void spark();
}
