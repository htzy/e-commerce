package com.huangshihe.ecommercellt.ecommcespark.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

/**
 * 配置信息类
 * <p>
 * Create Date: 2017-12-02 23:24
 *
 * @author huangshihe
 */
public class Configuration {

    /**
     * 日志
     */
    private static Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    private Properties properties = null;

    public Configuration(String fileName) {
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
            throw new RuntimeException("Error loading properties file.", e);
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

    private ClassLoader getClassLoader() {
        ClassLoader ret = Thread.currentThread().getContextClassLoader();
        return ret != null ? ret : getClass().getClassLoader();
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
