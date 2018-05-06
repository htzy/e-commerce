#!/usr/bin/env bash

# 设置hadoop环境变量 重启生效
#HADOOP_HOME=/usr/local/Cellar/hadoop/2.7.3/libexec/
#launchctl setenv HADOOP_HOME $HADOOP_HOME

PROJECT_ROOT=".."
SOURCE_ROOT="/Users/huangshihe/.m2/repository/com/huangshihe/ecommerce"
INSTALL_JAR_ROOT="/usr/local/opt/ecommerce/lib/"
CONFIGS_JAR_ROOT="/usr/local/opt/ecommerce/configs/jar"
DATA_ROOT="/usr/local/opt/ecommerce/data/"

# 打包项目
cd "$PROJECT_ROOT" && mvn clean install -Dmaven.test.skip=true

# TODO 将hbase-dao等jar包上传到hbase依赖库中，这里暂时使用符号链接
ln -s /Users/huangshihe/.m2/repository/com/huangshihe/ecommerce/ecommerce-hbase/ec-hbase-dao/0.0.1/ec-hbase-dao-0.0.1.jar /usr/local/opt/hbase/libexec/lib/ec-hbase-dao-0.0.1.jar

#unzip *config-0.0.1.jar -d /usr/local/opt/ecommerce
echo 'begin to deploy...'
#find "$PROJECT_ROOT" -type f -name "\*.jar"
echo "rm lib ing..."
rm -rfv "$INSTALL_JAR_ROOT"
echo "rm lib end..."
# -p 递归创建目录
mkdir -pv "$INSTALL_JAR_ROOT"
# 拷贝当前目录下的所有jar包到安装目录（/usr/local/opt/ecommerce/lib）下，除去llt相关的jar包，并屏蔽tools目录
find "$PROJECT_ROOT" -type f -name '*.jar' -not -name '*llt*' -not -path "$PROJECT_ROOT/tools/*" -exec cp '{}' "$INSTALL_JAR_ROOT" ';'

echo "rm configs jar ing..."
rm -rfv "$CONFIGS_JAR_ROOT"
echo "rm configs jar end..."
mkdir -pv "$CONFIGS_JAR_ROOT"
# 拷贝配置文件到配置目录（/usr/local/opt/ecommerce/configs/jar）下
find "$PROJECT_ROOT" -type f -name 'ecommerce-*-config-*.jar' -not -path "$PROJECT_ROOT/tools/*" -exec cp '{}' "$CONFIGS_JAR_ROOT" ';'
#find "$PROJECT_ROOT" \( -type f -name '*.jar' \) -not \( -type f -name '*llt*' \)

echo "rm data ing..."
rm -rfv "$DATA_ROOT"
echo "rm data end..."

# 使用java代码解压配置文件到以下目录下：
#/usr/local/opt/ecommerce/data/configs/


echo 'deploy finish...'