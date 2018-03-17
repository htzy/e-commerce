package com.huangshihe.ecommerce.pub.config.threadpool;


import com.huangshihe.ecommerce.common.aop.Interceptor;
import com.huangshihe.ecommerce.common.aop.Invocation;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程任务拦截器.
 * <p>
 * Create Date: 2018-03-14 22:30
 *
 * @author huangshihe
 */
public class  ThreadTaskInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv) {
        // inv.getTarget().getClass() 为增强类，即原业务类的子类
        // 获取增强对象
//        Object enhanceObject = ThreadPoolManager.getInstance().getEnhancedObject(inv.getTarget().getClass());
        // 获取线程池执行对象
        ThreadPoolExecutor executor = ThreadPoolManager.getInstance().getExecutor(inv.getTarget().getClass());
        // 提交到线程池中执行
        executor.submit(new Runnable() {
            @Override
            public void run() {
                inv.invoke();
            }
        });

    }
}
