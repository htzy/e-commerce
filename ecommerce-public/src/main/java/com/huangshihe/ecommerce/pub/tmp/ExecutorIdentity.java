package com.huangshihe.ecommerce.pub.tmp;

import com.huangshihe.ecommerce.pub.config.threadpool.ServiceEntity;
import com.huangshihe.ecommerce.pub.config.threadpool.ThreadPoolEntity;

/**
 * 执行者身份类，用于区分是哪一类执行者.
 * <p>
 * Create Date: 2018-01-23 21:29
 *
 * @author huangshihe
 */
public class ExecutorIdentity {

    private String nameSpace;

    private String name;

    private ExecutorType executorType;

    public ExecutorIdentity toIdentity(ServiceEntity serviceEntity, ThreadPoolEntity threadPoolEntity) {
        nameSpace = serviceEntity.getNameSpace();
        name = serviceEntity.getName();
//        executorType = ExecutorType.valueOf(threadPoolEntity.getRejectedExecutionHandlerStr());
        return this;
    }

    public ExecutorIdentity toThreadPoolIdentity(String nameSpace, String name) {
        this.nameSpace = nameSpace;
        this.name = name;
        this.executorType = ExecutorType.THREAD_POOL;
        return this;
    }

//    public ExecutorIdentity toScheduledPoolIdentity(String nameSpace, String name) {
//        this.nameSpace = nameSpace;
//        this.name = name;
//        this.executorType = ExecutorType.SCHEDULED_THREAD_POOL;
//        return this;
//    }

//    public ExecutorIdentity toSingleExecutorIdentity(String nameSpace, String name) {
//        this.nameSpace = nameSpace;
//        this.name = name;
//        this.executorType = ExecutorType.SINGLE_THREAD_EXECUTOR;
//        return this;
//    }

    public ExecutorIdentity toIdentity(String nameSpace, String name, ExecutorType executorType) {
        this.nameSpace = nameSpace;
        this.name = name;
        this.executorType = executorType;
        return this;
    }

    public ExecutorIdentity() {

    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExecutorType getExecutorType() {
        return executorType;
    }

    public void setExecutorType(ExecutorType executorType) {
        this.executorType = executorType;
    }

    public enum ExecutorType {
        THREAD_POOL("threadPool");
//        SCHEDULED_THREAD_POOL("scheduledThreadPool"),
//        SINGLE_THREAD_EXECUTOR("singleThreadExecutor");

        private String value;

        /**
         * 枚举的构造方法不允许在枚举外部使用，即不能实例化，所以不需要指定访问控制符.
         *
         * @param value value
         */
        ExecutorType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}

