package com.huangshihe.ecommerce.pub.Main;

import com.huangshihe.ecommerce.pub.config.ConfigManager;

/**
 * main
 * <p>
 * Create Date: 2018-04-05 12:37
 *
 * @author huangshihe
 */
public class Main {
    public static void main(String[] args) {
        // 启动
        // 部署任务
        // TODO 部署任务也将归至public组件中？部署任务需要解压配置文件
        // 部署任务任务之一为：将所有的config包下的配置都复制到当前jar包下的resources目录下
        // 这里需读取所有的配置文件，都在当前jar包下的resources目录下


        // 加载配置
        ConfigManager.getInstance().init();

        // TODO
        // 启动以下服务的守护线程
        // Kafka : 检查生产者状态，初始化消费者；当服务状态没了？则需要自愈能力！
        // Hadoop : 检查集群环境，当服务状态没了？需要自愈能力
        // HBase : 检查HBase状态，备份数据？
        // Spark : Spark的任务进度检测？Spark的任务如何记录？客户端到服务端如何保证信息完整，可查询。
        //          完整的信息保存到mysql中？或高速缓存中？将id存放到zookeeper中
        // Zookeeper :

        // keep running...
        while (true) {
        }
    }
}
