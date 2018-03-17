package com.huangshihe.ecommerce.pub.factory;

import com.huangshihe.ecommerce.pub.config.threadpool.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务工厂类.
 * 服务使用工厂方式获取实例，首先到manager中找，如果没有的话，再创建实例，不要直接显式的创建实例。
 * <p>
 * Create Date: 2018-03-17 23:39
 *
 * @author huangshihe
 */
@SuppressWarnings("unchecked")
public class ServicesFactory {

    private static Logger LOGGER = LoggerFactory.getLogger(ServicesFactory.class);

    private static ServicesFactory instance = new ServicesFactory();

    private ServicesFactory() {

    }

    public static ServicesFactory getInstance() {
        return instance;
    }

    /**
     * 获取Service对象.
     *
     * @param cls Service类
     * @param <T> 类类型
     * @return Service对象
     */
    public <T> T getServiceObject(Class<T> cls) {
        T object = (T) ThreadPoolManager.getInstance().getEnhancedObject(cls);
        if (null == object) {
            try {
                object = cls.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("new instance error, class:{}, detail:{}", cls, e);
                throw new IllegalArgumentException(e);
            }
        }
        // TODO 根据需要增加if-else
        return object;
    }
}
