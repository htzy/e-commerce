# 删除idea配置
find . -name *.iml | xargs rm -f
find . -name .idea | xargs rm -rf
# 删除编译后文件
find . -name target | xargs rm -rf
find . -name out | xargs rm -rf
find . -name build | xargs rm -rf


