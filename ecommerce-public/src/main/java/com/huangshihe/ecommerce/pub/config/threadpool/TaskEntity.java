package com.huangshihe.ecommerce.pub.config.threadpool;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * 线程任务实体类.
 * <p>
 * Create Date: 2018-01-23 21:42
 *
 * @author huangshihe
 */
public class TaskEntity {

    private String className;

    private String method;

    @XmlAttribute(name = "class", required = true)
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @XmlAttribute(name = "method", required = true)
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "className='" + className + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
