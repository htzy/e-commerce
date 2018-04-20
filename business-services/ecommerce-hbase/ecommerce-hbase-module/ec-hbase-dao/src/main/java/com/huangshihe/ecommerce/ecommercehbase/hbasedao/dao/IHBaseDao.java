package com.huangshihe.ecommerce.ecommercehbase.hbasedao.dao;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.RegionLocator;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.Filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * hbase数据库访问接口.
 * <p>
 * Create Date: 2017-12-12 22:42
 *
 * @author huangshihe
 */
public interface IHBaseDao {

    /**
     * 创建表.
     *
     * @param tableNameStr 表名
     * @param familyNames  列族名
     * @param ttl          老化时间，单位为秒
     */
    void createTable(String tableNameStr, String[] familyNames, int ttl);

    /**
     * 创建表.
     *
     * @param tableNameStr 表名
     * @param familyName   列族名
     * @param ttl          老化时间，单位为秒
     *                     TODO 老化时间的具体含义？当时间到了之后，能否把整个表删除，设置表的ttl？
     */
    void createTable(String tableNameStr, String familyName, int ttl);

    /**
     * 通过rowKey查询.
     *
     * @param tableNameStr 表名
     * @param rowKey       rowKey
     * @return cellList
     */
    List<Cell> queryTableByRowKey(String tableNameStr, String rowKey);

    /**
     * 通过rowKey查询，并通过columns过滤.
     *
     * @param tableNameStr 表名
     * @param rowKey       rowKey
     * @param column       过滤的列名(key为family，value为qualifier)
     * @return cellList
     */
    List<Cell> queryTableByRowKey(String tableNameStr, String rowKey, Map<String, List<String>> column);


    /**
     * 查询表中的所有数据（全表扫描）.
     *
     * @param tableNameStr 表名
     * @return 表中所有数据
     */
    List<Result> queryAll(String tableNameStr);

    /**
     * 根据rowKey范围查找.
     *
     * @param tableNameStr 表名
     * @param startRowKey  起始（包含）
     * @param stopRowKey   止于（不包含）
     * @return results
     */
    List<Result> query(String tableNameStr, String startRowKey, String stopRowKey);

    /**
     * 根据rowKey范围分页查找.
     *
     * @param tableNameStr 表名
     * @param startRowKey  起始（包含）
     * @param stopRowKey   止于（不包含）
     * @param pageSize     页大小
     * @return results
     */
    List<Result> query(String tableNameStr, String startRowKey, String stopRowKey, int pageSize);

    /**
     * 多个过滤器过滤.
     *
     * @param tableNameStr 表名
     * @param filter       过滤器
     * @return results
     */
    List<Result> query(String tableNameStr, Filter filter);

    /**
     * 获取Table.
     *
     * @param tableNameStr 表名
     * @return table
     */
    Table getTable(String tableNameStr);


    /**
     * 根据rowKey插入值，即只有一个rowKey.
     *
     * @param tableNameStr    表名
     * @param rowKey          rowKey
     * @param family          列族
     * @param qualifierValues 列及值
     */
    void insert(String tableNameStr, String rowKey, String family, Map<String, String> qualifierValues);

    /**
     * 根据rowKey插入值，即只有一个rowKey.
     *
     * @param tableNameStr    表名
     * @param rowKey          rowKey
     * @param family          列族
     * @param qualifierValues 列及值
     */
    void insert(String tableNameStr, byte[] rowKey, String family, Map<String, String> qualifierValues);

//
//    public void queryTableByCondition();
//
//    public void truncateTable();
//

    /**
     * 删除表.
     *
     * @param tableNameStr 表名
     */
    void deleteTable(String tableNameStr);


    /**
     * 检查表是否存在.
     *
     * @param tableNameStr 表名
     * @return 是否存在
     */
    boolean isExists(String tableNameStr);

    /**
     * 检查表是否启动.
     *
     * @param tableNameStr 表名
     * @return 是否启动
     */
    boolean isActive(String tableNameStr);
//
//    public void deleteByRowKey();
//
//    public void deleteByCondition();
//
//    public void addColumnFamily();
//
//    public void deleteColumnFamily();
//
//    public void insert();

    /**
     * 获取table的regionLocator.
     *
     * @param tableNameStr 表名
     * @return regionLocator
     */
    RegionLocator getRegionLocator(String tableNameStr);

    /**
     * 获取table的regionLocator.
     *
     * @param table 表
     * @return regionLocator
     */
    RegionLocator getRegionLocator(Table table);
}
