package com.huangshihe.ecommerce.pub.constants;

import com.huangshihe.ecommerce.common.constants.Constants;

import java.io.File;

/**
 * 配置文件的常量类.
 * <p>
 * Create Date: 2018-03-05 15:20
 *
 * @author huangshihe
 */
public class ConfigConstant {

    /**
     * 配置文件存放目录：/usr/local/opt/ecommerce/data/configs/
     */
    public static final String CONFIG_FILE_DIR = Constants.ROOT_DIR + "data" + File.separator + "configs" + File.separator;

    /**
     * 配置文件jar包存放目录：/usr/local/opt/ecommerce/configs/jar/
     */
    public static final String CONFIG_JAR_FILE_DIR = Constants.ROOT_DIR + "configs" + File.separator + "jar" + File.separator;

    /**
     * 线程池配置文件存放目录.
     */
    public static final String THREAD_POOL_CONFIG_DIR = CONFIG_FILE_DIR + "threadPool" + File.separator;

    /**
     * 配置文件名格式：***-cfg.xml
     */
    public static final String CONFIG_FILE_PATTERN = "(.*)-cfg\\.xml";

    /**
     * 配置类型的jar名字格式：
     */
    public static final String CONFIG_JAR_PATTERN = "ecommerce-(.*)-config-(.*)\\.jar";
}
