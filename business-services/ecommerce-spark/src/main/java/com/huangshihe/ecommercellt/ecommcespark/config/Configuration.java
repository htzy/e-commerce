package com.huangshihe.ecommercellt.ecommcespark.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 配置信息类
 * <p>
 * Create Date: 2017-12-02 23:24
 *
 * @author huangshihe
 */
public class Configuration {

    private static Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    private Properties properties = null;

    public Configuration(String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                LOGGER.error("Properties file not found in classpath: {}", fileName);
                throw new IllegalArgumentException("Properties file not found in classpath: " + fileName);
            }
            properties = new Properties();
            properties.load(new InputStreamReader(inputStream, "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file.", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("Error close inputStream, {}", e);
                }
            }
        }
    }

    private ClassLoader getClassLoader() {
        ClassLoader ret = Thread.currentThread().getContextClassLoader();
        return ret != null ? ret : getClass().getClassLoader();
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
