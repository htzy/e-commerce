package com.huangshihe.ecommerce.common.aop;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * aop callback.
 * <p>
 * Create Date: 2018-03-15 22:52
 *
 * @author huangshihe
 */
public class Callback implements MethodInterceptor {
    private Object injectTarget = null;
    private final Interceptor[] injectInters;

    private static final Set<String> excludedMethodName = buildExcludedMethodName();

    public Callback(Interceptor... injectInters) {
        checkInjectInterceptors(injectInters);
        this.injectInters = injectInters;
    }

    private void checkInjectInterceptors(Interceptor... injectInters) {
        if (injectInters == null) {
            throw new IllegalArgumentException("injectInters can not be null.");
        }
        for (Interceptor inter : injectInters) {
            if (inter == null) {
                throw new IllegalArgumentException("interceptor in injectInters can not be null.");
            }
        }
    }

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        // 若拦截的是Class自带的方法，则需要按下方法特殊处理
        if (excludedMethodName.contains(method.getName())) {
            if (this.injectTarget == null || method.getName().equals("finalize")) {
                return methodProxy.invokeSuper(target, args);
            } else {
                return methodProxy.invoke(this.injectTarget, args);
            }
        }
//        // TODO 被增强对象为原类的子类对象
//        Class<?> targetClass = target.getClass();
//        if (targetClass.getName().indexOf("$$EnhancerByCGLIB") != -1) {
//            targetClass = targetClass.getSuperclass();
//        }
        Invocation invocation = new Invocation(target, method, args, methodProxy, injectInters);
        invocation.invoke();
        return invocation.getReturnValue();

    }

    /**
     * 生成Class自带的所有作用域修饰符修饰的方法（即：private、protected、default、public；不含继承的）
     *
     * @return
     */
    private static final Set<String> buildExcludedMethodName() {
        Set<String> excludedMethodName = new HashSet<String>();
        Method[] methods = Object.class.getDeclaredMethods();
        for (Method m : methods) {
            excludedMethodName.add(m.getName());
        }
        // getClass() registerNatives() can not be enhanced
        // excludedMethodName.remove("getClass");
        // excludedMethodName.remove("registerNatives");
        return excludedMethodName;
    }
}
