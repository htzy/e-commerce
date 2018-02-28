package com.huangshihe.ecommerce.pub.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

/**
 * 配置类.
 * TODO 读取配置文件，读取xml类型的，同时在common中新建xmlkit工具类，
 * TODO 问题：因为配置文件太多类了，如何抽象？或者是否需要抽象？如线程池的有配置文件，spark的有配置文件，用一个共同的格式显示也不合适
 * TODO 暂时使用抽象一层，其他配置文件继承，抽象的或者使用接口！
 * 1. 如果使用接口，那么对于线程池而言，线程池的配置文件在线程池中，部署时统一归置到某文件夹中，那么继承配置文件的专门处理线程池的java类呢？放哪？
 * 配置文件是否还有必要部署时统一部署到某文件夹中？（必要！部署时统一将配置文件读取到内存中，之后的业务只需要调用即可）
 * 那么问题是：如何将内存中的配置文件转为配置的实体对象？
 *
 * 2. 可以将配置中的配置项转为实体类，既然是配置，肯定会用到，那么既然会用到，那么肯定要有对应的实体类。
 *
 * TODO 当前要做的是：怎么把配置文件读到内存里，读到内存里之后，如何与配置的实体类关联？因为配置文件有很多，同类型的配置文件可能也有很多，需要打开全部的配置文件
 *
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
     * 配置.
     */
    private final Properties properties;


    /**
     * 根据放在resources目录下的配置文件生成配置信息类.
     *
     * @param fileName 配置文件名
     */
    public Config(final String fileName) {

        try (InputStream inputStream = getClassLoader().getResourceAsStream(fileName);
             Reader reader = new InputStreamReader(inputStream, "UTF-8")) {
            properties = new Properties();

            properties.load(reader);
        } catch (IOException e) {
            // 可能是文件格式或字符编码不对
            LOGGER.error("Properties file not found in classpath or loading properties file error, {}", e);
            throw new IllegalArgumentException("Properties file not found in classpath or loading properties file error", e);
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


    /**
     * 获取配置值.
     *
     * @param key 配置名
     * @return 配置值
     */
    public String getProperty(final String key) {
        return properties.getProperty(key);
    }

    /**
     * 获取boolean配置值.
     *
     * @param key 配置名
     * @return 配置值
     */
    public boolean getBoolean(final String key) {
        // 内部实现：只要key不是"true"，返回值就是false
        return Boolean.parseBoolean(properties.getProperty(key));
    }
}
