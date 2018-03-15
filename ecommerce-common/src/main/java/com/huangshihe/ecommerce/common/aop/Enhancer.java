package com.huangshihe.ecommerce.common.aop;

/**
 * 增强类.
 * <p>
 * Create Date: 2018-03-15 22:41
 *
 * @author huangshihe
 */
public class Enhancer {
    public static <T> T enhance(Class<T> targetClass, Interceptor... injectInters) {
        return (T) net.sf.cglib.proxy.Enhancer.create(targetClass, new Callback(injectInters));
    }

    public static <T> T enhance(Class<T> targetClass, Class<? extends Interceptor> injectIntersClass) {
        return (T) enhance(targetClass, createInjectInters(injectIntersClass));
    }

    private static Interceptor[] createInjectInters(Class<? extends Interceptor>... injectInterClasses) {
        return InterceptorManager.getInstance().createInterceptor(injectInterClasses);
    }
}

