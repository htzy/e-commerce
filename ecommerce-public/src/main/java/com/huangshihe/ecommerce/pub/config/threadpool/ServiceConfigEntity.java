package com.huangshihe.ecommerce.pub.config.threadpool;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * 服务配置类.
 * <p>
 * Create Date: 2018-03-03 20:59
 *
 * @author huangshihe
 */
public class ServiceConfigEntity {

    private ServiceEntity serviceEntity;

    private ThreadPoolEntity threadPoolEntity;

    private List<TaskEntity> taskEntities;

    private ScheduledThreadPoolEntity scheduledThreadPoolEntity;

    @XmlElement(name = "service", required = true)
    public ServiceEntity getServiceEntity() {
        return serviceEntity;
    }

    public void setServiceEntity(ServiceEntity serviceEntity) {
        this.serviceEntity = serviceEntity;
    }

    @XmlElement(name = "threadPool")
    public ThreadPoolEntity getThreadPoolEntity() {
        return threadPoolEntity;
    }

    public void setThreadPoolEntity(ThreadPoolEntity threadPoolEntity) {
        this.threadPoolEntity = threadPoolEntity;
    }

    @XmlElement(name = "scheduledThreadPool")
    public ScheduledThreadPoolEntity getScheduledThreadPoolEntity() {
        return scheduledThreadPoolEntity;
    }

    public void setScheduledThreadPoolEntity(ScheduledThreadPoolEntity scheduledThreadPoolEntity) {
        this.scheduledThreadPoolEntity = scheduledThreadPoolEntity;
    }

    @XmlElementWrapper(name = "tasks")
    @XmlElement(name = "task")
    public List<TaskEntity> getTaskEntities() {
        return taskEntities;
    }

    public void setTaskEntities(List<TaskEntity> taskEntities) {
        this.taskEntities = taskEntities;
    }

    public String getIdentity() {
        return serviceEntity.getNameSpace() + "-" + serviceEntity.getName();
    }

    @Override
    public String toString() {
        return "ServiceConfigEntity{" +
                "serviceEntity=" + serviceEntity +
                ", threadPoolEntity=" + threadPoolEntity +
                ", taskEntities=" + taskEntities +
                ", scheduledThreadPoolEntity=" + scheduledThreadPoolEntity +
                '}';
    }
}
