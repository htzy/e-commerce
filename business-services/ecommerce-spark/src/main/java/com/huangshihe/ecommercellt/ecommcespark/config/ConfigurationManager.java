package com.huangshihe.ecommercellt.ecommcespark.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 配置管理类
 *
 * @author huangshihe
 * Create Date: 2017-12-01 23:02
 */
public class ConfigurationManager {
    /**
     *
     */
    private static Properties properties;

    private static InputStream inputStream;

    private static Logger LOGGER = LoggerFactory.getLogger(ConfigurationManager.class);

    static {
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("basic.properties");
            if(inputStream == null) {
                LOGGER.error("Properties file not found in classpath: basic.properties");
                throw new IllegalArgumentException("Properties file not found in classpath: basic.properties");
            }
            properties = new Properties();
            properties.load(new InputStreamReader(inputStream, "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file.", e);
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    LOGGER.error("Error close inputStream, {}", e1);
                }
            }

        }

    }

    /**
     * 通过key获取属性值
     * @param key key
     * @return 属性值
     */
    public static String getProperties(String key) {
        return properties.getProperty(key);
    }

}
