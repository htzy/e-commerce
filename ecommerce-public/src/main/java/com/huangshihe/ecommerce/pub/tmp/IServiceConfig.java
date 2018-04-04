package com.huangshihe.ecommerce.pub.tmp;

import com.huangshihe.ecommerce.pub.config.threadpool.TaskEntity;

import java.io.Serializable;
import java.util.Set;

/**
 * 线程服务配置.
 * <p>
 * Create Date: 2018-01-23 21:28
 *
 * @author huangshihe
 */
public interface IServiceConfig extends Serializable {

    /**
     * 获取service的命名空间.
     *
     * @return namespace
     */
    String getServiceNameSpace();

    /**
     * 获取service的名字.
     *
     * @return name
     */
    String getServiceName();

    /**
     * 获取执行者实体.
     *
     * @return entity
     */
    ExecutorIdentity getExecutorEntity();

    /**
     * 获取线程池配置类.
     *
     * @return config
     */
    IThreadPoolConfig getThreadPoolConfig();

    /**
     * 获取所有的任务.
     *
     * @return taskEntities
     */
    Set<TaskEntity> getTaskEntities();


}
