package com.huangshihe.ecommerce.ecommercespark.taskmanager.manager;

import com.huangshihe.ecommerce.common.configs.SimpleConfig;
import com.huangshihe.ecommerce.ecommercespark.taskmanager.constants.SparkEnvConstant;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务管理
 * <p>
 * Create Date: 2018-01-01 14:32
 *
 * @author huangshihe
 */
public class SparkTaskManager {
    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SparkTaskManager.class);

    /**
     * 实例.
     */
    private static SparkTaskManager INSTANCE = new SparkTaskManager();


    /**
     * 获取实例.
     *
     * @return 实例
     */
    public static SparkTaskManager getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化.
     */
    private void init() {

    }

    /**
     * 私有构造方法
     */
    private SparkTaskManager() {
    }

    /**
     * 根据appName获取sql上下文.
     *
     * @param appName name
     * @return sqlContext
     */
    public SQLContext getSqlContext(String appName) {
        SimpleConfig basicConf = new SimpleConfig(SparkEnvConstant.BASIC_CONF_FILENAME);
        final String master = basicConf.getProperty(SparkEnvConstant.CONF_SPARK_MASTER);
        SparkSession session = SparkSession.builder().appName(appName).master(master)
                // 指定spark序列化类
                .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .getOrCreate();

        return new SQLContext(session);
    }

    /**
     * 根据appName获取JavaSpark上下文.
     *
     * @param appName name
     * @return JavaSparkContext
     */
    public JavaSparkContext getJavaSparkContext(String appName) {
        return new JavaSparkContext(getSqlContext(appName).sparkContext());
    }

}
