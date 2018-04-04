package com.huangshihe.ecommerce.common.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象工厂
 * <p>
 * Create Date: 2018-04-04 15:52
 *
 * @author huangshihe
 */
@SuppressWarnings("unchecked")
public class Factory {
    private static Logger LOGGER = LoggerFactory.getLogger(Factory.class);


    /**
     * 获取新对象.
     *
     * @param cls 类
     * @param <T> 类类型
     * @return 新对象
     */
    public <T> T createNewObject(Class<T> cls) {
        Object object = null;
        if (cls == null) {
            return null;
        }
        try {
            object = cls.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            LOGGER.error("new instance error, class:{}, detail:{}", cls, e);
            throw new IllegalArgumentException(e);
        }
        return (T) object;
    }
}
