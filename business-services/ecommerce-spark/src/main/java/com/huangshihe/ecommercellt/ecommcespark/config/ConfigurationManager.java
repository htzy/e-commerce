package com.huangshihe.ecommercellt.ecommcespark.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置管理类，这里不像框架，这里的配置文件是规定的。
 * TODO 之后将增加配置文件，将配置归类
 * <p>
 * Create Date: 2017-12-01 23:02
 *
 * @author huangshihe
 */
public class ConfigurationManager {

    private static Logger LOGGER = LoggerFactory.getLogger(ConfigurationManager.class);

    private static ConcurrentHashMap<String, Configuration> map = new ConcurrentHashMap<String, Configuration>();

    private ConfigurationManager() {
    }

    /**
     * 通过文件名加载配置文件到map
     * @param fileName 配置文件名
     * @return 配置信息类
     */
    public static Configuration load(String fileName) {
        Configuration result = map.get(fileName);
        if (result == null) {
            synchronized (ConfigurationManager.class) {
                result = map.computeIfAbsent(fileName, k -> new Configuration(fileName));
            }
        }
        return result;
    }

    // 一启动时，即加载所有的配置文件。
    // 这里不使用单例模式的原因是：使用单例模式还需要被动调用执行，而加载配置是必须的，因此不如直接让它自己主动执行。
    static {
        LOGGER.info("load basic.properties begin...");
        load("basic.properties");
        LOGGER.info("load basic.properties end...");
    }

    public static Configuration getConfiguration(String fileName) {
        return map.get(fileName);
    }

    public static Configuration getBasicConfiguration() {
        return getConfiguration("basic.properties");
    }

}
