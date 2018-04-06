package com.huangshihe.ecommerce.pub.config;

import com.huangshihe.ecommerce.pub.config.threadpool.ServiceConfigEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 配置根元素类.
 * <p>
 * Create Date: 2018-03-03 21:10
 *
 * @author huangshihe
 */
@XmlRootElement(name = "config")
public class ConfigEntity {

    private List<ServiceConfigEntity> serviceConfigEntities;

    @XmlElementWrapper(name = "serviceConfigs")
    @XmlElement(name = "serviceConfig")
    public List<ServiceConfigEntity> getServiceConfigEntities() {
        return serviceConfigEntities;
    }

    public void setServiceConfigEntities(List<ServiceConfigEntity> serviceConfigEntities) {
        this.serviceConfigEntities = serviceConfigEntities;
    }

    @Override
    public String toString() {
        return "ConfigEntity{" +
                "serviceConfigEntities=" + serviceConfigEntities +
                '}';
    }
}
