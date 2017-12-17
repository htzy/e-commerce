package com.huangshihe.ecommerce.ecommercehbase.dao;

import org.apache.hadoop.hbase.Cell;

import java.util.List;

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

//    public void queryTable();
//

    /**
     * 通过rowKey查询.
     *
     * @param tableNameStr 表名
     * @param rowKey       rowKey
     * @return cellList
     */
    List<Cell> queryTableByRowKey(String tableNameStr, String rowKey);
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
