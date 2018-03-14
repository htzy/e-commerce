package com.huangshihe.ecommerce.pub.config.threadpool;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

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
//        System.out.println("--target.getClass():"+inv.getTarget().getClass());
        // 获取增强对象
        Object enhanceObject = ExecutorManager.getInstance().get(inv.getTarget().getClass());
        // 获取线程池执行对象
        ThreadPoolExecutor executor = ExecutorManager.getInstance().getExecutor(inv.getTarget().getClass());
        // 提交到线程池中执行
        executor.submit(new Runnable() {
            @Override
            public void run() {
                inv.invoke();
                // 若需要返回自定义return value，需要自定义：MethodInterceptor方法，示例：com.jfinal.aop.Callback
//                inv.setReturnValue(2);
            }
        });

    }
}
