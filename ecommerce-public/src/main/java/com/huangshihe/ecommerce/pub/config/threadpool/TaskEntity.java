package com.huangshihe.ecommerce.pub.config.threadpool;

import com.huangshihe.ecommerce.common.kits.StringKit;

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

    private String methodName;

    @XmlAttribute(name = "class", required = true)
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @XmlAttribute(name = "method", required = true)
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getTaskName() {
        if (StringKit.isContainEmpty(className, methodName)) {
            throw new IllegalArgumentException("error! className or methodName contain empty");
        }
        return className + methodName;
    }
}
