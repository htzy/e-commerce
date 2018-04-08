package com.huangshihe.ecommerce.common.aop;

import com.huangshihe.ecommerce.common.kits.AopKit;
import com.huangshihe.ecommerce.common.kits.ArrayKit;
import com.huangshihe.ecommerce.common.kits.ClassKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(InterceptorManager.class);


    private static InterceptorManager _instance = new InterceptorManager();

    /**
     * key: 拦截器类；value: 拦截器对象
     */
    private final ConcurrentHashMap<Class<? extends Interceptor>, Interceptor> _intersMap
            = new ConcurrentHashMap<Class<? extends Interceptor>, Interceptor>();

    /**
     * key: 待拦截的方法；value: 拦截器对象数组
     * 即：该类可以有多个拦截器对象
     */
    private final ConcurrentHashMap<Method, Interceptor[]> _serviceMethodInters = new ConcurrentHashMap<Method, Interceptor[]>();

    private InterceptorManager() {

    }

    public static InterceptorManager getInstance() {
        return _instance;
    }

    /**
     * 创建方法拦截器对象
     *
     * @param method             方法
     * @param interceptorClasses 拦截器类
     */
    public void createMethodInterceptor(Method method, Class<? extends Interceptor>[] interceptorClasses) {
        if (method == null || ArrayKit.isEmpty(interceptorClasses)) {
            return;
        }
        // 如果已有对应的拦截器，则追加此次新的拦截器
        Interceptor[] result = _serviceMethodInters.get(method);
        // 因为Interceptor对象是根据拦截器类从singletonMap取出来的，所以同一个拦截器类的多个对象是一个对象，故这里可以直接通过equals来判断是否相同
        _serviceMethodInters.put(method, ArrayKit.mergeWithNoSame(result, createInterceptor(interceptorClasses)));
    }

    /**
     * 针对该类的所有方法创建拦截器.
     *
     * @param serviceClass     服务类
     * @param interceptorClass 拦截器类
     */
    public void createMethodInterceptor(Class<?> serviceClass, Class<? extends Interceptor> interceptorClass) {
        if (serviceClass == null || interceptorClass == null) {
            return;
        }
        Method[] methods = ClassKit.getMethods(serviceClass);
        if (ArrayKit.isNotEmpty(methods)) {
            for (Method method : methods) {
                createMethodInterceptor(method, interceptorClass);
            }
        }
    }

    /**
     * 创建方法拦截器对象.
     *
     * @param method      方法
     * @param interceptor 拦截器对象
     */
    public void createMethodInterceptor(Method method, Interceptor interceptor) {
        if (method == null || interceptor == null) {
            return;
        }
        // 如果已有对应的拦截器，则追加这个新的拦截器
        Interceptor[] result = _serviceMethodInters.get(method);
        // serviceMethodInters.put(method, ArrayKit.addWithNoSame(result, interceptor, result.getClass()));// 不可用，result可能为null
        // serviceMethodInters.put(method, ArrayKit.addWithNoSame(result, interceptor, Interceptor[].class));
        _serviceMethodInters.put(method, ArrayKit.mergeWithNoSame(result, new Interceptor[]{interceptor}));
    }

    /**
     * 创建方法拦截器对象.
     *
     * @param method           方法
     * @param interceptorClass 拦截器类
     */
    public void createMethodInterceptor(Method method, Class<? extends Interceptor> interceptorClass) {
        if (method == null || interceptorClass == null) {
            return;
        }
        // 如果已有对应的拦截器，则追加这个新的拦截器
        Interceptor[] result = _serviceMethodInters.get(method);
        Interceptor interceptor = createInterceptor(interceptorClass);

        _serviceMethodInters.put(method, ArrayKit.addWithNoSame(result, interceptor, Interceptor[].class));
    }

    /**
     * 创建方法拦截器.
     *
     * @param method       方法
     * @param interceptors 拦截器对象
     */
    public void createMethodInterceptor(Method method, Interceptor[] interceptors) {
        if (method == null || ArrayKit.isEmpty(interceptors)) {
            return;
        }
        AopKit.checkInjectInterceptors(interceptors);
        // 如果已有对应的拦截器，则追加此次新的拦截器
        Interceptor[] result = _serviceMethodInters.get(method);
        // 因为Interceptor对象是根据拦截器类从singletonMap取出来的，所以同一个拦截器类的多个对象是一个对象，故这里可以直接通过equals来判断是否相同
        _serviceMethodInters.put(method, ArrayKit.mergeWithNoSame(result, interceptors));
    }

    /**
     * 对类的所有public方法（不包含继承的）都增加对应的拦截器.
     *
     * @param serviceClass       指定类
     * @param interceptorClasses 拦截器类
     */
    public void createMethodInterceptor(Class<?> serviceClass, Class<? extends Interceptor>[] interceptorClasses) {
        if (serviceClass == null || ArrayKit.isEmpty(interceptorClasses)) {
            return;
        }
        Method[] methods = ClassKit.getMethods(serviceClass);
        if (ArrayKit.isNotEmpty(methods)) {
            for (Method method : methods) {
                createMethodInterceptor(method, interceptorClasses);
            }
        }
    }

    /**
     * 对类的所有public方法（不包含继承的）都增加对应的拦截器对象.
     *
     * @param serviceClass 指定类
     * @param interceptors 拦截器对象
     */
    public void createMethodInterceptor(Class<?> serviceClass, Interceptor[] interceptors) {
        if (serviceClass == null || ArrayKit.isEmpty(interceptors)) {
            return;
        }
        AopKit.checkInjectInterceptors(interceptors);
        Method[] methods = ClassKit.getMethods(serviceClass);
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
    public Interceptor[] queryMethodInters(Method method) {
        return _serviceMethodInters.get(method);
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
                result[i] = _intersMap.get(interceptorClasses[i]);
                if (result[i] == null) {
                    result[i] = interceptorClasses[i].newInstance();
                    _intersMap.put(interceptorClasses[i], result[i]);
                }
            }
            return result;
        } catch (IllegalAccessException | InstantiationException e) {
            LOGGER.error("create interceptorClasses:{} error! detail:{}", Arrays.toString(interceptorClasses), e);
            throw new IllegalArgumentException("create interceptorClasses error");
        }
    }

    /**
     * 根据拦截类生成拦截对象，若该对象已存在，则直接获取该对象返回.
     *
     * @param interceptorClass 拦截类
     * @return 拦截器对象
     */
    public Interceptor createInterceptor(Class<? extends Interceptor> interceptorClass) {
        if (interceptorClass == null) {
            return null;
        }
        try {
            Interceptor interceptor = _intersMap.get(interceptorClass);
            if (interceptor == null) {
                interceptor = interceptorClass.newInstance();
                _intersMap.put(interceptorClass, interceptor);
            }
            return interceptor;
        } catch (IllegalAccessException | InstantiationException e) {
            LOGGER.error("create interceptorClass:{} error! detail:{}", interceptorClass, e);
            throw new IllegalArgumentException("create interceptorClasses error");
        }
    }
}
