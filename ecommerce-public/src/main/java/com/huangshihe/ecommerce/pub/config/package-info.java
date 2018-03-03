/**
 * 线程池的配置文件如下：
 * <config>
 * <serviceConfigs>
 *     <serviceConfig>
 *         <service namespace="ecommerce" name="simpletask" description="simple task in ecommerce"></service>
 *         <threadPool poolSize="1" maxPoolSize="1" queueSize="20" keepAliveTime="200"></threadPool>
 *         <tasks>
 *             <task class="com.huangshihe.ecommerce.pub.config.threadpool.Simple" method="onDemoTask"></task>
 *         </tasks>
 *     </serviceConfig>
 * </serviceConfigs>
 * </config>
 *
 * config为：配置文件根元素，所有的配置文件均以该元素作为根元素
 * serviceConfigs为：配置文件的RootElement，服务配置
 * serviceConfig为：具体某一项服务配置
 * service为：该服务的一些描述信息，如服务命名空间，服务名以及描述（其中描述只用于xml中，代码中不会使用）
 * TODO 其中服务命名空间+服务名作为该服务下线程池下的所有线程的名字
 * threadPool为：该服务对应的线程池的描述信息
 *（TODO 其中threadPool还可以为：scheduledThreadPool——定时？singleThreadExecutor单线程执行？）
 * tasks为：线程池中运行的任务列表
 * task为：线程池中具体运行的某一项任务的描述信息，指定类名和方法名（即将该方法作为task扔到池子中运行）
 *
 * <p>
 * Create Date: 2018-03-03 19:55
 *
 * @author huangshihe
 */
package com.huangshihe.ecommerce.pub.config;
