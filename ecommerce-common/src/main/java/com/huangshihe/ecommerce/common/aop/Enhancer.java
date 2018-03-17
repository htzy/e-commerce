package com.huangshihe.ecommerce.common.aop;

/**
 * AOP增强类.
 * <p>
 * Create Date: 2018-03-15 22:41
 *
 * @author huangshihe
 */
@SuppressWarnings("unchecked")
public class Enhancer {
    /**
     * 对目标类注入拦截器.
     *
     * @param targetClass  目标类
     * @param injectInters 需注入的拦截器
     * @param <T>          类类型
     * @return 对目标类注入拦截器生成的目标对象，该对象其实是目标类的子类的实例.
     */
    public static <T> T enhance(Class<T> targetClass, Interceptor... injectInters) {
        return (T) net.sf.cglib.proxy.Enhancer.create(targetClass, new Callback(injectInters));
    }

    /**
     * 对目标类注入拦截器.
     *
     * @param targetClass       目标类
     * @param injectIntersClass 需注入的拦截器类
     * @param <T>               目标类类型
     * @return 对目标类注入拦截器生成的目标对象，该对象其实是目标类的子类的实例.
     */
    public static <T> T enhance(Class<T> targetClass, Class<? extends Interceptor> injectIntersClass) {
        return enhance(targetClass, createInjectInters(injectIntersClass));
    }

    /**
     * 生成拦截器对象.
     *
     * @param injectInterClasses 需注入的拦截器类
     * @return 拦截器对象
     */
    private static Interceptor[] createInjectInters(Class<? extends Interceptor>... injectInterClasses) {
        return InterceptorManager.getInstance().createInterceptor(injectInterClasses);
    }
}

