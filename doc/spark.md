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


# 参考
[Spark java读取Hbase数据](http://blog.csdn.net/incy_1218/article/details/71453608)  
