package com.huangshihe.ecommerce.pub.config.threadpool;

import com.huangshihe.ecommerce.common.aop.Enhancer;
import com.huangshihe.ecommerce.common.kits.AopKit;
import com.huangshihe.ecommerce.pub.config.ConfigEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理类.
 * <p>
 * Create Date: 2018-03-17 23:01
 *
 * @author huangshihe
 */
public class ThreadPoolManager {
    private static ThreadPoolManager instance = new ThreadPoolManager();

    private ThreadPoolManager() {

    }

    public static ThreadPoolManager getInstance() {
        return instance;
    }

    // 存放线程池执行对象
    private Map<String, ThreadPoolExecutor> executorMap = new ConcurrentHashMap<String, ThreadPoolExecutor>();

    // 存放增强对象
    private Map<Class<?>, Object> enhancedObjectMap = new ConcurrentHashMap<Class<?>, Object>();

    // 存放线程池执行对象和增强对象的关系，以增强对象类作为key，线程池执行对象name作为value
    private Map<Class<?>, String> relationMap = new HashMap<Class<?>, String>();

    /**
     * 创建线程池.
     *
     * @param configEntity 配置实体
     */
    public void createPools(ConfigEntity configEntity) {
        for (ServiceConfigEntity serviceConfig : configEntity.getServiceConfigEntities()) {
            // 新建线程池
            ThreadPoolEntity pool = serviceConfig.getThreadPoolEntity();
            ThreadPoolExecutor executor = new ThreadPoolExecutor(pool.getPoolSize(), pool.getMaxPoolSize(),
                    pool.getKeepAliveTime(), TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
            for (TaskEntity task : serviceConfig.getTaskEntities()) {
                Class classType = null;
                try {
                    classType = Class.forName(task.getClassName());
                    // TODO 这里先不管是哪个方法，统一拦截所有方法
                    Object object = Enhancer.enhance(classType, ThreadTaskInterceptor.class);
                    add(classType, object, serviceConfig.getIdentity(), executor);

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public Map<String, ThreadPoolExecutor> getExecutorMap() {
        return executorMap;
    }

    public Map<Class<?>, Object> getEnhancedObjectMap() {
        return enhancedObjectMap;
    }

    @Deprecated
    public void add(Class<?> cls, Object object) {
        enhancedObjectMap.putIfAbsent(cls, object);
    }

    @Deprecated
    public void add(String executorName, ThreadPoolExecutor executor) {
        executorMap.putIfAbsent(executorName, executor);
    }

    /**
     * 增加线程.
     *
     * @param cls          被增强类
     * @param object       已增强对象
     * @param executorName 线程对象名
     * @param executor     线程执行对象
     */
    public void add(Class<?> cls, Object object, String executorName, ThreadPoolExecutor executor) {
        enhancedObjectMap.putIfAbsent(cls, object);
        executorMap.putIfAbsent(executorName, executor);
        relationMap.putIfAbsent(cls, executorName);
    }

    /**
     * 根据原类获取已增强对象.
     *
     * @param cls 被增强类
     * @return 已增强对象
     */
    public Object getEnhancedObject(Class<?> cls) {
        return enhancedObjectMap.get(cls);
    }

    /**
     * 根据原类获取属于该类的线程池执行对象（即该线程池中运行该类的任务）.
     *
     * @param cls 被增强类
     * @return 线程吃执行对象
     */
    public ThreadPoolExecutor getExecutor(Class<?> cls) {

        // 首先到关系中根据class找到对应的池子名字, TODO executorName修改为poolServiceName？identity
        String executorName;

        // 如果是增强类，则应获取增强类的父类才是原始的业务类
        if (cls.getName().contains(AopKit.ENHANCED_TAG)) {
            executorName = relationMap.get(cls.getSuperclass());
        } else {
            executorName = relationMap.get(cls);
        }

        // 然后根据池子名字找到对应的executor
        return executorMap.get(executorName);
    }

    /**
     * 根据线程执行对象名获取线程执行对象.
     *
     * @param executorName 线程执行对象名
     * @return 线程执行对象
     */
    public ThreadPoolExecutor getExecutor(String executorName) {
        return executorMap.get(executorName);
    }

}
