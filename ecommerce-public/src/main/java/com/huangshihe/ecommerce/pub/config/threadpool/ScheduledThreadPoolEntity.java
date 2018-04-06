package com.huangshihe.ecommerce.pub.config.threadpool;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * 定时任务线程池实体.
 * 这里注意：定时任务中的方法必须是静态的，否则将无法执行。
 * <p>
 * Create Date: 2018-04-06 19:21
 *
 * @author huangshihe
 */
public class ScheduledThreadPoolEntity implements Serializable {

    /**
     * 线程数.
     */
    private int poolSize;

    /**
     * 开始时间.
     */
    private String beginTime;

    /**
     * 间隔，运行周期.
     */
    private long period;

    @XmlAttribute(name = "poolSize", required = true)
    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    @XmlAttribute(name = "beginTime", required = true)
    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    @XmlAttribute(name = "period", required = true)
    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "ScheduledThreadPoolEntity{" +
                "poolSize=" + poolSize +
                ", beginTime='" + beginTime + '\'' +
                ", period=" + period +
                '}';
    }
}
