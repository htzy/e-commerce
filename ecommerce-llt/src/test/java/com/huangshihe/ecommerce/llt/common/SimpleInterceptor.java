package com.huangshihe.ecommerce.llt.common;

import com.huangshihe.ecommerce.common.aop.Interceptor;
import com.huangshihe.ecommerce.common.aop.Invocation;

/**
 * simple interceptor
 * <p>
 * Create Date: 2018-03-15 23:24
 *
 * @author huangshihe
 */
public class SimpleInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv) {
        if ("setName".equals(inv.getMethod().getName())) {
            inv.setArg(0, "tom");
        }
//        inv.getTarget().getClass()
        inv.invoke();
    }
}
