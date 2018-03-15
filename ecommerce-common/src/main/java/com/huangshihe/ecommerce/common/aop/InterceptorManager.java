package com.huangshihe.ecommerce.common.aop;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Intercerptor管理类.
 * <p>
 * Create Date: 2018-03-15 22:43
 *
 * @author huangshihe
 */
public class InterceptorManager {

    private static InterceptorManager instance = new InterceptorManager();

    public static final Interceptor[] NULL_INTERS = new Interceptor[0];

    private final ConcurrentHashMap<Class<? extends Interceptor>, Interceptor> singletonMap
            = new ConcurrentHashMap<Class<? extends Interceptor>, Interceptor>();

    private InterceptorManager() {

    }

    public static InterceptorManager getInstance() {
        return instance;
    }

    /**
     * 根据拦截类生成拦截对象，若该对象已存在，则直接获取该对象返回.
     *
     * @param interceptorClasses 拦截类
     * @return
     */
    public Interceptor[] createInterceptor(Class<? extends Interceptor>[] interceptorClasses) {
        if (interceptorClasses == null || interceptorClasses.length == 0) {
            return NULL_INTERS;
        }

        Interceptor[] result = new Interceptor[interceptorClasses.length];
        try {
            for (int i = 0; i < result.length; i++) {
                result[i] = singletonMap.get(interceptorClasses[i]);
                if (result[i] == null) {
                    result[i] = interceptorClasses[i].newInstance();
                    singletonMap.put(interceptorClasses[i], result[i]);
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
