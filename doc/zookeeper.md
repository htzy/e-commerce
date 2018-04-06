# 环境
macOS high_sierra


# 安装
```shell
brew install zookeeper
# 下载版本：zookeeper-3.4.10.high_sierra.bottle.1.tar.gz

# 安装目录：/usr/local/Cellar/zookeeper/3.4.10

# 默认配置文件目录：/usr/local/etc/zookeeper/

# 日志文件目录：/usr/local/var/log/zookeeper (配置在log4j.properties中)
```

## 配置
    - 2888端口号：zookeeper服务之间通信的端口
    - 3888端口号：zookeeper与其他程序通信的端口，选举Leader使用
    - tickTime：配置一次心跳时间间隔时间，单位为毫秒，默认为2000毫秒。
    - initLimit：配置zookeeper服务器集群中连接到Leader的Follower服务器，初始化连接时最长能忍受多少个心跳时间间隔数，默认为10次，当超过tickTime*initLimit，默认为20秒时，Follower服务器将连接失败。
    - syncLimit：Leader与Follower之间发送消息，请求和应答时间最长长度，默认为2次，即总时间长度不得超过tickTime*syncLimit，默认为4秒。
    - server.A=B:C:D：其中A是数字，代表这是第几号服务器；
        B是这个服务器的地址（IP或配置在/etc/hosts文件中的主机名）；
        C：该服务器与其他zk服务器交换信息的端口，Leader监听该端口；
        D：若zk集群中的Leader挂了，需要重新选举，则通过该端口进行重新选举。
    
    

# 常用命令
```shell
# To have launchd start zookeeper now and restart at login:
  brew services start zookeeper
# Or, if you don't want/need a background service you can just run:
  zkServer start  # (可在任意目录下执行)

# 查看zkServer信息
  zkServer

# 查看zkServer启动状态
  zkServer status
  
# 进入
  zkCli    # （可在任意目录下执行）
  
# 查看进程
  jps
  # 其中QuorumPeerMain是zookeeper进程
```

# 应用与原理



# 参考
[Mac OSX安装启动 zookeeper](https://www.cnblogs.com/phpdragon/p/5637943.html)  
[zookeeper的应用和原理](http://blog.csdn.net/gs80140/article/details/51496925)  

