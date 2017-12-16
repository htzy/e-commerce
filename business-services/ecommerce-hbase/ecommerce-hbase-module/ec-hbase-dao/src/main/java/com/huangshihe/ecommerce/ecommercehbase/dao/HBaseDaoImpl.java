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
    private final Connection connection;

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
    public void createTable(final String tableNameStr, final String[] familyNames, final int ttl) { //NOPMD
        // 数据库元数据操作对象
        try (Admin admin = connection.getAdmin()) {
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
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error("create table '{}' failed! this table name is reserved, detail: {}", tableNameStr, e);
        } catch (MasterNotRunningException e) {
            LOGGER.error("create table '{}' failed! hbase master is not running, detail: {}", tableNameStr, e);
        } catch (TableExistsException e) {
            LOGGER.error("create table '{}' failed! the table is exists, detail: {}", tableNameStr, e);
        } catch (IOException e) {
            LOGGER.error("create table failed! table: {}, network exception occurs? detail: {}", tableNameStr, e);
        }
    }

    /**
     * 删除表.
     *
     * @param tableNameStr 表名
     */
    @Override
    public void deleteTable(final String tableNameStr) {

    }

    /**
     * 检查表是否存在.
     *
     * @param tableNameStr 表名
     * @return 是否存在
     */
    @Override
    public boolean isExists(final String tableNameStr) {
        boolean result = false;
        try (Admin admin = connection.getAdmin()) {
            result = admin.tableExists(TableName.valueOf(tableNameStr));
            // admin继承了AutoCloseable，在try-with-resources中不需要手动关闭。
        } catch (IOException e) {
            LOGGER.error("check table isExists failed! table: {}, network exception occurs? detail: {}", tableNameStr, e);
        }
        return result;
    }

}
