package com.huangshihe.ecommerce.common.kits;

import com.huangshihe.ecommerce.common.aop.Interceptor;

import java.lang.reflect.Method;

/**
 * AopKit.
 * <p>
 * Create Date: 2018-03-17 17:39
 *
 * @author huangshihe
 */
public class AopKit {

    /**
     * 空拦截器数组.
     */
    public static final Interceptor[] NULL_INTERS = new Interceptor[0];

    /**
     * 被增强的标记.
     */
    public static final String ENHANCED_TAG = "$$EnhancerByCGLIB";

    /**
     * 被增强对象为原类的子类对象
     *
     * @param target 被增强对象
     * @return 原被增强对象类，而非修改后的子类
     */
    public static Class<?> getTargetClass(Object target) {
        if (target == null) {
            return null;
        }
        return getTargetClass(target.getClass());
    }

    /**
     * 获取原类.
     *
     * @param targetClass 被增强类
     * @return 原类
     */
    public static Class<?> getTargetClass(Class<?> targetClass) {
        if (targetClass == null) {
            return null;
        }
        if (targetClass.getName().contains(ENHANCED_TAG)) {
            targetClass = targetClass.getSuperclass();
        }
        return targetClass;
    }

    /**
     * 该对象是否为被增强对象.
     *
     * @param target 对象
     * @return 是否被增强
     */
    public static boolean isEnhanced(Object target) {
        if (target == null) {
            return false;
        }
        if (target.toString().contains(ENHANCED_TAG)) {
            return true;
        } else if (target.getClass().getName().contains(ENHANCED_TAG)) {
            // 如果该对象的toString被改写，则即使是被增强对象也拿不到被增强标签，所以获取该类的类名
            return true;
        }
        return false;
    }

    /**
     * 获取方法标识符：类名+方法名.
     *
     * @param cls    类
     * @param method 方法
     * @return 方法标识符
     */
    public static String getMethodIdentity(Class<?> cls, Method method) {
        return ClassKit.getMethodIdentity(getTargetClass(cls), method);
    }

    /**
     * 获取方法标识符：类名+方法名.
     *
     * @param cls    类
     * @param method 方法
     * @return 方法标识符
     */
    public static String getMethodIdentity(Class<?> cls, String method) {
        return ClassKit.getMethodIdentity(getTargetClass(cls), method);
    }

    /**
     * 检查注入的拦截对象是否为空.
     *
     * @param injectInters 注入的拦截对象
     */
    public static void checkInjectInterceptors(Interceptor... injectInters) {
        if (injectInters == null) {
            throw new IllegalArgumentException("injectInters can not be null.");
        }
        for (Interceptor inter : injectInters) {
            if (inter == null) {
                throw new IllegalArgumentException("interceptor in injectInters can not be null.");
            }
        }
    }

}
