package com.huangshihe.ecommerce.ecommercehbase.hbaseservice.services;

import com.huangshihe.ecommerce.common.kits.TimeKit;
import com.huangshihe.ecommerce.ecommercehbase.hbasedao.dao.HBaseDaoImpl;
import com.huangshihe.ecommerce.ecommercehbase.hbasedao.dao.IHBaseDao;
import com.huangshihe.ecommerce.ecommercehbase.hbaseservice.constants.OriginalConstant;

/**
 * 原始数据服务实现类.
 * <p>
 * Create Date: 2018-03-19 20:10
 *
 * @author huangshihe
 */
public class OriginalServiceImpl implements IOriginalService {

    private IHBaseDao hbaseDao = new HBaseDaoImpl();

    /**
     * 获取当前表名.
     *
     * @return 当天表名
     */
    @Override
    public String getTodayTableName() {
        return OriginalConstant.TABLE_NAME_PRE + TimeKit.getTodayDate();
    }

    /**
     * 获取所有的familyNames.
     *
     * @return familyNames
     */
    @Override
    public String[] getFamilyNames() {
        return new String[]{OriginalConstant.SSID, OriginalConstant.TYPE, OriginalConstant.CHANNEL,
                OriginalConstant.RSSI, OriginalConstant.DETAIL_TYPE,
                OriginalConstant.DELETE_FLAG, OriginalConstant.IS_ASSOCIATED};
    }

    /**
     * 每日新建表.
     */
    @Override
    public void createDaily() {
        hbaseDao.createTable(getTodayTableName(), getFamilyNames(), OriginalConstant.TTL);
    }
}
