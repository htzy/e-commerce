package com.huangshihe.ecommerce.ecommercehbase.hbaseservice.services;

/**
 * 原始数据服务类接口.
 * <p>
 * Create Date: 2018-03-19 19:20
 *
 * @author huangshihe
 */
public interface IOriginalService {

    /**
     * 获取当前表名.
     *
     * @return 当天表名
     */
    String getTodayTableName();

    /**
     * 获取所有的qualifiers.
     *
     * @return qualifiers
     */
    String[] getQualifiers();

    /**
     * 每日新建表.
     */
    void createDaily();

    /**
     * 检查当天表是否存在.
     *
     * @return 是否存在
     */
    boolean isTodayTableExists();

    /**
     * 检查当天表是否存在且为active状态.
     *
     * @return 是否启动
     */
    boolean isTodayTableActive();

    /**
     * 删除当天表，由于设置了ttl，将会自动删除，目前没有需要手动删除的场景，但暂时保留该方法。
     */
    @Deprecated
    void deleteTodayTable();
}
