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
  
# 可以在web中查看StoreFile的情况
# 在Put之后，hbase会先写到MemStore中，这时不会写到StoreFile中，可以执行如下命令生成：
  flush 'tableName'
# 执行后可以在web中看到生成了一个新的StoreFile。
# 删除数据后，若被删除的数据处于StoreFile中，那么StoreFile中的对应部分也不会立马被删除，可以执行如下命令，立即删除：
  campact 'tableName'
# TODO 删除表后，是否需要刷新？如何刷新？

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
```java
// 若需要手动关闭table可在finally中使用：
import org.apache.hadoop.io.IOUtils;
IOUtils.closeStream(table);
// 也可以利用try-with-resources，自动关闭try(open table){}catch(){}
```


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


# 基础知识
## Client API
> 所有修改数据的操作都保证了行级别的原子性，这会影响到这一行数据所有的并发读写操作。换句话说，其他客户端或线程对同一行的读写操作都不会影响到该行数据的原子性：要么读到最新的修改，要么等待系统允许写入该行修改。
  通常，在正常负载和常规操作下，客户端读操作不会受到其他修改数据的客户端影响，因为它们之间的冲突可以忽略不计。但是，当许多客户端需要同时修改同一行数据时就会产生问题。所以，用户应当尽量使用批量处理（batch）更新来减少单独操作同一行数据的次数。
  写操作中涉及的列的数目不会影响该行数据的原子性，行原子性会同时保护到所有列。
  
  ### 单行put
  > 创建Put实例时用户需要提供一个行健row，在HBase中每行数据都有唯一的行键（row key）作为标识。
  在创建Put实例后，就可以向该实例添加数据了，即用add()方法，每次调用add() 都可以特定地添加一列数据，如果再加一个时间戳选项，就能形成一个数据单元格。注意，当不指定时间戳调用add()方法时，Put实例会使用来自构造函数的可选时间戳参数（ts），如果在构造Put实例时也没有指定时间戳，则时间戳将由region服务器设定。
  
  #### 客户端的写缓冲区
> 每一个put操作实际上都是一个RPC操作，它将客户端数据传送到服务器然后返回。这只适合小数据量的操作，如果有个应用程序需要每秒存储上千行数据到HBase表中，这样的处理就不合适了！
减少独立RPC调用的关键是限制往返时间（round-trip time），往返时间就是客户端发送一个请求到服务器，然后服务器通过网络进行响应的时间。这个时间不包含数据实际传输的时间，它其实就是通过线路传送网络包的开销。一般情况下，在LAN网络中大约要花1毫秒的时间，这意味着在1秒钟的时间内只能完成1000次RPC往返响应。
另一个重要的因素就是消息大小。如果通过网络发送的请求内容较大，那么需要请求返回的次数相应较少，这是因为时间主要花费在数据传递上。不过如果传送的数据量很小，比如一个计数器递增操作，那么用户把多次修改的数据批量提交给服务器并减少请求次数，性能会有相应提升。

HBase的API配备了一个客户端的写缓冲区（write buffer），缓冲区负责收集put操作，然后调用RPC操作一次性将put送往服务器。
```java
// 默认情况下，缓冲区是禁用的，将autoflush设置为false来激活缓冲区。
void setAutoFlush(boolean autoFlush)
boolean isAutoFlush()
// 当需要强制把数据写到服务端时，可以调用flushCommits()方法（用户调用则为显式刷写）
void flushCommits()

// 用户可以强制刷写缓冲区，不过这通常不必要。当缓冲的数据量超过缓冲指定的大小限制，客户端就会隐式地调用刷写命令。
// 可以通过如下方法设置客户端写缓冲区的大小（默认的大小是2MB）：
long getWriteBufferSize()
void setWriteBufferSize(long writeBufferSize)
// 也可以在hbase-site.xml中配置"hbase.client.write.buffer"的大小，单位为字节，默认值即2097152（字节，即2MB），用这种方式可以避免在每一个Table实例中设定缓冲区的麻烦。
```

- 显式刷写
    用户调用flushCommits()方法，把数据发送到服务器做永久存储。
- 隐式刷写
    隐式刷写会在用户调用put()或setWriteBufferSize方法时触发。将目前占用的缓冲区大小与用户配置的大小做比较，如果超出限制，则会调用flushCommits()方法。如果缓冲区被禁用，即设置setAutoFlush(true)，这样用户每次调用put()方法时都会触发刷写。在调用Table类的close()方法时也会无条件地隐式触发刷写。
  
  对于往返时间，如果用户只存储大单元格，客户端缓冲区的作用就不大了，因为传输时间占用了大部分的请求时间。在这种情况下，建议最好不要增加客户端缓冲区大小。
  

## rowKey
rowkey可以直接通过拼接来完成，不需要连接符，所有的column长度定义为常量。


## 规范用名：
1. column 由 column family 和 column qualifier组成，column一般表示列族或family+qualifier。
2. 列族（一般用cf、column、family等表示）：  
String: familyName  
byte[]: family  
3. qualifier限定符
byte[]: qualifier


## 获取Cell中的值
Cell为接口，针对这个接口有多种实现，而在多种实现中getFamilyArray()和getQualifierArray()方法都是返回完整的字节数组，而不是单独的family或qualifier
```java
// 以下方式获取有问题！
LOGGER.debug("cell-family: {}, cell-qualifier: {}, cell-values: {}",
                        Bytes.toString(cell.getFamilyArray()), 
                        Bytes.toString(cell.getQualifierArray()),
                        Bytes.toString(cell.getValueArray()));
// 应该使用下面的方式
LOGGER.debug("cell-family: {}, cell-qualifier: {}, cell-values: {}",
                        Bytes.toString(CellUtil.cloneFamily(cell)),
                        Bytes.toString(CellUtil.cloneQualifier(cell)),
                        Bytes.toString(CellUtil.cloneValue(cell)));
```

## 启动zk相关
start-hbase.sh
```shell
# 其中$distMode 应该为：hbase-site.xml 中的hbase.cluster.distributed配置，默认为false，也就是默认为单机部署。
if [ "$distMode" == 'false' ]
then
  # 当单机部署时，运行hbase-daemon.sh，输入参数为：master。
  # TODO 那么单机情况下，zk是如何自动启动的？若先启动zk，还会报zk已启动？只能直接启动hbase-start,让它自动启动zk
  "$bin"/hbase-daemon.sh --config "${HBASE_CONF_DIR}" $commandToRun master $@
else
  # 这里很奇怪，如果部署为分布式的，一般来说是先手动启动zk，然后再启动hbase，而这里却显示hbase在
  # 分布式情况下，hbase在启动master之前自动启动了一个Zookeeper的server？
  "$bin"/hbase-daemons.sh --config "${HBASE_CONF_DIR}" $commandToRun zookeeper
  "$bin"/hbase-daemon.sh --config "${HBASE_CONF_DIR}" $commandToRun master
  "$bin"/hbase-daemons.sh --config "${HBASE_CONF_DIR}" \
    --hosts "${HBASE_REGIONSERVERS}" $commandToRun regionserver
  "$bin"/hbase-daemons.sh --config "${HBASE_CONF_DIR}" \
    --hosts "${HBASE_BACKUP_MASTERS}" $commandToRun master-backup
fi
```

hbase-deamon.sh
```shell
cleanAfterRun() {
  if [ -f ${HBASE_PID} ]; then
    # If the process is still running time to tear it down.
    kill -9 `cat ${HBASE_PID}` > /dev/null 2>&1
    rm -f ${HBASE_PID} > /dev/null 2>&1
  fi

  if [ -f ${HBASE_ZNODE_FILE} ]; then
    if [ "$command" = "master" ]; then
      # 输入参数并执行hbase master clear
      HBASE_OPTS="$HBASE_OPTS $HBASE_MASTER_OPTS" $bin/hbase master clear > /dev/null 2>&1
    else
      #call ZK to delete the node
      ZNODE=`cat ${HBASE_ZNODE_FILE}`
      HBASE_OPTS="$HBASE_OPTS $HBASE_REGIONSERVER_OPTS" $bin/hbase zkcli delete ${ZNODE} > /dev/null 2>&1
    fi
    rm ${HBASE_ZNODE_FILE}
  fi
}

```

hbase
```shell
  echo "Some commands take arguments. Pass no args or -h for usage."
  echo "  shell           Run the HBase shell"
  echo "  hbck            Run the hbase 'fsck' tool"
  echo "  snapshot        Create a new snapshot of a table"
  echo "  snapshotinfo    Tool for dumping snapshot information"
  echo "  wal             Write-ahead-log analyzer"
  echo "  hfile           Store file analyzer"
  echo "  zkcli           Run the ZooKeeper shell"
  echo "  upgrade         Upgrade hbase"
  echo "  master          Run an HBase HMaster node"
  echo "  regionserver    Run an HBase HRegionServer node"
  echo "  zookeeper       Run a Zookeeper server"
  echo "  rest            Run an HBase REST server"
  echo "  thrift          Run the HBase Thrift server"
  echo "  thrift2         Run the HBase Thrift2 server"
  echo "  clean           Run the HBase clean up script"
  echo "  classpath       Dump hbase CLASSPATH"
  echo "  mapredcp        Dump CLASSPATH entries required by mapreduce"
  echo "  pe              Run PerformanceEvaluation"
  echo "  ltt             Run LoadTestTool"
  echo "  version         Print the version"
  echo "  CLASSNAME       Run the class named CLASSNAME"
  exit 1



elif [ "$COMMAND" = "master" ] ; then
  CLASS='org.apache.hadoop.hbase.master.HMaster'
  if [ "$1" != "stop" ] && [ "$1" != "clear" ] ; then
    HBASE_OPTS="$HBASE_OPTS $HBASE_MASTER_OPTS"
  fi
  
  
```











# 参考
[Mac下安装HBase及详解](http://www.jianshu.com/p/510e1d599123)  
[Hbase禁用自带ZooKeeper，使用已经安装的ZooKeeper](http://www.aboutyun.com/thread-7451-1-1.html)  
[hbase in action学习笔记一(quick start)](http://san-yun.iteye.com/blog/1991107)  
[HBase入门实例: Table中Family和Qualifier的关系与区别](http://blog.csdn.net/wkwanglei/article/details/43988109)  
[HBase scan setBatch和setCaching的区别【转】](https://www.cnblogs.com/seaspring/p/6861957.html)  
