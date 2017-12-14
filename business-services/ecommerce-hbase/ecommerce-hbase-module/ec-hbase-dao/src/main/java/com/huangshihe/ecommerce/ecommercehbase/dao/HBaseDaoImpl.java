package com.huangshihe.ecommerce.ecommercehbase.dao;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * DAO实现类.
 * <p>
 * Create Date: 2017-12-14 00:18
 *
 * @author huangshihe
 */
public class HBaseDaoImpl implements IHBaseDao {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseDaoImpl.class);

    /**
     * HBase数据的连接对象.
     */
    private Connection connection;

    /**
     * 数据库元数据操作对象.
     */
    private Admin admin;

    /**
     * 构造方法.
     * // TODO 临时方案：在构造方法内准备环境.
     */
    public HBaseDaoImpl() {
        // 创建一个数据库配置对象
        final Configuration configuration = HBaseConfiguration.create();
        // TODO 以下配置放入配置文件中
        // 配置HBase数据库主机IP，即zookeeper主机地址，默认值为127.0.0.1
        configuration.set(HConstants.ZOOKEEPER_QUORUM, HConstants.LOCALHOST_IP);
        // HBase客户端连接端口，即zookeeper端口，默认值为2181
        configuration.set(HConstants.ZOOKEEPER_CLIENT_PORT, String.valueOf(HConstants.DEFAULT_ZOOKEPER_CLIENT_PORT));
        try {
            // 获取数据库连接对象
            connection = ConnectionFactory.createConnection();
            // 获取数据库元数据操作对象
            admin = connection.getAdmin();
        } catch (IOException e) {
            LOGGER.error("create hbase connection failed! {}", e);
            close();
        }

    }


    /**
     * 创建表.
     *
     * @param tableNameStr 表名
     * @param familyNames  列族名
     * @param ttl          老化时间
     */
    @Override
    public boolean createTable(final String tableNameStr, final String[] familyNames, final int ttl) {
        boolean result = false;
        // 检查表是否存在
        final TableName tableName = TableName.valueOf(tableNameStr);
        try {
            if (admin.tableExists(tableName)) {
                LOGGER.error("create table failed！table: '{}' is exists!", tableNameStr);
            } else {
                // 数据表描述对象
                final HTableDescriptor tableDesc = new HTableDescriptor(tableName);
                // 列族描述对象
                for (final String familyName : familyNames) {
                    final HColumnDescriptor column = new HColumnDescriptor(familyName);
                    column.setTimeToLive(ttl);
                    tableDesc.addFamily(column);
                }
                admin.createTable(tableDesc);
                result = true;
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error("create table '{}' failed! this table name is reserved, detail: {}", tableNameStr, e);
        } catch (MasterNotRunningException e) {
            LOGGER.error("create table '{}' failed! hbase master is not running, detail: {}", tableNameStr, e);
        } catch (TableExistsException e) {
            LOGGER.error("create table '{}' failed! the table is exists, detail: {}", tableNameStr, e);
        } catch (IOException e) {
            LOGGER.error("create table failed! table: {}, network exception occurs? detail: {}", tableNameStr, e);
        } finally {
            close();
        }
        return result;
    }

    /**
     * 关闭连接.
     */
    public void close() {
        try {
            if (admin != null) {
                admin.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (IOException e) {
            LOGGER.error("close admin table and connection failed!, network exception occurs? detail: {}", e);
        }
    }
}
