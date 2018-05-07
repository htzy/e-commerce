package com.huangshihe.ecommerce.pub.deploy;

import com.huangshihe.ecommerce.common.kits.FileKit;
import com.huangshihe.ecommerce.pub.constants.ConfigConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 部署管理类.
 * <p>
 * Create Date: 2018-04-07 20:51
 *
 * @author huangshihe
 */
public final class DeployManager {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeployManager.class);

    private static final DeployManager _instance = new DeployManager();

    public static DeployManager getInstance() {
        return _instance;
    }

    /**
     * 初始化任务.
     */
    public void init() {
        // 部署配置文件
        deployConfigs();
    }

    /**
     *  TODO 目前采用：脚本拷贝jar包到root目录（即：/usr/local/opt/ecommerce/）下
     */
    public void deployJars() {

    }

    /**
     * 部署配置文件.
     */
    public void deployConfigs() {
        // 将configs的jar包解压到配置文件目录下
        List<File> configs = FileKit.getAllFiles(ConfigConstant.CONFIG_JAR_FILE_DIR, ConfigConstant.CONFIG_JAR_PATTERN);
        for (File config : configs) {
            try {
                FileKit.uncompressConfigJar(config, new File(ConfigConstant.CONFIG_FILE_DIR));
            } catch (IOException e) {
                LOGGER.error("uncompress config failed! config file:{}, detail:{}", config, e);
                throw new IllegalArgumentException("uncompress config failed!");
            }
        }
    }

}
