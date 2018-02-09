#!/bin/bash
SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do
    TARGET="$(readlink "$SOURCE")"
    if [[ $TARGET == /* ]]; then
        echo "SOURCE '$SOURCE' is an absolute symlink to '$TARGET'"
        SOURCE = "$TARGET"
    else
        DIR="$( dirname "$SOURCE" )"
        echo "SOURCE '$SOURCE' is a relative symlink to '$TARGET' (relative to '$DIR')"
        SOURCE="$DIR/$TARGET" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
    fi
done
echo "SOURCE is '$SOURCE'"
RDIR="$( dirname "$SOURCE" )"
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
if [ "$DIR" != "$RDIR" ]; then
  echo "DIR '$RDIR' resolves to '$DIR'"
fi
echo "DIR is '$DIR'"

echo "-------------------------------"
echo | find "$DIR" -name *.iml

echo "-------------------------------"

# 得去上一级目录，即${ecommerce}目录，如以下方式，可以查到内容
find /Users/huangshihe/bigdata/codes/ecommerce/ -name *.iml

echo "11111111"

# 需要指定执行脚本当前目录，否则以下两种方式执行效果不一样
# 第一种：
# cd ${ecommerce}/script/
# ./clean_code_env.sh

# 第二种：
# cd ${ecommerce}
# ./script/clean_code_env.sh

# TODO 如何指定？
# 删除要显示删除的文件，使用-v参数


# 删除idea配置
echo "before clean: find .. -name *.iml"
# rm -f ../*.iml
echo | find .. -name *.iml
# find .. -name *.iml | xargs rm -fv
# find .. -name *.iml
# rm -rf ../.idea
# find .. -name .idea | xargs rm -rfv
# find .. -name .idea
echo "after clean: find .. -name *.iml"
echo | find .. -name *.iml
# 删除编译后文件
# find .. -name target | xargs rm -rfv
# find .. -name out | xargs rm -rfv
# find .. -name build | xargs rm -rfv

find .. -name target
find .. -name out
find .. -name build
