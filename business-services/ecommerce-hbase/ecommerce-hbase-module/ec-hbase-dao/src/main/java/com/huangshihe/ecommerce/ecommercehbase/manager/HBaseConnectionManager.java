package com.huangshihe.ecommerce.ecommercehbase.manager;

import com.huangshihe.ecommerce.ecommercehbase.dao.HBaseDaoImpl;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
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
    private static Configuration conf = null;

    /**
     * 静态连接对象.
     */
    private static Connection connection = null;

    /**
     * 获取配置对象.
     *
     * @return 配置对象
     */
    public static Configuration getConfiguration() {
        if (conf == null) {
            synchronized (HBaseConnectionManager.class) {
                if (conf == null) {
                    // 创建一个数据库配置对象
                    conf = HBaseConfiguration.create();
                    // TODO 以下配置放入配置文件中，如果取不到则使用默认值
                    // 配置HBase数据库主机IP，即zookeeper主机地址，默认值为127.0.0.1
                    conf.set(HConstants.ZOOKEEPER_QUORUM, HConstants.LOCALHOST_IP);
                    // HBase客户端连接端口，即zookeeper端口，默认值为2181
                    conf.set(HConstants.ZOOKEEPER_CLIENT_PORT, String.valueOf(HConstants.DEFAULT_ZOOKEPER_CLIENT_PORT));
                }
            }
        }
        return conf;
    }

    /**
     * 获取连接对象.
     *
     * @return 连接对象
     */
    public static Connection getConnection() {
        if (connection == null) {
            synchronized (HBaseConnectionManager.class) {
                if (connection == null) {
                    // 获取数据库连接对象
                    try {
                        connection = ConnectionFactory.createConnection(getConfiguration());
                    } catch (IOException e) {
                        LOGGER.error("create hbase connection failed! {}", e);
                    }
                }
            }
        }
        return connection;
    }

    /**
     * 私有构造方法.
     */
    private HBaseConnectionManager() {

    }
}
