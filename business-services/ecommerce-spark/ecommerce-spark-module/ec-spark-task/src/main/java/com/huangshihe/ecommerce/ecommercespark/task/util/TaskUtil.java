package com.huangshihe.ecommerce.ecommercespark.task.util;

import com.huangshihe.ecommerce.common.configs.SimpleConfig;
import com.huangshihe.ecommerce.ecommercespark.task.constants.SparkConstants;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;

/**
 * 任务工具类.
 * // TODO 应该放入manager中
 * <p>
 * Create Date: 2018-01-01 15:14
 *
 * @author huangshihe
 */
@Deprecated
public class TaskUtil {

    /**
     * 创建sc.
     *
     * @param appName appName
     * @return sc
     */
    public static JavaSparkContext createSCEnv(String appName) {
        SimpleConfig basicConf = new SimpleConfig(SparkConstants.BASIC_CONF_FILENAME);
        final String master = basicConf.getProperty(SparkConstants.CONF_SPARK_MASTER);
        SparkConf conf = new SparkConf().setAppName(appName).setMaster(master);
        return new JavaSparkContext(conf);
    }




    /**
     * 获取sqlContext.
     * TODO 数据来源：统一使用HBase.
     *
     * @param config       配置
     * @param sparkContext sparkContext
     * @return sqlContext
     */
    @Deprecated
    public static SQLContext getSqlContext(SimpleConfig config, SparkContext sparkContext) {
        SimpleConfig basicConf = new SimpleConfig(SparkConstants.BASIC_CONF_FILENAME);
        if (basicConf.getBoolean(SparkConstants.CONF_SPARK_LOCAL)) {
            // 这里是单机模式，所以使用最简单的SQLContext
            return new SQLContext(sparkContext);
        } else {
            // TODO 如果是生产环境，数据来源为HBase，那么需要对应的上下文。
            return null;
        }
    }

    /**
     * 获取sqlContext.
     * 本地测试环境下使用。
     *
     * @param sparkContext sparkContext
     * @return sqlContext
     */
    public static SQLContext getLocalSqlContext(SparkContext sparkContext) {
        return new SQLContext(sparkContext);
    }


}
