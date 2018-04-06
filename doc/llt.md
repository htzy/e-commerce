# cucumber
## background和before的区别？
1. 相同：每次都会运行。
2. Background：background的steps在feature中可见，可代替Before，给每个scenario创建上下文。
在scenario执行之前执行，若还存在Before，则Background会先于Before执行。
3. 当相同的操作，可放至Background也可以放至Before中，则选择哪一种方式，取决于当它们明确的出现在feature文件里是否有价值。

## before和after的用法
```java
class FooTest{
    @Before
    public void beforeScenario() {
        tomcat.start();
        tomcat.deploy("munger");
        browser = new FirefoxDriver();
    }
    
    @After
    public void afterScenario() {
        browser.close();
        tomcat.stop();
    }
}
```
或者使用下面的方式：
```text
Feature: web text munger kata
  @web
  Scenario: It should process a sentence
    // The steps
```

```java
class FooTest{
    @Before("@web")
    public void beforeScenario() {
      // actions
    }
    
    @After("@web")
    public void afterScenario() {
      // actions
    }
}
```
## hbase-llt
````text
"""
新用例若已覆盖到旧用例，则删除旧用例即可（即旧用例已重复，没必要）
将创建等内容抽象出来，放入background，删除不必要的用例（或者注释？）

Background: 每一次都会调用下面的语句，但这里连接只在第一次创建。除非测试删除表，否则每次运行后都会自动删除表名为tableName的表
Before: 准备测试环境(): 每次都会执行：创建hbase连接
After: 清理测试环境(): 每次都会执行：当tableName表名不为空时，删除该表

TODO 再多一点再删除，迭代出口整改时统一删除。（代码未稳定时，什么狗屎都可能会有！）

"""
````

# 参考
[Execute Cucumber step before/after a specific feature](https://stackoverflow.com/questions/18856458/execute-cucumber-step-before-after-a-specific-feature)  
[Cucumber JVM: Hooks](https://zsoltfabok.com/blog/2012/09/cucumber-jvm-hooks/)    
[cucumber java](https://cucumber.io/docs/reference/jvm#java)  
[Cucumber入门之_Hooks&Background](https://www.cnblogs.com/puresoul/archive/2012/03/05/2380543.html)  
