package com.huangshihe.ecommerce.common.aop;

/**
 * 拦截器接口.
 * <p>
 * Create Date: 2018-03-15 22:29
 *
 * @author huangshihe
 */
public interface Interceptor {
    void intercept(Invocation inv);
}
