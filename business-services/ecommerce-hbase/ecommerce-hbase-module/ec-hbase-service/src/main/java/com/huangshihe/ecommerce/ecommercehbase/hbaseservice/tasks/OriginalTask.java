package com.huangshihe.ecommerce.ecommercehbase.hbaseservice.tasks;

import com.huangshihe.ecommerce.common.factory.ServicesFactory;
import com.huangshihe.ecommerce.ecommercehbase.hbaseservice.services.IOriginalService;
import com.huangshihe.ecommerce.ecommercehbase.hbaseservice.services.OriginalServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 原始表任务.
 * <p>
 * Create Date: 2018-04-07 14:13
 *
 * @author huangshihe
 */
public class OriginalTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(OriginalTask.class);

    private static IOriginalService originalService = ServicesFactory.getInstance().getServiceObject(OriginalServiceImpl.class);

    /**
     * 每日创建原始表.
     */
    public static void createDaily() {
        if (!originalService.isTodayTableExists()) {
            LOGGER.error("origin today table not exists! creating now!");
            originalService.createDaily();
            LOGGER.error("origin today table not exists! created already!");
        }
        // TODO 追加其他每日新增内容（该TODO一直保留）
    }
}
