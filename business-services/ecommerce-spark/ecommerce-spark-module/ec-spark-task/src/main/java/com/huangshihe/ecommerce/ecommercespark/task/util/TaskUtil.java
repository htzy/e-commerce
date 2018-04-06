package com.huangshihe.ecommerce.ecommercespark.task.util;

import com.huangshihe.ecommerce.ecommercespark.commonconfig.entity.ECConfiguration;
import com.huangshihe.ecommerce.ecommercespark.commonconfig.manager.ECConfigurationManager;
import com.huangshihe.ecommerce.ecommercespark.task.constants.SparkConstants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;

/**
 * 任务工具类.
 * <p>
 * Create Date: 2018-01-01 15:14
 *
 * @author huangshihe
 */
public class TaskUtil {

    /**
     * 创建sc.
     *
     * @param appName appName
     * @return sc
     */
    public static JavaSparkContext createSCEnv(String appName) {
        ECConfiguration basicConf = ECConfigurationManager.getConfiguration(SparkConstants.BASIC_CONF_FILENAME);
        final String master = basicConf.getProperty(SparkConstants.CONF_SPARK_MASTER);
        SparkConf conf = new SparkConf().setAppName(appName).setMaster(master);
        return new JavaSparkContext(conf);
    }


    /**
     * 获取sqlContext.
     *
     * @param sparkContext sparkContext
     * @return sqlContext
     */
    public static SQLContext getSqlContext(SparkContext sparkContext) {
        ECConfiguration basicConf = ECConfigurationManager.getConfiguration(SparkConstants.BASIC_CONF_FILENAME);
        if (basicConf.getBoolean(SparkConstants.CONF_SPARK_LOCAL)) {
            // 这里是单机模式，所以使用最简单的SQLContext
            return new SQLContext(sparkContext);
        } else {
            // TODO 如果是生产环境，数据来源为HBase，那么需要对应的上下文。
            return null;
        }
    }

}
