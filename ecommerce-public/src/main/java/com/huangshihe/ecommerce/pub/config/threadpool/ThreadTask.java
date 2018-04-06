package com.huangshihe.ecommerce.pub.config.threadpool;

import com.huangshihe.ecommerce.common.kits.ClassKit;
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
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadTask.class);

    /**
     * 任务实体.
     */
    private TaskEntity taskEntity;

    private Class<?> classType;

    private Method method;

    /**
     * 构造方法.
     *
     * @param taskEntity 任务实体
     */
    public ThreadTask(TaskEntity taskEntity) {
        this.taskEntity = taskEntity;

        try {
            classType = Class.forName(taskEntity.getClassName());
            method = classType.getMethod(taskEntity.getMethodName());
        } catch (ClassNotFoundException e) {
            LOGGER.error("class {} not found, {}", taskEntity.getClassName(), e);
        } catch (NoSuchMethodException e) {
            LOGGER.error("method {} not found, {}", taskEntity.getMethodName(), e);
        }
    }


    /**
     * 返回方法标识符.
     *
     * @return 方法标识符
     */
    public String getMethodIdentity() {
        return ClassKit.getMethodIdentity(classType, method);
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
            // 这里是只能执行静态方法，否则将无法执行
            method.invoke(classType);
        } catch (IllegalAccessException e) {
            LOGGER.error("method {} in class {} , illegal access, {}",
                    taskEntity.getMethodName(), taskEntity.getClassName(), e);
        } catch (InvocationTargetException e) {
            LOGGER.error("method {} in class {} invocation target error, detail:{}",
                    taskEntity.getMethodName(), taskEntity.getClassName(), e);
        } catch (IllegalArgumentException e) {
            // 这里是只能执行静态方法，否则这里将报错
            // java.lang.IllegalArgumentException: object is not an instance of declaring class
            LOGGER.error("method {} in class {} unknown target error, detail:{}",
                    taskEntity.getMethodName(), taskEntity.getClassName(), e);
        }
    }
}
