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
        // TODO setArg当前不可用！！！原jfinal方式中也不可用
        inv.setArg(0, new Object[]{"tom"});
//        inv.getTarget().getClass()
        inv.invoke();
    }
}
