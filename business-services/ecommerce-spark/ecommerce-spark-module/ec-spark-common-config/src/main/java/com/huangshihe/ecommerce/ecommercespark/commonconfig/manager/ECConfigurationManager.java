package com.huangshihe.ecommerce.ecommercespark.commonconfig.manager;

import com.huangshihe.ecommerce.ecommercespark.commonconfig.constants.Constants;
import com.huangshihe.ecommerce.ecommercespark.commonconfig.entity.ECConfiguration;
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
public final class ECConfigurationManager {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ECConfigurationManager.class);

    /**
     * 存放所有的配置信息.
     */
    private static ConcurrentHashMap<String, ECConfiguration> map = new ConcurrentHashMap<String, ECConfiguration>();

    /**
     * 私有的构造方法.
     */
    private ECConfigurationManager() {
    }

    /**
     * 通过文件名加载配置文件到map.
     *
     * @param fileName 配置文件名
     * @return 配置信息类
     */
    public static ECConfiguration load(final String fileName) {
        ECConfiguration result = map.get(fileName);
        // 两步检查机制
        if (result == null) {
            // 同一时间，只能有一个线程获取到ConfigurationManager对象的锁
            // 这里需要注意的是"同步"若加在方法上，则造成load成功之后，再次执行load，
            // 则每次都需要进行无用的"同步"，因为配置已存在，不需要再次"同步"新建配置
            // TODO 这里同步可以优化为：锁住一个内部无用的对象，还是锁住整个类？
            synchronized (ECConfigurationManager.class) {
//                result = map.get(fileName);
//                if (result == null) {
//                    result = new ECConfiguration(fileName);
//                    map.put(fileName, result);
//                }
                // 这里仍需检查是否为空，如果为空，则新建
                result = map.computeIfAbsent(fileName, key -> new ECConfiguration(fileName));
            }
        }
        return result;
    }

    // 一启动时，即加载所有的配置文件。
    // 这里不使用单例模式的原因是：使用单例模式还需要被动调用执行，而加载配置是必须的，因此不如直接让它自己主动执行。
    // TODO 如果主动执行，如果配置文件出现问题？获取失败了，如何重新加载处理？如果配置文件过多，而只有启动前期使用过，但长期占用内存，却无法释放？
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
    public static ECConfiguration getConfiguration(final String fileName) {
        ECConfiguration conf = map.get(fileName);
        if (conf == null) {
            conf = load(fileName);
        }
        return conf;
    }

    /**
     * 获取基本配置.
     *
     * @return 基本配置
     */
    public static ECConfiguration getBasicConfiguration() {
        return getConfiguration(Constants.BASIC_CONFIG);
    }

}
