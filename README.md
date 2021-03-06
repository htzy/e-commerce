### 更新时间
2017-11-26 初始化  
2017-11-26 增加分支说明  
2017-11-30 增加计量等TODO、项目结构及加入llt（low level test）  
2017-12-1 更新项目结构  
2017-12-2 增加日志相关的TODO  
2017-12-4 增加白盒检查工具  
2017-12-7 完善项目结构  
2017-12-10 增加工具说明  
2017-12-16 增加分支说明  
2018-2-26 增加部署及启动方式


# e-commerce需求
1. 通过写一个电商场景下的的数据分析项目来学习Spark的数据汇聚内容
2. 数据汇聚包括：时间维度汇聚和空间维度汇聚
3. 使用Spark实现ARIMA


# 分支说明
- master 稳定发行版
- b_XXXXX_XXXXX版本开发版

说明：目前分支b_0_0_1，待当前版本开发完之后，完全测试后，合并到主线master，并增加tag；新版本代码在新分支上开发。
代码开发、清理白盒、清理废弃代码等操作建议分开操作，并在git commit中说明，如果混在一起提交，将损失很多信息，之前遇到的问题也无法参考。

# 项目结构
- com.huangshihe.<product_name>      // 项目名
    - business-services              // 下面放组件
        - <component_name>           // 组件
            - <component_name>-api   // 对外暴露的API
            - <component_name>-config        // 配置
            - <component_name>-database      // 数据库
            - <component_name>-llt           // low level test
            - <component_name>-privilege     // 
            - <component_name>-module        // 下面放模块，如ui、service、dao、entity等
    - <product_name>.common          // 放常用工具包（可以增加线程池的工具包，但线程池的主体是归为public）
    - <product_name>.public          // 放公共内容，如线程池、全局配置文件管理（而非配置文件工具类）及需要提前部署的内容
        - Main.Main                  // 程序入口


# 工具说明
- 公式编辑——macOS Grapher
    - 将公式在Grapher中编辑好，拷贝成LaTex格式
    - 将公式粘贴在"http://latex.codecogs.com/gif.latex?\\" 的后面
    - 拷贝到markdown中


# 白盒检查工具
- 白盒覆盖率：cucumber-llt
- PMD
- FindBugs
- CheckStyle（Sun Checks）
- SourceMonitor（暂时不考虑）


# 部署及启动
- 先部署再启动
- 部署
    - 统一按层级解压每个组件下的配置jar到（/usr/local/opt/ecommerce/data/configs/）目录下
    - 上传必要的jar包（如ec-hbase-dao）到集群上
    - 所有的依赖jar包拷贝到：/usr/local/opt/ecommerce/lib目录下
    - 配置文件jar包拷贝到：/usr/local/opt/ecommerce/configs/jar目录下
    - 配置文件jar包将在部署代码中的java部分解压到数据目录下，如hbase配置文件目录：/usr/local/opt/ecommerce/data/configs/hbase/pub
    
- 启动
    - 暂不引入bundle（osgi），当前成本太高
    - 规避方案：引入一个初始init任务，启动所有的组件
    - 初始init任务中定义定时任务
    


# TODO
1. 相关、回归（一元线性回归、多元线性回归、截面线性回归、时间序列回归<MA/AR/ARMA/ARIMA/GARCH/VAR>、面板线性回归、Copula。
2. 完善日志的配置信息
3. 完善llt的配置文件
4. 可利用Spring进行技术整合
5. HBase查询优化
6. HBase org.apache.hadoop.conf.Configuration 的配置原理
7. 迭代出口，整改llt和白盒
8. 内部使用的字段加_下划线，私有方法也同样加_下划线（静态的视情况定）
9. 设计数据库
10. 去掉LOGGER中的类名和方法名，冗余！
11. 人工审核，确认不再需要的对象，及时置空，便于GC


# 参考
[git 合并与创建分支](https://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000/001375840038939c291467cc7c747b1810aab2fb8863508000)  
[sfj4j文档](https://www.slf4j.org/manual.html)  
[Linux 软件安装到 /usr，/usr/local/ 还是 /opt 目录？](http://blog.csdn.net/aqxin/article/details/48324377)  
[日志错误](http://zhangzhenyihi.blog.163.com/blog/static/13548809420141015055383/)  
[常见的几种RuntimeException](http://blog.csdn.net/qq635785620/article/details/7781026)  
[Maven创建多模块项目（包括依赖版本号的统一更新）](https://www.cnblogs.com/EasonJim/p/6863987.html)  
[Maven详解之聚合与继承](http://blog.csdn.net/wanghantong/article/details/36427411)  
[Equation Editor](http://latex.codecogs.com/eqneditor/integration/htmlequations.php)  
[github-jackson](https://github.com/FasterXML/jackson-core)  
