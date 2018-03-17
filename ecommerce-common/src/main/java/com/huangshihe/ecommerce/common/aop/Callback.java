package com.huangshihe.ecommerce.common.aop;

import com.huangshihe.ecommerce.common.kits.AopKit;
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
        AopKit.checkInjectInterceptors(injectInters);
        this.injectInters = injectInters;
    }

    /**
     * 拦截方法.
     *
     * @param target      被增强对象，即原类的子类对象
     * @param method      当前拦截的方法
     * @param args        当前拦截方法的参数
     * @param methodProxy 方法代理
     * @return 被拦截后的方法的执行结果，若原方法为void，则这里返回null
     * @throws Throwable 异常
     */
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
        Invocation invocation = new Invocation(target, method, args, methodProxy, injectInters);
        invocation.invoke();
        // 若需要返回自定义return value，需要自定义：MethodInterceptor方法，示例：com.jfinal.aop.Callback
        // 被拦截后的方法的执行结果，若原方法为void，则这里返回null
        return invocation.getReturnValue();
    }

    /**
     * 生成Class自带的所有作用域修饰符修饰的方法（即：private、protected、default、public；不含继承的）
     *
     * @return Class自带的所有作用域修饰符修饰的方法集合
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
