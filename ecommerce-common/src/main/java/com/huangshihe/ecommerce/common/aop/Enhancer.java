package com.huangshihe.ecommerce.common.aop;

import com.huangshihe.ecommerce.common.kits.AopKit;
import com.huangshihe.ecommerce.common.kits.ArrayKit;

import java.lang.reflect.Method;

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
     * 拦截器管理类.
     */
    private static final InterceptorManager _interMan = InterceptorManager.getInstance();

    // 这里的net.sf.cglib.proxy.Enhancer.create(targetClass) 支持传入null，结果为：
    //    net.sf.cglib.empty.Object$$EnhancerByCGLIB$$4eb773e8@6bf2d08e
    // 但是这里暂时不需要类为null创建的增强对象

    // 注意这里对类或对象进行增强，都不允许是final类型的，因为无法创建它的子类，且不能是单例，因为构造方法要求是可见的

    /**
     * 对目标类注入拦截器.
     *
     * @param targetClass  目标类
     * @param injectInters 需注入的拦截器
     * @param <T>          类类型
     * @return 对目标类注入拦截器生成的目标对象，该对象其实是目标类的子类的实例.
     */
    public static <T> T enhance(Class<T> targetClass, Interceptor... injectInters) {
        if (targetClass == null) {
            return null;
        }
        _interMan.createMethodInterceptor(targetClass, injectInters);
        return (T) net.sf.cglib.proxy.Enhancer.create(targetClass, new Callback());
    }

    /**
     * 对目标类注入拦截器.
     *
     * @param targetClass  目标类
     * @param method       指定拦截的方法
     * @param injectInters 需注入的拦截器
     * @param <T>          类类型
     * @return 对目标类注入拦截器生成的目标对象，该对象其实是目标类的子类的实例.
     */
    public static <T> T enhance(Class<T> targetClass, Method method, Interceptor... injectInters) {
        if (targetClass == null) {
            return null;
        }
        _interMan.createMethodInterceptor(method, injectInters);
        return (T) net.sf.cglib.proxy.Enhancer.create(targetClass, new Callback());
    }

    /**
     * 对目标类注入拦截器.
     *
     * @param targetClass      目标类
     * @param injectInterClass 需注入的拦截器类
     * @param <T>              目标类类型
     * @return 对目标类注入拦截器生成的目标对象，该对象其实是目标类的子类的实例.
     */
    public static <T> T enhance(Class<T> targetClass, Class<? extends Interceptor> injectInterClass) {
        if (targetClass == null) {
            return null;
        }
        _interMan.createMethodInterceptor(targetClass, injectInterClass);
        // 在Callback中动态查询方法的拦截器
        return (T) net.sf.cglib.proxy.Enhancer.create(targetClass, new Callback());
    }

    /**
     * 对目标类注入拦截器.
     *
     * @param targetClass      目标类
     * @param method           指定拦截的方法
     * @param injectInterClass 需注入的拦截器来
     * @param <T>              目标类类型
     * @return 对目标类注入拦截器生成的目标对象，该对象其实是目标类的子类的实例.
     */
    public static <T> T enhance(Class<T> targetClass, Method method, Class<? extends Interceptor> injectInterClass) {
        if (targetClass == null) {
            return null;
        }
        _interMan.createMethodInterceptor(method, injectInterClass);
        return (T) net.sf.cglib.proxy.Enhancer.create(targetClass, new Callback());
    }

    /**
     * 对已有对象的特定方法注入拦截器对象，若该对象已是增强对象，则增加拦截器对象，而不创建新的增强对象，直接返回原有增强对象.
     *
     * @param target       已有对象
     * @param method       方法
     * @param injectInters 拦截器对象
     * @param <T>          类类型
     * @return 增强对象
     */
    public static <T> T enhance(Object target, Method method, Interceptor... injectInters) {
        if (target == null || ArrayKit.isEmpty(injectInters)) {
            return null;
        }
        // 创建方法与拦截器对象的关联
        _interMan.createMethodInterceptor(method, injectInters);
        // 如果该对象已经是增强对象，则直接返回即可。
        if (AopKit.isEnhanced(target)) {
            return (T) target;
        } else {
            return (T) net.sf.cglib.proxy.Enhancer.create(target.getClass(), new Callback());
        }
    }

    /**
     * 对已有对象的特定方法注入拦截器对象，若该对象已是增强对象，则增加拦截器对象，而不创建新的增强对象，直接返回原有增强对象.
     *
     * @param target           已有对象
     * @param method           方法
     * @param injectInterClass 拦截器类
     * @param <T>              类类型
     * @return 增强对象
     */
    public static <T> T enhance(Object target, Method method, Class<? extends Interceptor> injectInterClass) {
        if (target == null || injectInterClass == null) {
            return null;
        }
        // 创建方法与拦截器对象的关联
        _interMan.createMethodInterceptor(method, injectInterClass);
        // 如果该对象已经是增强对象，则直接返回即可。
        if (AopKit.isEnhanced(target)) {
            return (T) target;
        } else {
            return (T) net.sf.cglib.proxy.Enhancer.create(target.getClass(), new Callback());
        }
    }


    /**
     * 对已有对象的特定方法注入拦截器对象，若该对象已是增强对象，则增加拦截器对象，而不创建新的增强对象，直接返回原有增强对象.
     *
     * @param target           已有对象
     * @param injectInterClass 拦截器类
     * @param <T>              类类型
     * @return 增强对象
     */
    public static <T> T enhance(Object target, Class<? extends Interceptor> injectInterClass) {
        if (target == null || injectInterClass == null) {
            return null;
        }
        // 创建方法与拦截器对象的关联
        _interMan.createMethodInterceptor(target.getClass(), injectInterClass);
        // 如果该对象已经是增强对象，则直接返回即可。
        if (AopKit.isEnhanced(target)) {
            return (T) target;
        } else {
            return (T) net.sf.cglib.proxy.Enhancer.create(target.getClass(), new Callback());
        }
    }
}

