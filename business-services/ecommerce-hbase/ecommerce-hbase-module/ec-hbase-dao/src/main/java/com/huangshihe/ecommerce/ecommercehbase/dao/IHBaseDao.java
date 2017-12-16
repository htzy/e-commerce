package com.huangshihe.ecommerce.ecommercehbase.dao;

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
     * @return 是否创建成功
     */
    boolean createTable(String tableNameStr, String[] familyNames, int ttl);

//    public void queryTable();
//
//    public void queryTableByRowKey();
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
