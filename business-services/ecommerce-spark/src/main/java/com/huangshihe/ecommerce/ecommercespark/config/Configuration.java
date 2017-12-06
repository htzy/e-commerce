package com.huangshihe.ecommerce.ecommercespark.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置信息类.
 * <p>
 * Create Date: 2017-12-02 23:24
 *
 * @author huangshihe
 */
public class Configuration {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    /**
     * 配置.
     */
    private final Properties properties; //NOPMD

    /**
     * 根据放在resources目录下的配置文件生成配置信息类.
     *
     * @param fileName 配置文件名
     */
    public Configuration(final String fileName) {
        InputStream inputStream = null;
        Reader reader = null;
        try {
            inputStream = getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                LOGGER.error("Properties file not found in classpath: {}", fileName);
                throw new IllegalArgumentException("Properties file not found in classpath: " + fileName);
            }
            properties = new Properties();
            reader = new InputStreamReader(inputStream, "UTF-8");
            properties.load(reader);
        } catch (IOException e) {
            // 可能是文件格式或字符编码不对
            LOGGER.error("Error loading properties file, {}", e);
            throw new IllegalArgumentException("Error loading properties file.", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOGGER.error("Error close reader, {}", e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("Error close inputStream, {}", e);
                }
            }
        }
    }

    /**
     * 获取class loader.
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
}
