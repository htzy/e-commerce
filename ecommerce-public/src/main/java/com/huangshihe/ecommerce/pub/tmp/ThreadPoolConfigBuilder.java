package com.huangshihe.ecommerce.pub.tmp;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Create Date: 2018-01-23 22:32
 *
 * @author huangshihe
 */
public class ThreadPoolConfigBuilder {


    private static class ThreadPoolConfigImpl implements IThreadPoolConfig {

        private Integer poolSize;
        private Integer maxPoolSize;
        private Long keepAliveTime;
        private Integer queueSize;
        private int priority = 5;
        private TimeUnit timeUnit;
        private transient RejectedExecutionHandler rejectedExecutionHandler;
        private int queueOverLoadWarning;
        private int queueRecoverWarning;
        private boolean deadLoopEnabled;
        private boolean starveEnabled;
        private boolean delayEnabled;
        private boolean queuePercentEnabled;
        private long deadLoopThreshold;
        private long starveThreshold;
        private long delay;
        private long queuePercent;



        @Override
        public Integer getPoolSize() {
            return poolSize;
        }

        @Override
        public Integer getMaxPoolSize() {
            return maxPoolSize;
        }

        @Override
        public Long getKeepAliveTime() {
            return keepAliveTime;
        }

        @Override
        public Integer getQueueSize() {
            return queueSize;
        }
    }
}

