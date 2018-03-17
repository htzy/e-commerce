package com.huangshihe.ecommerce.common.kits;

import com.huangshihe.ecommerce.common.aop.Interceptor;

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

    public static final String ENHANCED_TAG = "$$EnhancerByCGLIB";

    /**
     * 被增强对象为原类的子类对象
     *
     * @param target 被增强对象
     * @return 原被增强对象类，而非修改后的子类
     */
    public static Class<?> getTargetClass(Object target) {
        Class<?> targetClass = target.getClass();
        if (targetClass.getName().contains(ENHANCED_TAG)) {
            targetClass = targetClass.getSuperclass();
        }
        return targetClass;
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
