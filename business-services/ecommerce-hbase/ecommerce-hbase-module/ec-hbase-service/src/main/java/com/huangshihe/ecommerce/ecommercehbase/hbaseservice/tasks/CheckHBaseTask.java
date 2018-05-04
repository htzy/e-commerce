package com.huangshihe.ecommerce.ecommercehbase.hbaseservice.tasks;

import com.huangshihe.ecommerce.common.factory.ServicesFactory;
import com.huangshihe.ecommerce.ecommercehbase.hbaseservice.services.IOriginalService;
import com.huangshihe.ecommerce.ecommercehbase.hbaseservice.services.OriginalServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HBase检查任务.
 * <p>
 * Create Date: 2018-04-07 14:29
 *
 * @author huangshihe
 */
public class CheckHBaseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckHBaseTask.class);

    private static IOriginalService originalService =
            ServicesFactory.getInstance().getServiceObjectOrNew(OriginalServiceImpl.class);
//            ServicesFactory.getInstance().getServiceObject(OriginalServiceImpl.class);
//     TODO 这里如果取线程池中的originalService对象，就会出现莫名其妙的问题？？？
    // 当前表现出的问题为：检查任务（某线程池中）只能进行一半，而不管主线程等待多久，都等不到结果？

    // 配置文件：hbase_task-cfg.xml中设置

    public static void check() {
        LOGGER.debug("begin to check origin......");
        // 检查当前表是否已存在，若不存在，则新建，(每日新建任务可能失败，或每日新建任务只在凌晨触发，所以有可能表未新建)
        if (!originalService.isTodayTableExists() || !originalService.isTodayTableActive()) {
            originalService.createDaily();
        }
        LOGGER.debug("check origin finish......");

        // TODO 新增其他检查内容（该TODO一直保留）

    }
}
