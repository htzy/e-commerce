package com.huangshihe.ecommerce.ecommercehbase.dao;

import com.huangshihe.ecommerce.ecommercehbase.manager.HBaseConnectionManager;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
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
     * 构造方法.
     */
    public HBaseDaoImpl() {
        connection = HBaseConnectionManager.getConnection();
    }


    /**
     * 创建表.
     *
     * @param tableNameStr 表名
     * @param familyNames  列族名
     * @param ttl          老化时间
     */
    @Override
    public boolean createTable(final String tableNameStr, final String[] familyNames, final int ttl) { //NOPMD
        boolean result = false; //NOPMD
        // 数据库元数据操作对象
        Admin admin = null;
        try {
            admin = connection.getAdmin();
            // 检查表是否存在
            final TableName tableName = TableName.valueOf(tableNameStr);
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
            try {
                if (admin != null) {
                    admin.close();
                }
            } catch (IOException e) {
                LOGGER.error("close admin table and connection failed!, network exception occurs? detail: {}", e);
            }

        }
        return result;
    }
}
