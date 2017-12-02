### 更新时间
2017-11-26 初始化  
2017-11-26 增加分支说明  
2017-11-30 增加计量等TODO、项目结构及加入llt（low level test）  
2017-12-1 更新项目结构
2017-12-2 增加日志相关的TODO

# e-commerce需求
1. 通过写一个电商类的数据分析项目来学习Spark的数据汇聚内容
2. 数据汇聚包括：时间维度汇聚和空间维度汇聚
3. 使用Spark实现ARIMA


# 分支说明
- master 稳定发行版
- b_XXXXX XXXXX版本开发版

说明：目前分支b_0_0_1，待当前版本开发完之后，完全测试后，合并到主线master，并增加tag；新版本代码在新分支上开发。

# 项目结构
- com.huangshihe.<product_name>
    - business-services
        - <module_name>
    - <product_name>-llt

# TODO
1. 规范化项目结构
2. 相关、回归（一元线性回归、多元线性回归、截面线性回归、时间序列回归<MA/AR/ARMA/ARIMA/GARCH/VAR>、面板线性回归、Copula。
3. 完善日志的配置信息
4. 完善java自动生成的javadoc模板
5. 完善llt的配置文件
6. 可利用Spring进行技术整合

# 参考
[git 合并与创建分支](https://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000/001375840038939c291467cc7c747b1810aab2fb8863508000)  
[sfj4j文档](https://www.slf4j.org/manual.html)  
[cucumber java](https://cucumber.io/docs/reference/jvm#java)  
[cucumber java idea](https://www.jetbrains.com/help/idea/cucumber.html)
[Linux 软件安装到 /usr，/usr/local/ 还是 /opt 目录？](http://blog.csdn.net/aqxin/article/details/48324377)