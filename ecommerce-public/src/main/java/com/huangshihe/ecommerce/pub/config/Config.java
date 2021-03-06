package com.huangshihe.ecommerce.pub.config;

import com.huangshihe.ecommerce.common.kits.FileKit;
import com.huangshihe.ecommerce.common.kits.XmlKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * 配置类.
 * 1. 对于线程池而言，线程池的配置类在config/threadpool下，部署时统一将配置文件归置到pub的resources下，Main中统一读取配置文件到内存。
 * 2. 可以将配置中的配置项转为实体类，既然是配置，肯定会用到，那么既然会用到，那么肯定要有对应的实体类。
 * <p>
 * 参考：http://blog.csdn.net/melody_wkx/article/details/73205316
 * http://blog.csdn.net/qq_23039605/article/details/71080190
 * <p>
 * Create Date: 2018-02-26 21:16
 *
 * @author huangshihe
 */
public class Config {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    /**
     * 配置对象.
     */
    private final ConfigEntity configEntity;


    /**
     * 根据放在resources目录下的配置文件生成配置信息类.
     *
     * @param fileName 配置文件名
     */
    public Config(final String fileName, ConfigType type) {
        InputStream inputStream = null;
        switch (type) {
            case RESOURCETYPE:
                inputStream = getClassLoader().getResourceAsStream(fileName);
                break;
            case FILETYPE:
                inputStream = FileKit.getStream(fileName);
                break;
        }
        if (inputStream != null) {
            try (Reader reader = new InputStreamReader(inputStream, "UTF-8")) {
                configEntity = XmlKit.toEntity(ConfigEntity.class, reader);
            } catch (IOException e) {
                // 可能是文件格式或字符编码不对
                LOGGER.error("config file to InputStreamRead failed! detail:{}", e);
                throw new IllegalArgumentException("config file to InputStreamRead failed!", e);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // 可能是文件格式或字符编码不对
                    LOGGER.error("close inputStream error fileName:{} detail:{}", fileName, e);
//                    throw new IllegalArgumentException("close inputStream error fileName!", e);
                }
            }
        } else {
            LOGGER.error("config fileName:{} to inputStream is null, fileName:{}", fileName);
            throw new IllegalArgumentException("config fileName:{} to inputStream is null!");
        }

    }


    /**
     * 获取class loader.
     *
     * @return 当前线程的class loader
     */
    private ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader(); //NOPMD
    }

    public ConfigEntity getConfigEntity() {
        return configEntity;
    }

    /**
     * 配置文件枚举，分为resource类型和file文件.
     */
    public enum ConfigType {
        RESOURCETYPE, FILETYPE
    }
}
