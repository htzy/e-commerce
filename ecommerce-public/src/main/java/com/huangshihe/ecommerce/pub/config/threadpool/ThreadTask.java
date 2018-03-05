package com.huangshihe.ecommerce.pub.config.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 线程任务.
 * <p>
 * Create Date: 2018-03-05 19:54
 *
 * @author huangshihe
 */
public class ThreadTask implements Runnable {

    /**
     * 日志.
     * TODO 日志中的线程名显示：pool-1-thread-1 修改改为服务名+线程名
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadTask.class);

    /**
     * 任务实体.
     */
    private TaskEntity taskEntity;

    /**
     * 构造方法.
     *
     * @param taskEntity 任务实体
     */
    public ThreadTask(TaskEntity taskEntity) {
        this.taskEntity = taskEntity;
    }


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            Class<?> classType = Class.forName(taskEntity.getClassName());
            Method method = classType.getMethod(taskEntity.getMethodName());
            method.invoke(classType);
        } catch (ClassNotFoundException e) {
            LOGGER.error("class {} not found, {}", taskEntity.getClassName(), e);
        } catch (NoSuchMethodException e) {
            LOGGER.error("method {} not found, {}", taskEntity.getMethodName(), e);
        } catch (IllegalAccessException e) {
            LOGGER.error("method {} in class {} , illegal access, {}",
                    taskEntity.getMethodName(), taskEntity.getClassName(), e);
        } catch (InvocationTargetException e) {
            LOGGER.error("method {} in class {} invocation target error, e",
                    taskEntity.getMethodName(), taskEntity.getClassName(), e);
        }
    }
}
