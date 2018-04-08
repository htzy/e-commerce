package com.huangshihe.ecommerce.pub.config.threadpool;


import com.huangshihe.ecommerce.common.aop.Interceptor;
import com.huangshihe.ecommerce.common.aop.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程任务拦截器.
 * <p>
 * Create Date: 2018-03-14 22:30
 *
 * @author huangshihe
 */
public class ThreadTaskInterceptor implements Interceptor {

    private static Logger LOGGER = LoggerFactory.getLogger(ThreadTaskInterceptor.class);

    @Override
    public void intercept(Invocation inv) {
        // inv.getTarget().getClass() 为增强类，即原业务类的子类
        // 获取线程池执行对象
        ThreadPoolExecutor executor = ThreadPoolManager.getInstance()
                .getThreadPoolExecutor(inv.getTarget().getClass(), inv.getMethod());
        if (executor != null) {
            try {
                // 提交到线程池中执行
                // 这里提交到线程池中后，方法执行不一定能取到结果，如果方法的返回值不为空，则需要异步运行完毕才能取到结果。
                Future<?> future = executor.submit(inv::invoke);
                LOGGER.debug("invoke已提交到线程池");

                if (inv.getMethod().getReturnType() != Void.TYPE) {
                    // 到这里等结果
                    // 在没有加get()之前debug模式下慢慢单步调，也可以过，快速debug也会失败，直接运行也失败
                    // 而这里手动调用get()方法即可以任意方式跑通，FutureTask.get()方法会等待任务完成再返回null
                    while (future.get() != null) {
                        // TODO 这里将会阻塞主线程！，当前的线程为main，只有提交到线程池中的任务才是子线程
                        Thread.sleep(5);
                        LOGGER.debug("卧槽，等了5ms了！");
                    }
                }
//                if (inv.getMethod().getReturnType() == Void.TYPE) {
//                    // 如果方法是返回值类型为void，则可以直接运行。
//                    LOGGER.debug("返回值类型为void的任务已开始执行...");
//                    executor.execute(inv::invoke);
//                    LOGGER.debug("返回值类型为void的任务已执行完...");
//                } else {
//                    LOGGER.debug("执行到返回值不是空的方法了！{}, 返回值：{}", inv.getMethod(), inv.getMethod().getReturnType());
//                    // 提交到线程池中执行
//                    // 这里提交到线程池中后，方法执行不一定能取到结果，如果方法的返回值不为空，则需要异步运行完毕才能取到结果。
//                    Future<?> future = executor.submit(inv::invoke);
//                    LOGGER.debug("invoke已提交到线程池");
//                    // 到这里等结果
//                    // 在没有加get()之前debug模式下慢慢单步调，也可以过，快速debug也会失败，直接运行也失败
//                    // 而这里手动调用get()方法即可以任意方式跑通，FutureTask.get()方法会等待任务完成再返回null
//                    while (future.get() != null) {
//                        // TODO 这里将会阻塞主线程！，当前的线程为main，只有提交到线程池中的任务才是子线程
//                        Thread.sleep(5);
//                        LOGGER.debug("卧槽，等了5ms了！");
//                    }
//                }
            } catch (Exception e) {
                LOGGER.error("wrong occur when task submit to thread pool, detail:{}", e);
                throw new RuntimeException(e);
            }
        }
    }
}
