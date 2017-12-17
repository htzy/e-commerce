# 环境
macOS high_sierra
# 安装
```shell
brew install hbase
# 下载版本：hbase-1.2.6.high_sierra.bottle.tar.gz
# 安装目录：/usr/local/Cellar/hbase/1.2.6
```

# 常用命令
```shell
# To have launchd start hbase now and restart at login:
  brew services start hbase
# Or, if you don't want/need a background service you can just run:
  /usr/local/opt/hbase/bin/start-hbase.sh
  
# 进入shell
  hbase shell
```

# HBase连接池
例子
```java
Configuration conf = HBaseConfiguration.create();
Connection connnect = ConnectionFactory.createConnection(conf);
Table table = connection.getTable(TableName.valueOf(tablename));
// use table as needed, the table returned is lightweight

```
HTablePool is Deprecated! HConnection, which is deprecated in HBase 1.0 by Connection. Please use Connection instead.
HTableInterface is Deprecated! use Table instead.


# 禁用自带zookeeper
修改conf/hbase-env.sh
```shell
export HBASE_MANAGES_ZK=false
```
直接通过通过/bin/start-hbase.sh启动  
不要先启动zk,可能导致端口占用报错:
Could not start ZK at requested port of 2181.  
ZK was started at port: 2182.  
Aborting as clients (e.g. shell) will not be able to find this ZK quorum

# web
http://localhost:16010/

# 参考
[Mac下安装HBase及详解](http://www.jianshu.com/p/510e1d599123)  
[Hbase禁用自带ZooKeeper，使用已经安装的ZooKeeper](http://www.aboutyun.com/thread-7451-1-1.html)  
[hbase in action学习笔记一(quick start)](http://san-yun.iteye.com/blog/1991107)  
