package com.huangshihe.ecommerce.pub.config.threadpool;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * 线程池实体类.
 * <p>
 * Create Date: 2018-01-23 21:48
 *
 * @author huangshihe
 */
//@XmlRootElement(name = "threadPool")
public class ThreadPoolEntity implements Serializable {

    /**
     * 核心线程数.
     */
    private Integer poolSize;

    /**
     * 最大线程数.
     */
    private Integer maxPoolSize;

    /**
     * 队列大小.
     */
    private Integer queueSize;

    /**
     * 除核心线程数之外的空闲线程存活时间，单位毫秒.
     */
    private long keepAliveTime;

//    private Integer priority;
//    private String rejectedExecutionHandlerStr;
//    private WarningEntity warningEntity;
//    private AbnormalDetectEntity abnormalDetectEntity;

    @XmlAttribute(name = "poolSize")
    public Integer getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    @XmlAttribute(name = "maxPoolSize")
    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    @XmlAttribute(name = "queueSize")
    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    @XmlAttribute(name = "keepAliveTime")
    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    @Override
    public String toString() {
        return "ThreadPoolEntity{" +
                "poolSize=" + poolSize +
                ", maxPoolSize=" + maxPoolSize +
                ", queueSize=" + queueSize +
                ", keepAliveTime=" + keepAliveTime +
                '}';
    }
}
