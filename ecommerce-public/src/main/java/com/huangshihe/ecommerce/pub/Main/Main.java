package com.huangshihe.ecommerce.pub.Main;

import com.huangshihe.ecommerce.pub.config.ConfigManager;
import com.huangshihe.ecommerce.pub.config.threadpool.ThreadPoolManager;
import com.huangshihe.ecommerce.pub.deploy.DeployManager;

/**
 * main
 * <p>
 * Create Date: 2018-04-05 12:37
 *
 * @author huangshihe
 */
public class Main {

    /**
     * 初始化.
     */
    public static void init() {
        // 启动
        // 部署任务
        // 部署任务前置条件：执行script中的deploy.sh脚本：将jar包拷贝到lib目录下，将配置jar包拷贝的configs/jar下
        // 部署任务：将configs/jar下的jar包解压，抽取出配置文件到data/configs目录下
        DeployManager.getInstance().init();
        // TODO 部署任务包括：上传需要运行在大数据集群中的jar包，如运行在HBase中的分页jar

        // 加载配置
        ConfigManager.getInstance().init();

        // 启动线程池
        ThreadPoolManager.getInstance().init();

        // TODO 启动以下服务的守护线程
        // Kafka : 检查生产者状态，初始化消费者；当服务状态没了？则需要自愈能力！
        // Hadoop : 检查集群环境，当服务状态没了？需要自愈能力
        // HBase : 检查HBase状态，备份数据？
        // Spark : Spark的任务进度检测？Spark的任务如何记录？客户端到服务端如何保证信息完整，可查询。
        //          完整的信息保存到mysql中？或高速缓存中？将id存放到zookeeper中
        // Zookeeper :

    }

    public static void main(String[] args) {
        // 初始化
        init();
        // keep running...
        while (true) {
        }
    }
}
