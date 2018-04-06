package com.huangshihe.ecommerce.common.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务工厂类.
 * 服务使用工厂方式获取实例，首先到manager中找，如果没有的话，再创建实例，不要直接显式的创建实例。
 * 这里的Service全局只会有一个！并不需要多个！
 * TODO 若在配置文件中多次指定，同一个类，然后不同的方法，放入线程池，那么这种情况下，工厂中是有一个service还是对应多个呢？
 * TODO 解决方法：class对应的service还是只有一个，如果说之后配置多个method，那么将在对应的service中存储多个，追加需要拦截的方法
 * <p>
 * Create Date: 2018-03-17 23:39
 *
 * @author huangshihe
 */
@SuppressWarnings("unchecked")
public class ServicesFactory extends Factory {

    private static Logger LOGGER = LoggerFactory.getLogger(ServicesFactory.class);

    private static ServicesFactory _instance = new ServicesFactory();

    private ServicesFactory() {

    }

    public static ServicesFactory getInstance() {
        return _instance;
    }

    private final Map<Class<?>, Object> _servicesMap = new ConcurrentHashMap<Class<?>, Object>();

    /**
     * 获取Service对象.
     *
     * @param serviceClass Service类
     * @param <T>          类类型
     * @return Service对象
     */
    public <T> T getServiceObjectOrNew(Class<T> serviceClass) {

        T object = getServiceObject(serviceClass);
        // 如果不存在，则新建一个Service，且该新建的Service对象将不会主动存放到map中，若需要存放，则需要手动存放
        // 这样可以保证，在map中没有该对象时，可以满足需要多个新建的service对象的场景。
        if (null == object) {
            object = createNewObject(serviceClass);
        }
        return object;
    }

    /**
     * 获取Service对象.
     *
     * @param serviceClass Service类
     * @param <T>          类类型
     * @return Service对象
     */
    public <T> T getServiceObject(Class<T> serviceClass) {
        return (T) _servicesMap.get(serviceClass);
    }

    public void addServiceObject(Class<?> serviceClass, Object serviceObject) {
        if (serviceClass == null || serviceObject == null) {
            return;
        }
        // 若该类对应的object已存在则更新
        _servicesMap.put(serviceClass, serviceObject);
    }
}
