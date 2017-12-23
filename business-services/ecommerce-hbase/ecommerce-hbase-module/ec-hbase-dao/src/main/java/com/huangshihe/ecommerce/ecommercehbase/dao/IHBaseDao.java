package com.huangshihe.ecommerce.ecommercehbase.dao;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;

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
     * @param ttl          老化时间
     */
    void createTable(String tableNameStr, String[] familyNames, int ttl);

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
     * @param cf           过滤的列名(key为family，value为column)
     * @return cellList
     */
    List<Cell> queryTableByRowKey(String tableNameStr, String rowKey, Map<String, List<String>> cf);


    /**
     * 查询表中的所有数据（全表扫描）.
     *
     * @param tableNameStr 表名
     * @return 表中所有数据
     */
    List<Result> queryAll(String tableNameStr);


    /**
     * 根据rowKey插入值，即只有一个rowKey.
     *
     * @param tableNameStr 表名
     * @param rowKey       rowKey
     * @param family       列族
     * @param columnValues 列及值
     */
    void insert(String tableNameStr, String rowKey, String family, Map<String, String> columnValues);

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
}
