package com.huangshihe.ecommerce.common.aop;

import com.huangshihe.ecommerce.common.kits.AopKit;
import com.huangshihe.ecommerce.common.kits.ArrayKit;
import com.huangshihe.ecommerce.common.kits.ClassKit;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Interceptor管理类.
 * 粒度：method级别
 * <p>
 * Create Date: 2018-03-15 22:43
 *
 * @author huangshihe
 */
public class InterceptorManager {

    private static InterceptorManager instance = new InterceptorManager();


    /**
     * key: 拦截器类；value: 拦截器对象
     */
    private final ConcurrentHashMap<Class<? extends Interceptor>, Interceptor> singletonMap
            = new ConcurrentHashMap<Class<? extends Interceptor>, Interceptor>();

    /**
     * key: 待拦截的类；value: 拦截器对象数组
     * 即：该类可以有多个拦截器对象
     */
    @Deprecated
    private final ConcurrentHashMap<Class<?>, Interceptor[]> serviceClassInters = new ConcurrentHashMap<Class<?>, Interceptor[]>();

    /**
     * key: 待拦截的方法；value: 拦截器对象数组
     * 即：该类可以有多个拦截器对象
     */
    private final ConcurrentHashMap<Method, Interceptor[]> serviceMethodInters = new ConcurrentHashMap<Method, Interceptor[]>();

    private InterceptorManager() {

    }

    public static InterceptorManager getInstance() {
        return instance;
    }

    /**
     * 新增service类及对应的拦截类对象.
     *
     * @param serviceClass 业务类
     * @param interceptors 拦截器类
     */
    @Deprecated
    public void createServiceInterceptor(Class<?> serviceClass, Class<? extends Interceptor>[] interceptors) {
        if (interceptors == null || interceptors.length == 0) {
            return;
        }
        // 如果已有对应的拦截器，则追加此次新的拦截器
        Interceptor[] result = serviceClassInters.get(serviceClass);
        // 因为Interceptor对象是根据拦截器类从singletonMap取出来的，所以同一个拦截器类的多个对象是一个对象，故这里可以直接通过equals来判断是否相同
        serviceClassInters.put(serviceClass, ArrayKit.mergeWithNoSame(result, createInterceptor(interceptors)));
    }

    public void createMethodInterceptor(Method method, Class<? extends Interceptor>[] interceptors) {
        if (method == null || interceptors == null || interceptors.length == 0) {
            return;
        }
        // 如果已有对应的拦截器，则追加此次新的拦截器
        Interceptor[] result = serviceMethodInters.get(method);
        // 因为Interceptor对象是根据拦截器类从singletonMap取出来的，所以同一个拦截器类的多个对象是一个对象，故这里可以直接通过equals来判断是否相同
        serviceMethodInters.put(method, ArrayKit.mergeWithNoSame(result, createInterceptor(interceptors)));
    }

    public void createMethodInterceptor(Class<?> serviceClass, Class<? extends Interceptor> interceptor) {
        if (serviceClass == null || interceptor == null) {
            return;
        }
        Method[] methods = ClassKit.getMethods(serviceClass);
        if (ArrayKit.isNotEmpty(methods)) {
            for (Method method : methods) {
                createMethodInterceptor(method, interceptor);
            }
        }
    }


    public void createMethodInterceptor(Method method, Interceptor interceptor) {
        if (method == null || interceptor == null) {
            return;
        }
        // 如果已有对应的拦截器，则追加这个新的拦截器
        Interceptor[] result = serviceMethodInters.get(method);
        // TODO
        // ///////////////////////////
//        serviceMethodInters.put(method, ArrayKit.addWithNoSame(result, interceptor));
        // ///////////////////////////
        serviceMethodInters.put(method, ArrayKit.mergeWithNoSame(result, new Interceptor[]{interceptor}));
    }

    public void createMethodInterceptor(Method method, Class<? extends Interceptor> interceptorClass) {
        if (method == null || interceptorClass == null) {
            return;
        }
        // 如果已有对应的拦截器，则追加这个新的拦截器
        Interceptor[] result = serviceMethodInters.get(method);
        Interceptor interceptor = createInterceptor(interceptorClass);

        //////////////////
//        serviceMethodInters.put(method, new Interceptor[]{interceptor});
//
//        result = serviceMethodInters.get(method);
//        result = new Interceptor[]{interceptor};

        //////////////////

        serviceMethodInters.put(method, ArrayKit.mergeWithNoSame(result, new Interceptor[]{interceptor}));

    }

    public void createMethodInterceptor(Method method, Interceptor[] interceptors) {
        if (method == null || ArrayKit.isEmpty(interceptors)) {
            return;
        }
        AopKit.checkInjectInterceptors(interceptors);
        // 如果已有对应的拦截器，则追加此次新的拦截器
        Interceptor[] result = serviceMethodInters.get(method);
        // 因为Interceptor对象是根据拦截器类从singletonMap取出来的，所以同一个拦截器类的多个对象是一个对象，故这里可以直接通过equals来判断是否相同
        serviceMethodInters.put(method, ArrayKit.mergeWithNoSame(result, interceptors));
    }

    /**
     * 对类的所有public方法（不包含继承的）都增加对应的拦截器.
     *
     * @param serviceClass 指定类
     * @param interceptors 拦截器
     */
    public void createMethodInterceptor(Class<?> serviceClass, Class<? extends Interceptor>[] interceptors) {
        if (serviceClass == null || ArrayKit.isEmpty(interceptors)) {
            return;
        }
        Method[] methods = ClassKit.getMethods(serviceClass);
        if (ArrayKit.isNotEmpty(methods)) {
            for (Method method : methods) {
                createMethodInterceptor(method, interceptors);
            }
        }
    }

    /**
     * 对类的所有public方法（不包含继承的）都增加对应的拦截器对象.
     *
     * @param cls          指定类
     * @param interceptors 拦截器对象
     */
    public void createMethodInterceptor(Class<?> cls, Interceptor[] interceptors) {
        if (cls == null || ArrayKit.isEmpty(interceptors)) {
            return;
        }
        AopKit.checkInjectInterceptors(interceptors);
        Method[] methods = ClassKit.getMethods(cls);
        if (ArrayKit.isNotEmpty(methods)) {
            for (Method method : methods) {
                createMethodInterceptor(method, interceptors);
            }
        }
    }

    /**
     * 查询某一个方法对应的拦截器.
     *
     * @param method 指定方法
     * @return 对应的拦截器
     */
    public Interceptor[] query(Method method) {
        return serviceMethodInters.get(method);
    }

    /**
     * 根据拦截类生成拦截对象，若该对象已存在，则直接获取该对象返回.
     *
     * @param interceptorClasses 拦截类
     * @return 拦截器对象数组
     */
    public Interceptor[] createInterceptor(Class<? extends Interceptor>[] interceptorClasses) {
        if (ArrayKit.isEmpty(interceptorClasses)) {
            return AopKit.NULL_INTERS;
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
            // TODO 细化异常
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Interceptor createInterceptor(Class<? extends Interceptor> interceptorClass) {
        if (interceptorClass == null) {
            return null;
        }
        try {
            Interceptor interceptor = singletonMap.get(interceptorClass);
            if (interceptor == null) {
                interceptor = interceptorClass.newInstance();
                singletonMap.put(interceptorClass, interceptor);
            }
            return interceptor;
            // TODO 细化异常
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
