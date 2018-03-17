package com.huangshihe.ecommerce.common.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理类.
 * 这里需要全局代理
 * ref ：http://blog.csdn.net/qq_35246620/article/details/68484407
 * <p>
 * Create Date: 2018-03-05 22:33
 *
 * @author huangshihe
 */
@Deprecated
public class DynamicProxyHandler implements InvocationHandler {

    // 声明被代理对象
    private Object business;


    public Object bind(Object business) {
        this.business = business;
        return Proxy.newProxyInstance(business.getClass().getClassLoader(),
                business.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
