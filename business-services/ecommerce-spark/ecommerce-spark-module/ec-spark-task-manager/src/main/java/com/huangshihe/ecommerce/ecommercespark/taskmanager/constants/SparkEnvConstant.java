package com.huangshihe.ecommerce.ecommercespark.taskmanager.constants;

/**
 * spark环境常量.
 * <p>
 * Create Date: 2018-05-06 16:47
 *
 * @author huangshihe
 */
public class SparkEnvConstant {

    /**
     * basic配置文件名
     */
    public static final String BASIC_CONF_FILENAME = "basic.properties";

    /**
     * master地址值.
     */
    public static final String CONF_SPARK_MASTER = "spark.master";

    /**
     * mac中环境变量设置后若丢失，则可从配置文件中获取hadoop home
     */
    public static final String CONF_HADOOP_HOME = "hadoop.home.dir";

}
