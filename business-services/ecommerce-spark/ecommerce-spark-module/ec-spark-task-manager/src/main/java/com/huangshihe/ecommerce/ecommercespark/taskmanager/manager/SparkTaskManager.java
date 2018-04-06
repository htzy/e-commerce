package com.huangshihe.ecommerce.ecommercespark.taskmanager.manager;

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
}
