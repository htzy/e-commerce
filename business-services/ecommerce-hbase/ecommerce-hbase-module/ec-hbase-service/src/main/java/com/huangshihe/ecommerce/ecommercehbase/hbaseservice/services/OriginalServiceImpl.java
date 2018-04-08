package com.huangshihe.ecommerce.ecommercehbase.hbaseservice.services;

import com.huangshihe.ecommerce.common.kits.TimeKit;
import com.huangshihe.ecommerce.ecommercehbase.hbasedao.dao.HBaseDaoImpl;
import com.huangshihe.ecommerce.ecommercehbase.hbasedao.dao.IHBaseDao;
import com.huangshihe.ecommerce.ecommercehbase.hbaseservice.constants.OriginalConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 原始数据服务实现类.
 * <p>
 * Create Date: 2018-03-19 20:10
 *
 * @author huangshihe
 */
public class OriginalServiceImpl implements IOriginalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OriginalServiceImpl.class);

    private IHBaseDao hbaseDao = new HBaseDaoImpl();

    /**
     * 获取当前表名.
     *
     * @return 当天表名
     */
    @Override
    public String getTodayTableName() {
        LOGGER.debug("todayTableName:{}", OriginalConstant.TABLE_NAME_PRE + TimeKit.getTodayDate());
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
        LOGGER.info("creating origin daily...");
        if (isTodayTableExists()) {
            throw new IllegalStateException("origin today table is exists! 当天表已存在！");
        } else {
            hbaseDao.createTable(getTodayTableName(), getFamilyNames(), OriginalConstant.TTL);
        }
        LOGGER.info("created origin daily...");
    }

    /**
     * 检查当天表是否存在.
     *
     * @return 是否存在
     */
    @Override
    public boolean isTodayTableExists() {
        return hbaseDao.isExists(getTodayTableName());
    }

    /**
     * 检查当天表是否存在且为active状态.
     *
     * @return 是否启动
     */
    @Override
    public boolean isTodayTableActive() {
        return hbaseDao.isActive(getTodayTableName());
    }

    /**
     * 删除当天表.
     */
    @Override
    public void deleteTodayTable() {
        hbaseDao.deleteTable(getTodayTableName());
    }
}
