package com.huangshihe.ecommerce.ecommercehbase.manager;

import com.huangshihe.ecommerce.ecommercehbase.dao.HBaseDaoImpl;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * hbase数据库连接管理类.
 * <p>
 * Create Date: 2017-12-15 22:11
 *
 * @author huangshihe
 */
public final class HBaseConnectionManager {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseDaoImpl.class);

    /**
     * 静态配置对象.
     */
    private static Configuration configuration = createConfiguration();

    /**
     * 静态连接对象.
     */
    private static Connection connection = createConnection();

    /**
     * 创建配置对象.
     *
     * @return 静态配置对象
     */
    private static Configuration createConfiguration() {
        // 将配置文件拷贝到resources目录下，create会去找hbase-site.xml配置文件进行创建。
        // 同时还会自动加载在org.apache.hbase:hbase-common.jar下的hbase-default.xml配置文件
        return HBaseConfiguration.create();
    }

    /**
     * 获取配置对象.
     *
     * @return 配置对象
     */
    public static Configuration getConfiguration() {
        return configuration;
    }

    /**
     * 创建连接对象.
     *
     * @return 连接对象
     */
    private static Connection createConnection() {
        try {
            // 获取数据库连接对象
            return ConnectionFactory.createConnection(getConfiguration());
        } catch (IOException e) {
            LOGGER.error("create hbase connection failed! {}", e);
            throw new IllegalArgumentException("create hbase connection failed! {}", e);
        }
    }

    /**
     * 获取连接对象.
     *
     * @return 连接对象
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * 私有构造方法.
     */
    private HBaseConnectionManager() {

    }
}
