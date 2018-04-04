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

    private static final Set<String> excludedMethodName = buildExcludedMethodName();

    private static final InterceptorManager _interMan = InterceptorManager.getInstance();

    public Callback() {
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
            return methodProxy.invokeSuper(target, args);
//            if (method.getName().equals("finalize")) {
//                return methodProxy.invokeSuper(target, args);
//            }
        }
        // 拦截方法与拦截器对象对应管理存储在interceptorManager中，具体拦截器对象需要实时查询
        Invocation invocation = new Invocation(target, method, args, methodProxy, _interMan.query(method));
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
