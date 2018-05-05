# mac上安装hadoop
0. 配置本地ssh，详见参考[如何在MacOSX上安装Hadoop(how to install hadoop on mac os x)]。
1. 下载hadoop
    由于spark、hbase和hadoop的版本需要配合使用，因此，需要指定版本下载，这里下载2.7的最新版：2.7.3    
    当前brew最新版是2.8+，因此下载homebrew-core的git指定版本号。  
    
```shell
# 下载homebrew-core到本地：
git clone https://github.com/Homebrew/homebrew-core.git
# 找到对应的版本：
git log master -- Formula/hadoop.rb
# 找到2.7最新版，并检出：
git checkout f9ec4b3
# 下载2.7.3版本的hadoop
brew install ./Formula/hadoop.rb
```
2. 配置hadoop
配置文件目录：/usr/local/opt/hadoop/libexec/etc/hadoop
core-site.xml
```xml
<configuration>
  <property>
     <name>hadoop.tmp.dir</name>
     <value>/usr/local/var/hadoop/hdfs/tmp</value>
     <description>A base for other temporary directories.</description>
  </property>
  <property>
     <name>fs.default.name</name>
     <value>hdfs://localhost:9000</value>
  </property>
</configuration>
```

hdfs-site.xml
```xml
<configuration>
  <property>
      <name>dfs.replication</name>
      <value>1</value>
  </property>
  <property>
    <name>dfs.datanode.data.dir</name>
    <value>file:///usr/local/var/hadoop/datanode</value>
  </property>
</configuration>
```

mapred-site.xml
```xml
<configuration>
   <property>
      <name>mapred.job.tracker</name>
      <value>localhost:9010</value>
   </property>
</configuration>
```

3. 配置hbase
hbase-site.xml 
```xml
<configuration>
  <property>
    <name>hbase.rootdir</name>
    <!--尤其注意这里端口号跟上面hadoop的配置一样-->
    <value>hdfs://localhost:9000/hbase</value>
  </property>
  <property>
    <name>hbase.zookeeper.property.clientPort</name>
    <value>2181</value>
  </property>
  <property>
    <name>hbase.zookeeper.property.dataDir</name>
    <value>/usr/local/var/zookeeper</value>
  </property>
  <property>
    <name>hbase.zookeeper.dns.interface</name>
    <value>lo0</value>
  </property>
  <property>
    <name>hbase.regionserver.dns.interface</name>
    <value>lo0</value>
  </property>
  <property>
    <name>hbase.master.dns.interface</name>
    <value>lo0</value>
  </property>
  <property>
     <name>dfs.replication</name>
     <value>1</value>
  </property>
  <property>
     <name>hbase.master.info.port</name>
     <value>60010</value>
  </property>
</configuration>
```

hbase-env.sh
```shell
# 之后将自定义的filter等类上传到该处，否则class not found
export HBASE_CLASSPATH="/usr/local/opt/hbase/libexec/lib"
```


4. 配置spark
TODO

# 命令
```shell
# 查看后台进程
jps

# hadoop namenode格式化，格式化之前，删除原有数据（具体可参考：《重新格式化HDFS的方法》）：
# 1.1 清除原有数据
cd /usr/local/var/hadoop
rm -rf *

# datanode
# cd /usr/local/var/hadoop/datanode
# namenode
# cd /usr/local/var/hadoop/hdfs?

# 由于hbase的数据存储在hadoop上，所以必须也清空hbase中的数据hbase-site.xml
cd /usr/local/var/zookeeper
rm -rf *
# 若遇到无法启动HMaster的情况，可以尝试如下命令
hbase org.apache.hadoop.hbase.util.hbck.OfflineMetaRepair 
# 如果报错，则直接备份hbase原有配置，删掉hbase（/usr/local/var下的）相关内容，直接重装hbase吧，别纠结了。

# 1.2 格式化
hadoop namenode -format

# 启动hadoop，注意要到hadoop目录下执行
./start-dfs.sh
./start-yarn.sh

# 停止hadoop，注意要到hadoop目录下执行
./stop-dfs.sh
./stop-yarn.sh

# 查看DataNode信息
hdfs dfsadmin -report 

# 在hdfs上创建目录tmp
hadoop fs -mkdir /tmp
# 提交当前目录下的1.txt到hdfs到/tmp目录下，并命令为1.txt
hadoop fs -put 1.txt /tmp/1.txt
# 在hdfs上查看/tmp目录下的文件 
hadoop fs -ls /tmp
# 在hadoop上运行jar包中的wordcount程序，并指定输入文件和输出结果位置
hadoop jar /opt/hadoop-mapreduce-examples-2.7.1.jar wordcount /tmp/1.txt /tmp/1_out
# 在hdfs上查看文件内容，（cat命令：将文件内容打印出来）
hadoop fs -cat /tmp/1_out/part-r-00000

# 删除hdfs上的目录
hdfs dfs -rm -r /data/simulate
```

# 默认端口号
```shell
Hadoop: http://localhost:8088/
DataNode: http://localhost:50070/


```

# 参考
[Mac OS X 10.10 运用 Homebrew安装Hadoop 2.7.1](http://blog.csdn.net/u012336567/article/details/50778989)  
[Mac 安装 hadoop+hive+hbase+spark](http://blog.csdn.net/hubin232/article/details/76769265)  
[hadoop_cluster搭建](https://www.jianshu.com/p/5f4be94630a3)  
[如何在MacOSX上安装Hadoop(how to install hadoop on mac os x)](http://www.ifzer.com/2014/10/31/how_to_install_hadoop_on_mac_ox_x/)
[重新格式化HDFS的方法](https://blog.csdn.net/yeruby/article/details/21542465)  
