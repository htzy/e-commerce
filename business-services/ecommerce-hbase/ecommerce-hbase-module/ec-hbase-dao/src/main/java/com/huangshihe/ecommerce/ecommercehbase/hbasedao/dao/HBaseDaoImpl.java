package com.huangshihe.ecommerce.ecommercehbase.hbasedao.dao;

import com.huangshihe.ecommerce.ecommercehbase.hbasedao.manager.HBaseConnectionManager;
import com.huangshihe.ecommerce.ecommercehbase.hbasedao.util.HBaseDaoUtil;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RegionLocator;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.exceptions.IllegalArgumentIOException;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PageFilter;
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
    private Connection connection; //NOPMD

    /**
     * 构造方法.
     */
    public HBaseDaoImpl() {
        connection = HBaseConnectionManager.getInstance().getConnection();
    }

    /**
     * 这里检查connection，如果异常，则从HBaseConnectionManager中获取connect
     *
     * @return connection
     */
    private Connection getConnection() {
        // 如果关闭了，那么重新从manager中获取
        if (connection == null || connection.isClosed() || connection.isAborted()) {
            connection = HBaseConnectionManager.getInstance().getConnection();
        }
        return connection;
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
        LOGGER.info("tableNameStr:{}, familyNames:{}, ttl:{}", tableNameStr, familyNames, ttl);
        // 数据库元数据操作对象
        try (Admin admin = getConnection().getAdmin()) {
            // 检查表是否存在
            final TableName tableName = TableName.valueOf(tableNameStr);
            if (admin.tableExists(tableName)) {
                LOGGER.error("create table failed！table: '{}' is exists!", tableNameStr);
            } else {
                // 数据表描述对象
                final HTableDescriptor tableDesc = new HTableDescriptor(tableName);
                // 列族描述对象
                for (final String familyName : familyNames) {
                    final HColumnDescriptor family = new HColumnDescriptor(familyName); //NOPMD
                    family.setTimeToLive(ttl);
                    tableDesc.addFamily(family);
                }
                admin.createTable(tableDesc);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error("create table '{}' failed! this table name is reserved, detail: {}", tableNameStr, e);
            throw new IllegalArgumentException(e);
        } catch (MasterNotRunningException e) {
            LOGGER.error("create table '{}' failed! hbase master is not running, detail: {}", tableNameStr, e);
            throw new IllegalArgumentException(e);
        } catch (TableExistsException e) {
            LOGGER.error("create table '{}' failed! the table is exists, detail: {}", tableNameStr, e);
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            LOGGER.error("create table failed! table: {}, network exception occurs? detail: {}", tableNameStr, e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 创建表.
     *
     * @param tableNameStr 表名
     * @param familyName   列族名
     * @param ttl          老化时间，单位为秒
     */
    @Override
    public void createTable(String tableNameStr, String familyName, int ttl) {
        LOGGER.info("tableNameStr:{}, familyName:{}, ttl:{}", tableNameStr, familyName, ttl);
        // 数据库元数据操作对象
        try (Admin admin = getConnection().getAdmin()) {
            // 检查表是否存在
            final TableName tableName = TableName.valueOf(tableNameStr);
            if (admin.tableExists(tableName)) {
                LOGGER.error("create table failed！table: '{}' is exists!", tableNameStr);
            } else {
                // 数据表描述对象
                final HTableDescriptor tableDesc = new HTableDescriptor(tableName);
                // 列族描述对象
                final HColumnDescriptor family = new HColumnDescriptor(familyName); //NOPMD
                family.setTimeToLive(ttl);
                tableDesc.addFamily(family);
                admin.createTable(tableDesc);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error("create table '{}' failed! this table name is reserved, detail: {}", tableNameStr, e);
            throw new IllegalArgumentException(e);
        } catch (MasterNotRunningException e) {
            LOGGER.error("create table '{}' failed! hbase master is not running, detail: {}", tableNameStr, e);
            throw new IllegalArgumentException(e);
        } catch (TableExistsException e) {
            LOGGER.error("create table '{}' failed! the table is exists, detail: {}", tableNameStr, e);
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            LOGGER.error("create table failed! table: {}, network exception occurs? detail: {}", tableNameStr, e);
            throw new IllegalArgumentException(e);
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
            LOGGER.error("table: {} is not exists!", tableNameStr);
            return null;
        }

        Result result = null;
        try (Table table = getConnection().getTable(TableName.valueOf(tableNameStr))) {
            // 这里的table名需要注意是否为default命名空间，即：default.tableName
            final Get get = new Get(Bytes.toBytes(rowKey));
            result = table.get(get); //NOPMD
            LOGGER.debug("result: {}", result);
        } catch (IOException e) {
            LOGGER.error("query table by rowKey failed! table: {}, rowKey: {}, network exception occurs? detail: {}",
                    tableNameStr, rowKey, e);
            throw new IllegalArgumentException(e);
        }
        return HBaseDaoUtil.getCells(result);
    }

    /**
     * 通过rowKey查询，并通过columns过滤.
     *
     * @param tableNameStr 表名
     * @param rowKey       rowKey
     * @param column       过滤的列名(key为family，value为qualifier)
     * @return cellList
     */
    @Override
    public List<Cell> queryTableByRowKey(final String tableNameStr, final String rowKey,
                                         final Map<String, List<String>> column) {
        if (!isExists(tableNameStr)) {
            LOGGER.error("table: {} is not exists!", tableNameStr);
            return null;
        }

        Result result = null;
        try (Table table = getConnection().getTable(TableName.valueOf(tableNameStr))) {
            // 这里的table名需要注意是否为default命名空间，即：default.tableName
            final Get get = new Get(Bytes.toBytes(rowKey));
            for (final String family : column.keySet()) {
                for (final String qualifier : column.get(family)) {
                    get.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
                }
            }
            result = table.get(get); //NOPMD
            LOGGER.debug("result: {}", result);
        } catch (IOException e) {
            LOGGER.error("query table by rowKey failed! table: {}, rowKey: {}, column: {}, network exception occurs? detail: {}",
                    tableNameStr, rowKey, column, e);
            throw new IllegalArgumentException(e);
        }
        return HBaseDaoUtil.getCells(result);
    }

    /**
     * 查询表中的所有数据（全表扫描）.
     *
     * @param tableNameStr 表名
     * @return 表中所有数据
     */
    @Override
    public List<Result> queryAll(String tableNameStr) {
        if (!isExists(tableNameStr)) {
            LOGGER.error("table: {} is not exists!", tableNameStr);
            return null;
        }
        ResultScanner resultScanner = null;
        List<Result> results = new ArrayList<Result>();
        try (Table table = getConnection().getTable(TableName.valueOf(tableNameStr))) {
            Scan scan = new Scan();
            resultScanner = table.getScanner(scan);
            for (Result result : resultScanner) {
                results.add(result);
            }
        } catch (IOException e) {
            LOGGER.error("query all failed! table: {},network exception occurs? detail: {}", tableNameStr, e);
            throw new IllegalArgumentException(e);
        } finally {
            IOUtils.closeStream(resultScanner);
        }
        return results;
    }

    /**
     * 根据rowKey范围查找.
     *
     * @param tableNameStr 表名
     * @param startRowKey  起始（包含）
     * @param stopRowKey   止于（不包含）
     * @return results
     */
    @Override
    public List<Result> query(String tableNameStr, String startRowKey, String stopRowKey) {
        if (!isExists(tableNameStr)) {
            LOGGER.error("table: {} is not exists!", tableNameStr);
            return null;
        }
        ResultScanner resultScanner = null;
        List<Result> results = new ArrayList<Result>();
        try (Table table = getConnection().getTable(TableName.valueOf(tableNameStr))) {
            Scan scan = new Scan();
            scan.setStartRow(Bytes.toBytes(startRowKey));
            scan.setStopRow(Bytes.toBytes(stopRowKey));

            resultScanner = table.getScanner(scan);
            for (Result result : resultScanner) {
                results.add(result);
            }
        } catch (IOException e) {
            LOGGER.error("query failed! table: {} startRowKey:{}, stopRowKey:{},network exception occurs? detail: {}",
                    tableNameStr, startRowKey, stopRowKey, e);
            throw new IllegalArgumentException(e);
        } finally {
            IOUtils.closeStream(resultScanner);
        }
        return results;
    }

    /**
     * 根据rowKey范围分页查找.
     *
     * @param tableNameStr 表名
     * @param startRowKey  起始（包含）
     * @param stopRowKey   止于（不包含）
     * @param pageSize     页大小
     * @return results
     */
    @Override
    public List<Result> query(String tableNameStr, String startRowKey, String stopRowKey, int pageSize) {
        if (!isExists(tableNameStr)) {
            LOGGER.error("table: {} is not exists!", tableNameStr);
            return null;
        }
        ResultScanner resultScanner = null;
        List<Result> results = new ArrayList<Result>();
        try (Table table = getConnection().getTable(TableName.valueOf(tableNameStr))) {
            Scan scan = new Scan();
            if (startRowKey != null && !startRowKey.isEmpty()) {
                scan.setStartRow(Bytes.toBytes(startRowKey));
            }
            if (stopRowKey != null && !stopRowKey.isEmpty()) {
                scan.setStopRow(Bytes.toBytes(stopRowKey));
            }
            // 不轻易使用filter，因为速度很慢，如果要用，建议使用前缀filter：PrefixFilter，还有协助分页的filter：PageFilter
            // 分页filter
            scan.setFilter(new PageFilter(pageSize));

//            // 协助缓存的
//            scan.setCacheBlocks()
//            scan.setCaching()
            resultScanner = table.getScanner(scan);
            for (Result result : resultScanner) {
                results.add(result);
            }
        } catch (IOException e) {
            LOGGER.error("query failed! table:{} startRowKey:{}, stopRowKey:{},pageSize:{}, network exception occurs? detail: {}",
                    tableNameStr, startRowKey, stopRowKey, pageSize, e);
            throw new IllegalArgumentException(e);
        } finally {
            IOUtils.closeStream(resultScanner);
        }
        return results;
    }

    /**
     * 多个过滤器过滤.
     *
     * @param tableNameStr 表名
     * @param filter       过滤器
     * @return results
     */
    @Override
    public List<Result> query(String tableNameStr, Filter filter) {
        if (!isExists(tableNameStr)) {
            LOGGER.error("table: {} is not exists!", tableNameStr);
            return null;
        }
        ResultScanner resultScanner = null;
        List<Result> results = new ArrayList<Result>();
        try (Table table = getConnection().getTable(TableName.valueOf(tableNameStr))) {
            Scan scan = new Scan();
            scan.setFilter(filter);
            resultScanner = table.getScanner(scan);
            for (Result result : resultScanner) {
                results.add(result);
            }
        } catch (IOException e) {
            LOGGER.error("query failed! table:{} filter:{},network exception occurs? detail: {}",
                    tableNameStr, filter, e);
            throw new IllegalArgumentException(e);
        } finally {
            IOUtils.closeStream(resultScanner);
        }
        return results;
    }

    /**
     * 获取Table，注意用完及时close表.
     *
     * @param tableNameStr 表名
     * @return table
     */
    @Override
    public Table getTable(String tableNameStr) {
        try {
            return getConnection().getTable(TableName.valueOf(tableNameStr));
        } catch (IOException e) {
            LOGGER.error("table failed! table:{},network exception occurs? detail: {}", tableNameStr, e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 根据rowKey插入值，即只有一个rowKey.
     * <p>
     * TODO 增加是否使用缓冲区选项，默认不用
     * TODO value可否设置为Object，如何更灵活的插入数据？
     * 一般family越少越好，名越短越好，详细见doc中的参考：HBase入门实例: Table中Family和Qualifier的关系与区别
     *
     * @param tableNameStr    表名
     * @param rowKey          rowKey
     * @param family          列族
     * @param qualifierValues 列及值
     */
    @Override
    public void insert(String tableNameStr, String rowKey, String family, Map<String, String> qualifierValues) {
        if (!isExists(tableNameStr)) {
            LOGGER.error("table: {} is not exists!", tableNameStr);
            return;
        }
        LOGGER.debug("tableNameStr:{}, rowKey:{}, qualifierValues:{}, qualifierValues.size: {}",
                tableNameStr, rowKey, qualifierValues, qualifierValues.size());

        try (Table table = getConnection().getTable(TableName.valueOf(tableNameStr))) {
            Put put = new Put(Bytes.toBytes(rowKey));
            for (String qualifier : qualifierValues.keySet()) {
                put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(qualifierValues.get(qualifier)));
            }
            LOGGER.debug("insert 'put' all cells size is {}", put.size());
            table.put(put);
        } catch (IOException e) {
            LOGGER.error("query table by rowKey failed! table: {}, rowKey: {}, network exception occurs? detail: {}",
                    tableNameStr, rowKey, e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 根据rowKey插入值，即只有一个rowKey.
     *
     * @param tableNameStr    表名
     * @param rowKey          rowKey
     * @param family          列族
     * @param qualifierValues 列及值
     */
    @Override
    public void insert(String tableNameStr, byte[] rowKey, String family, Map<String, String> qualifierValues) {
        if (!isExists(tableNameStr)) {
            LOGGER.error("table: {} is not exists!", tableNameStr);
            return;
        }
        LOGGER.debug("tableNameStr:{}, rowKey:{}, qualifierValues:{}, qualifierValues.size: {}",
                tableNameStr, rowKey, qualifierValues, qualifierValues.size());

        try (Table table = getConnection().getTable(TableName.valueOf(tableNameStr))) {
            Put put = new Put(rowKey);
            for (String qualifier : qualifierValues.keySet()) {
                put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(qualifierValues.get(qualifier)));
            }
            LOGGER.debug("insert 'put' all cells size is {}", put.size());
            table.put(put);
        } catch (IOException e) {
            LOGGER.error("query table by rowKey failed! table: {}, rowKey: {}, network exception occurs? detail: {}",
                    tableNameStr, rowKey, e);
            throw new IllegalArgumentException(e);
        }
    }


    /**
     * 删除表.
     *
     * @param tableNameStr 表名
     */
    @Override
    public void deleteTable(final String tableNameStr) {
        try (Admin admin = getConnection().getAdmin()) {
            final TableName tableName = TableName.valueOf(tableNameStr);
            // 删除前检查表是否不存在，不存在，则警告。
            if (!admin.tableExists(tableName)) {
                LOGGER.warn("delete table failed! table: {}, not exists!", tableNameStr);
                return;
            }
            // 禁用该表
            admin.disableTable(tableName);
            // 删除该表
            admin.deleteTable(tableName);
        } catch (TableNotFoundException e) {
            // 删除表为同步操作，之前检查过，仍有可能是因为表不存在而失败！
            LOGGER.warn("delete table failed! table: {}, not exists! detail: {}", tableNameStr, e);
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            LOGGER.error("delete table failed! table: {}, network exception occurs? detail: {}", tableNameStr, e);
            throw new IllegalArgumentException(e);
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
        try (Admin admin = getConnection().getAdmin()) {
            result = admin.tableExists(TableName.valueOf(tableNameStr));
            // admin继承了AutoCloseable，在try-with-resources中不需要手动关闭。
        } catch (IOException e) {
            LOGGER.error("check table isExists failed! table: {}, network exception occurs? detail: {}", tableNameStr, e);
            throw new IllegalArgumentException(e);
        }
        return result;
    }

    /**
     * 检查表是否启动.
     *
     * @param tableNameStr 表名
     * @return 是否启动
     */
    @Override
    public boolean isActive(String tableNameStr) {
        boolean result = false;
        try (Admin admin = getConnection().getAdmin()) {
//            admin.isTableAvailable(TableName.valueOf(tableNameStr))
            // 检查表是否启用
            result = admin.isTableEnabled(TableName.valueOf(tableNameStr));
        } catch (IOException e) {
            LOGGER.error("check table active failed! table: {}, network exception occurs? detail: {}", tableNameStr, e);
            throw new IllegalArgumentException(e);
        }
        return result;
    }

    /**
     * 获取table的regionLocator.
     *
     * @param tableNameStr 表名
     * @return regionLocator
     */
    public RegionLocator getRegionLocator(String tableNameStr) {
        try {
            return getConnection().getRegionLocator(TableName.valueOf(tableNameStr));
        } catch (IOException e) {
            LOGGER.error("get regionLocator! tableNameStr: {}, network exception occurs? detail: {}", tableNameStr, e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 获取table的regionLocator.
     *
     * @param table 表
     * @return regionLocator
     */
    public RegionLocator getRegionLocator(Table table) {
        try {
            return getConnection().getRegionLocator(table.getName());
        } catch (IOException e) {
            LOGGER.error("get regionLocator! table: {}, network exception occurs? detail: {}", table, e);
            throw new IllegalArgumentException(e);
        }
    }

}
