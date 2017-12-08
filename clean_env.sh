# 删除idea配置
echo "before clean: find . -name *.iml"
echo | find . -name *.iml
find . -name *.iml | xargs rm -f
find . -name .idea | xargs rm -rf
echo "after clean: find . -name *.iml"
echo | find . -name *.iml
# 删除编译后文件
find . -name target | xargs rm -rf
find . -name out | xargs rm -rf
find . -name build | xargs rm -rf
