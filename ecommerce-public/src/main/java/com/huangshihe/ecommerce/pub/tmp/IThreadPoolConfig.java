package com.huangshihe.ecommerce.pub.tmp;

/**
 * 线程池配置类.
 * <p>
 * Create Date: 2018-01-23 21:41
 *
 * @author huangshihe
 */
public interface IThreadPoolConfig {

    Integer getPoolSize();

    Integer getMaxPoolSize();

    Long getKeepAliveTime();

    Integer getQueueSize();

    //
}
