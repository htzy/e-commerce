package com.huangshihe.ecommerce.ecommercehbase.dao;

import com.huangshihe.ecommerce.ecommercehbase.manager.HBaseConnectionManager;
import com.huangshihe.ecommerce.ecommercehbase.util.HBaseDaoUtil;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DAO实现类.
 * TODO 是否需要针对每个数据表做一个DAO？
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
    private final Connection connection; //NOPMD

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
                    final HColumnDescriptor column = new HColumnDescriptor(familyName); //NOPMD
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
     * 通过rowKey查询.
     *
     * @param tableNameStr 表名
     * @param rowKey       rowKey
     */
    @Override
    public List<Cell> queryTableByRowKey(final String tableNameStr, final String rowKey) {
        if (!isExists(tableNameStr)) {
            LOGGER.error("[queryTableByRowKey] table: {} is not exists!", tableNameStr);
            return null;
        }

        Result result = null;
        try (Table table = connection.getTable(TableName.valueOf(tableNameStr))) {
            // 这里的table名需要注意是否为default命名空间，即：default.tableName
            final Get get = new Get(Bytes.toBytes(rowKey));
            result = table.get(get); //NOPMD
            LOGGER.debug("[queryTableByRowKey] result: {}", result);
        } catch (IOException e) {
            LOGGER.error("query table by rowKey failed! table: {}, rowKey: {}, network exception occurs? detail: {}",
                    tableNameStr, rowKey, e);
        }
        return HBaseDaoUtil.getCells(result);
    }

    /**
     * 通过rowKey查询，并通过columns过滤.
     *
     * @param tableNameStr 表名
     * @param rowKey       rowKey
     * @param cf           过滤的列名(key为family，value为column)
     * @return cellList
     */
    @Override
    public List<Cell> queryTableByRowKey(final String tableNameStr, final String rowKey,
                                         final Map<String, List<String>> cf) {
        if (!isExists(tableNameStr)) {
            LOGGER.error("[queryTableByRowKey] table: {} is not exists!", tableNameStr);
            return null;
        }

        Result result = null;
        try (Table table = connection.getTable(TableName.valueOf(tableNameStr))) {
            // 这里的table名需要注意是否为default命名空间，即：default.tableName
            final Get get = new Get(Bytes.toBytes(rowKey));
            for (final String family : cf.keySet()) {
                for (final String column : cf.get(family)) {
                    get.addColumn(Bytes.toBytes(family), Bytes.toBytes(column));
                }
            }
            result = table.get(get); //NOPMD
            LOGGER.debug("[queryTableByRowKey] result: {}", result);
        } catch (IOException e) {
            LOGGER.error("query table by rowKey failed! table: {}, rowKey: {}, cf: {}, network exception occurs? detail: {}",
                    tableNameStr, rowKey, cf, e);
        }
        return HBaseDaoUtil.getCells(result);
    }

    /**
     * 查询表中的所有数据（全表扫描）.
     * TODO 继续细化
     *
     * @param tableNameStr 表名
     * @return 表中所有数据
     */
    @Override
    public List<Cell> queryAll(String tableNameStr) {
        if (!isExists(tableNameStr)) {
            LOGGER.error("[queryAll] table: {} is not exists!", tableNameStr);
            return null;
        }

        ResultScanner resultScanner = null;
        try (Table table = connection.getTable(TableName.valueOf(tableNameStr))) {

            Scan scan = new Scan();
//            scan.setStartRow()
//            scan.setStopRow()
//            // 不轻易使用filter，因为速度很慢，如果要用，建议使用前缀filter：PrefixFilter，还有协助分页的filter：PageFilter
//            scan.setFilter()

//            // 协助缓存的
//            scan.setCacheBlocks()
//            scan.setCaching()
            resultScanner = table.getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(resultScanner);
        }
        return null;
    }

    /**
     * 插入值.
     * TODO 增加是否使用缓冲区选项，默认不用
     * TODO value可否设置为Object，如何更灵活的插入数据？
     *
     * @param tableNameStr 表名
     * @param rowKey       rowKey
     * @param family       列族
     * @param columnValues 列及值
     */
    @Override
    public void insert(String tableNameStr, String rowKey, String family, Map<String, List<String>> columnValues) {
        if (!isExists(tableNameStr)) {
            LOGGER.error("[insert] table: {} is not exists!", tableNameStr);
            return;
        }

        try (Table table = connection.getTable(TableName.valueOf(tableNameStr))) {
            LOGGER.debug("[insert]init List Put size: {}", columnValues.size() * columnValues.values().size());
            List<Put> puts = new ArrayList<>(columnValues.size() * columnValues.values().size());
            for (String column : columnValues.keySet()) {
                for (String value : columnValues.get(column)) {
                    Put put = new Put(Bytes.toBytes(rowKey));
                    put.addColumn(Bytes.toBytes(family), Bytes.toBytes(column), Bytes.toBytes(value));
                    puts.add(put);
                }
            }
            LOGGER.debug("[insert] insert 'puts' size is {}", puts.size());
            table.put(puts);
        } catch (IOException e) {
            LOGGER.error("query table by rowKey failed! table: {}, rowKey: {}, network exception occurs? detail: {}",
                    tableNameStr, rowKey, e);

        }
    }


    /**
     * 删除表.
     *
     * @param tableNameStr 表名
     */
    @Override
    public void deleteTable(final String tableNameStr) {
        // TODO 删除前是否需要检查表是否已经存在？
        try (Admin admin = connection.getAdmin()) {
            final TableName tableName = TableName.valueOf(tableNameStr);
            // 禁用该表
            admin.disableTable(tableName);
            // 删除该表
            admin.deleteTable(tableName);
        } catch (IOException e) {
            LOGGER.error("delete table failed! table: {}, network exception occurs? detail: {}", tableNameStr, e);
        }
    }

    /**
     * 检查表是否存在.
     *
     * @param tableNameStr 表名
     * @return 是否存在
     */
    @Override
    public boolean isExists(final String tableNameStr) {
        boolean result = false; //NOPMD
        try (Admin admin = connection.getAdmin()) {
            result = admin.tableExists(TableName.valueOf(tableNameStr));
            // admin继承了AutoCloseable，在try-with-resources中不需要手动关闭。
        } catch (IOException e) {
            LOGGER.error("check table isExists failed! table: {}, network exception occurs? detail: {}", tableNameStr, e);
        }
        return result;
    }

}
