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
