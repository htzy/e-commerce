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
     * 获取所有的familyNames.
     *
     * @return familyNames
     */
    String[] getFamilyNames();

    /**
     * 每日新建表.
     */
    void createDaily();
}
