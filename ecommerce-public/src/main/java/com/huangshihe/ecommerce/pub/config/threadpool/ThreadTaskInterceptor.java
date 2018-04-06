package com.huangshihe.ecommerce.pub.config.threadpool;


import com.huangshihe.ecommerce.common.aop.Interceptor;
import com.huangshihe.ecommerce.common.aop.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        inv.invoke();
                    }
                });
            } catch (Exception e) {
                LOGGER.error("wrong occur when task submit to thread pool, detail:{}", e);
                throw new RuntimeException(e);
            }
        }
    }
}
