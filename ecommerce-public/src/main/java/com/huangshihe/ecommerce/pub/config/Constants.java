package com.huangshihe.ecommerce.pub.config;

import java.io.File;

/**
 * 常量类.
 * <p>
 * Create Date: 2018-02-26 21:31
 *
 * @author huangshihe
 */
public class Constants {
    /**
     * 基本配置.
     */
    @Deprecated
    public static final String BASIC_CONFIG = "basic.properties";

    /**
     * 示例spark的配置文件名.
     */
    @Deprecated
    public static final String DEMO_CONF_FILENAME = "demotask.properties";

    /**
     * 配置文件存放目录：ecommerce-public的resource目录下，即：resource/data/pub/
     */
    public static final String CONFIG_FILE_DIR = "data" + File.separator + "pub" + File.separator;

    /**
     * 配置文件名格式：***-cfg.xml
     */
    public static final String CONFIG_FILE_PATTERN = "(.*)-cfg\\.xml";

    /**
     * 线程池配置文件存放目录.
     */
    public static final String THREAD_POOL_CONFIG_DIR = CONFIG_FILE_DIR + "threadPool" + File.separator;

    /**
     * 私有构造方法.
     */
    private Constants() {

    }
}
