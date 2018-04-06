package com.huangshihe.ecommerce.ecommercespark.task.constants;

/**
 * 常量类.
 * <p>
 * Create Date: 2017-12-27 23:32
 *
 * @author huangshihe
 */
public class SparkConstants {


    /**
     * 是否为local模式的配置值.
     */
    public static final String CONF_SPARK_LOCAL = "spark.local";

    /**
     * master地址值.
     */
    public static final String CONF_SPARK_MASTER = "spark.master";


    // 配置名

    /**
     * basic.
     */
    public static final String BASIC_CONF_FILENAME = "basic.properties";

    /**
     * 示例spark的配置文件名.
     */
    public static final String DEMO_CONF_FILENAME = "demotask.properties";

    /**
     * 性能的配置文件名.
     */
    public static final String PERFORMANCETASK_CONF_FILENAME = "performancetask.properties";


    // appName

    /**
     * 示例spark app名.
     */
    public static final String DEMO_APP_NAME = "DemoTask";

    /**
     * 性能的任务名.
     */
    public static final String PERFORMANCETASK_APP_NAME = "PerformanceDayTask";


    // 表名

    /**
     * 性能info表.
     */
    public static final String PERFORMANCE_INFO_TABLE = "performance_info";

    /**
     * 性能小时表.
     */
    public static final String PERFORMANCE_HOUR_TABLE = "performance_hour";

    /**
     * 性能天表.
     */
    public static final String PERFORMANCE_DAY_TABLE = "performance_day";


    // scan配置

    /**
     * 一次rpc查询的条数.
     */
    public static final int SCAN_CACHE_SIZE = 20000;

    /**
     * 查询天表，一次rpc查询的列数.
     */
    public static final int SCAN_DAY_BATCH_SIZE = 24;

}
