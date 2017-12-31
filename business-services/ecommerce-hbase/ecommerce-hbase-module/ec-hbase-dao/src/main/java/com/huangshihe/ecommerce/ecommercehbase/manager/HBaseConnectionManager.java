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
     * 单例.
     */
    private static HBaseConnectionManager INSTANCE = new HBaseConnectionManager();

    /**
     * 配置对象.
     */
    private Configuration configuration = null;

    /**
     * 连接对象.
     */
    private Connection connection = null;

    /**
     * 用于同步的对象？
     */
    private final Object object = new Object();

    /**
     * 创建配置对象.
     *
     * @return 静态配置对象
     */
    private Configuration createConfiguration() {
        // 将配置文件拷贝到resources目录下，create会去找hbase-site.xml配置文件进行创建。
        // 同时还会自动加载在org.apache.hbase:hbase-common.jar下的hbase-default.xml配置文件
        return HBaseConfiguration.create();
    }

    /**
     * 获取实例
     *
     * @return 实例
     */
    public static HBaseConnectionManager getInstance() {
        return INSTANCE;
    }

    /**
     * 创建连接对象.
     *
     * @return 连接对象
     */
    private Connection createConnection(Configuration conf) throws IOException {
        if (conf == null) {
            LOGGER.error("[HBaseConnectionManager] createConnection failed, conf is null");
            return null;
        } else {
            LOGGER.info("[HBaseConnectionManager] createConnection");
            // 获取数据库连接对象
            return ConnectionFactory.createConnection(conf);
        }
    }

    /**
     * 获取连接对象.
     *
     * @return 连接对象
     */
    public Connection getConnection() {
        // 如果connection被关闭，重新初始化，TODO 那么如何处理同步问题？两步检查机制可能不满足要求
        if (connection == null || connection.isClosed() || connection.isAborted()) {
            LOGGER.info("[HBaseConnectionManager] connection is not init");
            init();
        }
        return connection;
    }

    /**
     * 获取当前的配置对象.
     *
     * @return 配置
     */
    public Configuration getCurrentConfiguration() {
        // 由于初始化在getConnection时才会进行，那么就不能保证configuration已经被初始化？
        // 这里不能新建配置，只能初始化init中才能新建，否则职能乱套
        if (configuration == null) {
            LOGGER.warn("[HBaseConnectionManager] getConfiguration is null!");
        }
        return configuration;
    }

    /**
     * 获取全新的配置对象
     *
     * @return 配置
     */
    public Configuration getNewConfiguration() {
        return createConfiguration();
    }


    /**
     * 初始化.
     */
    private void init() {
        // TODO 同步使用object对象，具体如何处理
        Object obj = this.object;
        synchronized (this.object) {
            if (connection != null && !connection.isClosed() && !connection.isAborted()) {
                LOGGER.info("[HBaseConnectionManager] connection has been inited.");
            } else {
                LOGGER.info("[HBaseConnectionManager] init connection.");
                ClassLoader old = Thread.currentThread().getContextClassLoader();
                try {
                    Thread.currentThread().setContextClassLoader(Configuration.class.getClassLoader());
                    if (configuration == null) {
                        configuration = createConfiguration();
                    }
                    connection = createConnection(configuration);
                } catch (IOException e) {
                    LOGGER.error("[HBaseConnectionManager] init failed! {}", e);
                } finally {
                    Thread.currentThread().setContextClassLoader(old);
                }
            }
        }
    }

    /**
     * 私有构造方法.
     */
    private HBaseConnectionManager() {

    }
}
