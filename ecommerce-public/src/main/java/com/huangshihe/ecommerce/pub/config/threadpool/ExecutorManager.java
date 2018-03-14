package com.huangshihe.ecommerce.pub.config.threadpool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程执行管理类.
 * <p>
 * Create Date: 2018-03-14 23:11
 *
 * @author huangshihe
 */
public class ExecutorManager {

    // 存放线程池执行对象
    private Map<String, ThreadPoolExecutor> executorMap = new ConcurrentHashMap<String, ThreadPoolExecutor>();

    // 存放增强对象
    private Map<Class<?>, Object> objectMap = new ConcurrentHashMap<Class<?>, Object>();

    // 存放线程池执行对象和增强对象的关系，以增强对象类作为key，线程池执行对象name作为value
    private Map<Class<?>, String> relationMap = new HashMap<Class<?>, String>();

    private static ExecutorManager me = new ExecutorManager();

    public static ExecutorManager getInstance() {
        return me;
    }

    public Map<String, ThreadPoolExecutor> getExecutorMap() {
        return executorMap;
    }

    public Map<Class<?>, Object> getObjectMap() {
        return objectMap;
    }

    @Deprecated
    public void add(Class<?> cls, Object object) {
        objectMap.putIfAbsent(cls, object);
    }

    @Deprecated
    public void add(String executorName, ThreadPoolExecutor executor) {
        executorMap.putIfAbsent(executorName, executor);
    }

    /**
     *
     */
    public void add(Class<?> cls, Object object, String executorName, ThreadPoolExecutor executor) {
        objectMap.putIfAbsent(cls, object);
        executorMap.putIfAbsent(executorName, executor);
        relationMap.putIfAbsent(cls, executorName);
    }

    public Object get(Class<?> cls) {
        return objectMap.get(cls);
    }

    public ThreadPoolExecutor getExecutor(Class<?> cls) {

        // 首先到关系中根据class找到对应的池子名字, TODO executorName修改为poolServiceName？identity
        String executorName;
        // 如果是增强类，则应获取增强类的父类才是原始的业务类
        if (cls.getName().indexOf("$$EnhancerByCGLIB") != -1) {
            executorName = relationMap.get(cls.getSuperclass());
        } else {
            executorName = relationMap.get(cls);
        }

        // 然后根据池子名字找到对应的executor
        return executorMap.get(executorName);
    }

    public ThreadPoolExecutor getExecutor(String executorName) {
        return executorMap.get(executorName);
    }
}
