# 版本
这里使用的spark版本为：2.1.0 对应使用的slf4j的版本为1.7.16，jackson的版本为2.6.5，而slf4j可以用更高的版本，不会报错。
但是jackson若版本不匹配，则会报：
```text
Exception in thread "main" java.lang.ExceptionInInitializerError
...
Caused by: com.fasterxml.jackson.databind.JsonMappingException: Incompatible Jackson version: 2.9.3
...
```
解决版本不匹配的方法：
1. 找到报错的位置，这里即使用jackson的位置，找到对应引用的包名，这里即RDDOperationScope.scala里用到了jackson：
```scala
import com.fasterxml.jackson.annotation.JsonInclude.Include
```
进入到该类中，点击idea左栏上第一个圆形小图标"Scroll from source"，这时会导航到左栏下面的
External Libraries中的jackson依赖中，这时即可看到多个jackson的依赖，除去自己在pom中引用的版本外，剩下的就是spark或其他框架引入的，此时改为
跟框架一样即可，这里由2.9.3改为2.6.5。


# 问题
1. java.lang.Integer is not a valid external type for schema of bigint
    原因为指定的类型为LongType，而传入的为int类型，只需在int类型后加"L"或直接定义一个Long变量，赋值后再将该变量传入。
    如果执行还报一样的错，重新手动编译整个项目再运行试试。

# 命令
```shell
# 启动spark，注意到spark目录下执行 
./start-master.sh 
./start-slaves.sh

# 停止spark，注意到spark目录下执行
./stop-master.sh    
./stop-slaves.sh
```

# 安装配置spark
```shell
brew install apache-spark
# 当前下载的版本为：2.3.0
# 安装目录：/usr/local/Cellar/apache-spark/2.3.0/

# 配置
# 配置spark-env.sh
export HADOOP_HOME=/usr/local/opt/hadoop
export HADOOP_CONF_DIR=/usr/local/opt/hadoop/libexec/etc/hadoop
export SPARK_LOCAL_IP=localhost
export SPARK_MASTER_IP=localhost
export SPARK_MASTER_PORT=7077

# 配置spark-defaults.conf
spark.master                    spark://localhost:7077
spark.eventLog.enabled           true
spark.eventLog.dir               hdfs://localhost:9000/spark-logs

# 配置slaves
localhost

```


# 查看日志

# 安装和配置Jupyter
```shell
# install scala
brew install scala@2.11

# This formula is keg-only, which means it was not symlinked into /usr/local,
# because this is an alternate version of another formula.

# If you need to have this software first in your PATH run:
#  echo 'export PATH="/usr/local/opt/scala@2.11/bin:$PATH"' >> ~/.bash_profile


# Bash completion has been installed to:
#  /usr/local/opt/scala@2.11/etc/bash_completion.d

# install spark
brew install apache-spark
# /usr/local/Cellar/apache-spark/2.3.0: 1,018 files, 243.7MB, built in 39 seconds
# 将pyspark，spark-beeline，spark-shell，spark-submit，spark-class，spark-sql，sparkR自动加入/usr/local/bin下

which pyspark
# /usr/local/bin/pyspark


# pip install toree 默认下载的是0.1x，适用于spark1.6，所以不适用于当前环境
# ref https://github.com/apache/incubator-toree/tree/master
pip install https://dist.apache.org/repos/dist/dev/incubator/toree/0.2.0/snapshots/dev1/toree-pip/toree-0.2.0.dev1.tar.gz

# jupyter toree install --spark_home=/Users/huangshihe/bigdata/spark-2.1.0-bin-hadoop2.7 --spark_opts="--master spark://localhost:7077 --driver-memory 1g --total-executor-cores 2" jupyter notebook
jupyter toree install --spark_home=/usr/local/Cellar/apache-spark/2.3.0/libexec --spark_opts="--master spark://localhost:7077 --driver-memory 1g --total-executor-cores 2" jupyter notebook


# jupyter toree install --interpreters=Scala,PySpark,SparkR,SQL
jupyter toree install --interpreters=Scala,PySpark,SQL

# confirm the installation 
jupyter kernelspec list
#Available kernels:
#  python2                 /Users/huangshihe/anaconda/share/jupyter/kernels/python2
#  apache_toree_pyspark    /usr/local/share/jupyter/kernels/apache_toree_pyspark
#  apache_toree_scala      /usr/local/share/jupyter/kernels/apache_toree_scala
#  apache_toree_sql        /usr/local/share/jupyter/kernels/apache_toree_sql

# 启动
jupyter notebook

```

# 参考
[Spark java读取Hbase数据](http://blog.csdn.net/incy_1218/article/details/71453608)  
[Jupyter配置Spark开发环境](https://blog.csdn.net/u012948976/article/details/52372644)  
[Apache Toree Documentation](https://toree.incubator.apache.org/docs/current/user/installation/)  
[大数据常见错误解决方案（转载）](http://www.mamicode.com/info-detail-2132095.html)  
[python matplotlib 画图简介](https://blog.csdn.net/ali197294332/article/details/51694141)  
[目前看到的最好的RNN、LSTM、GRU博客：Understanding LSTM Networks](https://blog.csdn.net/mmc2015/article/details/54848220)  

