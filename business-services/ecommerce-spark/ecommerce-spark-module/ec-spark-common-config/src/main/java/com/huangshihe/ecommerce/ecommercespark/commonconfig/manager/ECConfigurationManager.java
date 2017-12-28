package com.huangshihe.ecommerce.ecommercespark.commonconfig.manager;

import com.huangshihe.ecommerce.ecommercespark.commonconfig.constants.Constants;
import com.huangshihe.ecommerce.ecommercespark.commonconfig.entity.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置管理类，这里不像框架，这里的配置文件是规定的.
 * TODO 之后将增加配置文件，将配置归类
 * <p>
 * Create Date: 2017-12-01 23:02
 *
 * @author huangshihe
 */
public final class ConfigurationManager {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationManager.class);

    /**
     * 存放所有的配置信息.
     */
    private static ConcurrentHashMap<String, Configuration> map = new ConcurrentHashMap<String, Configuration>();

    /**
     * 私有的构造方法.
     */
    private ConfigurationManager() {
    }

    /**
     * 通过文件名加载配置文件到map.
     *
     * @param fileName 配置文件名
     * @return 配置信息类
     */
    public static Configuration load(final String fileName) {
        Configuration result = map.get(fileName);
        // 两步检查机制
        if (result == null) {
            // 同一时间，只能有一个线程获取到ConfigurationManager对象的锁
            // 这里需要注意的是"同步"若加在方法上，则造成load成功之后，再次执行load，
            // 则每次都需要进行无用的"同步"，因为配置已存在，不需要再次"同步"新建配置
            synchronized (ConfigurationManager.class) {
//                result = map.get(fileName);
//                if (result == null) {
//                    result = new Configuration(fileName);
//                    map.put(fileName, result);
//                }
                // 这里仍需检查是否为空，如果为空，则新建
                result = map.computeIfAbsent(fileName, key -> new Configuration(fileName));
            }
        }
        return result;
    }

    // 一启动时，即加载所有的配置文件。
    // 这里不使用单例模式的原因是：使用单例模式还需要被动调用执行，而加载配置是必须的，因此不如直接让它自己主动执行。
    static {
        LOGGER.info("load basic.properties begin...");
        load(Constants.BASIC_CONFIG);
        LOGGER.info("load basic.properties end...");
    }

    /**
     * 根据文件名获取配置类.
     *
     * @param fileName 文件名
     * @return 配置信息
     */
    public static Configuration getConfiguration(final String fileName) {
        return map.get(fileName);
    }

    /**
     * 获取基本配置.
     *
     * @return 基本配置
     */
    public static Configuration getBasicConfiguration() {
        return getConfiguration(Constants.BASIC_CONFIG);
    }

}
