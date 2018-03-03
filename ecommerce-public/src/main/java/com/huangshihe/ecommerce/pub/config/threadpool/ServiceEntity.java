package com.huangshihe.ecommerce.pub.config.threadpool;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * 线程服务实体类.
 * <p>
 * Create Date: 2018-01-23 21:48
 *
 * @author huangshihe
 */
//@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceEntity implements Serializable {

    /**
     * 命名空间.
     */
    private String nameSpace;

    /**
     * 名字.
     */
    private String name;

    @XmlAttribute(name = "namespace", required = true)
    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    @XmlAttribute(name = "name", required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ServiceEntity{" +
                "nameSpace='" + nameSpace + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
