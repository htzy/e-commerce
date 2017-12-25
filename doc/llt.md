# cucumber
## background和before的区别？
1. 相同：每次都会运行。


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


# 参考
[Execute Cucumber step before/after a specific feature](https://stackoverflow.com/questions/18856458/execute-cucumber-step-before-after-a-specific-feature)  
[Cucumber JVM: Hooks](https://zsoltfabok.com/blog/2012/09/cucumber-jvm-hooks/)    
[cucumber java](https://cucumber.io/docs/reference/jvm#java)  
