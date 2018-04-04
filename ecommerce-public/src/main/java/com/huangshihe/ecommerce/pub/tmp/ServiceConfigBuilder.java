package com.huangshihe.ecommerce.pub.tmp;

import com.huangshihe.ecommerce.pub.config.threadpool.ServiceEntity;
import com.huangshihe.ecommerce.pub.config.threadpool.TaskEntity;
import com.huangshihe.ecommerce.pub.config.threadpool.ThreadPoolEntity;

import java.util.Set;

/**
 * 线程服务配置建造者.
 * <p>
 * Create Date: 2018-01-23 21:45
 *
 * @author huangshihe
 */
public class ServiceConfigBuilder {

    private static class ServiceConfigImpl implements IServiceConfig {
        private ServiceEntity serviceEntity;
        private ThreadPoolEntity threadPoolEntity;
        private Set<TaskEntity> taskEntities;


        /**
         * 获取service的命名空间.
         *
         * @return namespace
         */
        @Override
        public String getServiceNameSpace() {
            return serviceEntity.getNameSpace();
        }

        /**
         * 获取service的名字.
         *
         * @return name
         */
        @Override
        public String getServiceName() {
            return serviceEntity.getName();
        }

        /**
         * 获取执行者实体.
         *
         * @return entity
         */
        @Override
        public ExecutorIdentity getExecutorEntity() {
            return new ExecutorIdentity().toIdentity(serviceEntity, threadPoolEntity);
        }

        /**
         * 获取线程池配置类.
         * 这里其实做的不是获取，而是将配置生成？？
         * TODO
         *
         * @return config
         */
        @Deprecated
        @Override
        public IThreadPoolConfig getThreadPoolConfig() {
//            IThreadPoolConfig config = new ThreadPoolConfigBuilder()
            return null;
        }

        /**
         * 获取所有的任务.
         *
         * @return taskEntities
         */
        @Override
        public Set<TaskEntity> getTaskEntities() {
            return null;
        }
    }

}
