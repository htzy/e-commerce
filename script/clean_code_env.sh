# 需要指定执行脚本当前目录，否则以下两种方式执行效果不一样
# 第一种：
# cd ${ecommerce}/script/
# ./clean_code_env.sh

# 第二种：
# cd ${ecommerce}
# ./script/clean_code_env.sh

# TODO 如何指定？


# 删除idea配置
echo "before clean: find .. -name *.iml"
# rm -f ../*.iml
echo | find .. -name *.iml
# find .. -name *.iml | xargs rm -f
find .. -name *.iml
# rm -rf ../.idea
# find .. -name .idea | xargs rm -rf
find .. -name .idea
echo "after clean: find .. -name *.iml"
echo | find .. -name *.iml
# 删除编译后文件
# find .. -name target | xargs rm -rf
# find .. -name out | xargs rm -rf
# find .. -name build | xargs rm -rf

find .. -name target
find .. -name out
find .. -name build
