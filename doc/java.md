# 两步检查
## 修改方案
### 对于对象而言
```java
class Foo {
    private volatile Helper helper = null;

    public Helper getHelper() {
        if (helper == null) {
            synchronized (this) {
                if (helper == null)
                    helper = new Helper();
            }
        }
        return helper;
    }
}

class Helper {

}

public class Temp {
    private static volatile Temp t;

    private Temp() {

    }

    public static Temp getInstance() {
        if (t == null) {
            synchronized (Temp.class) {
                if (t == null) {
                    t = new Temp();
                }
            }
        }
        return t;
    }
}
```

### 对于静态类而言
```java
public class Foo {
    static Helper singleton = new Helper();
}

class Helper {

}
```

### 例子
```java

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static Main main = new Main();

    private final Object object = new Object();

    private volatile int count;

    public void increase() {
        LOGGER.debug("111 current thread:{} , count:{}, time", Thread.currentThread(), count);
        synchronized (this.object) {
            LOGGER.debug("000 current thread:{}, count:{}", Thread.currentThread(), count);
            if (count == 0) {
                count++;
                LOGGER.debug("*** current thread:{}, count:{}", Thread.currentThread(), count);
            }
        }
        LOGGER.debug("222 current thread:{} , count:{}", Thread.currentThread(), count);
    }

    private Main() {

    }

    public static Main getInstance() {
        return main;
    }

    public static void main(String[] args) {
        Main m1 = Main.getInstance();
        LOGGER.debug("m1:{}", m1.hashCode());
        Main m2 = Main.getInstance();
        LOGGER.debug("m2:{}", m2.hashCode());
        Main m3 = Main.getInstance();
        LOGGER.debug("m3:{}", m3.hashCode());
        Main m4 = Main.getInstance();
        LOGGER.debug("m4:{}", m4.hashCode());
        Main m5 = Main.getInstance();
        LOGGER.debug("m5:{}", m5.hashCode());

        Thread t1 = new Thread(m1::increase);
        Thread t2 = new Thread(m2::increase);
        Thread t3 = new Thread(m3::increase);
        Thread t4 = new Thread(m4::increase);
        Thread t5 = new Thread(m5::increase);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}

// 结果：
//2018-04-10 21:58:28,899 | [DEBUG] | main | com.huangshihe.ecommerce.pub.config.Main.main(Main.java:44) | m1:204349222
//2018-04-10 21:58:28,903 | [DEBUG] | main | com.huangshihe.ecommerce.pub.config.Main.main(Main.java:46) | m2:204349222
//2018-04-10 21:58:28,903 | [DEBUG] | main | com.huangshihe.ecommerce.pub.config.Main.main(Main.java:48) | m3:204349222
//2018-04-10 21:58:28,906 | [DEBUG] | main | com.huangshihe.ecommerce.pub.config.Main.main(Main.java:50) | m4:204349222
//2018-04-10 21:58:28,906 | [DEBUG] | main | com.huangshihe.ecommerce.pub.config.Main.main(Main.java:52) | m5:204349222
//2018-04-10 21:58:29,007 | [DEBUG] | Thread-0 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:23) | 111 current thread:Thread[Thread-0,5,main] , count:0
//2018-04-10 21:58:29,007 | [DEBUG] | Thread-0 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:25) | 000 current thread:Thread[Thread-0,5,main], count:0
//2018-04-10 21:58:29,007 | [DEBUG] | Thread-2 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:23) | 111 current thread:Thread[Thread-2,5,main] , count:0
//2018-04-10 21:58:29,007 | [DEBUG] | Thread-1 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:23) | 111 current thread:Thread[Thread-1,5,main] , count:0
//2018-04-10 21:58:29,007 | [DEBUG] | Thread-3 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:23) | 111 current thread:Thread[Thread-3,5,main] , count:1
//2018-04-10 21:58:29,007 | [DEBUG] | Thread-0 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:28) | *** current thread:Thread[Thread-0,5,main], count:1
//2018-04-10 21:58:29,008 | [DEBUG] | Thread-4 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:23) | 111 current thread:Thread[Thread-4,5,main] , count:1
//2018-04-10 21:58:29,009 | [DEBUG] | Thread-0 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:31) | 222 current thread:Thread[Thread-0,5,main] , count:1
//2018-04-10 21:58:29,009 | [DEBUG] | Thread-3 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:25) | 000 current thread:Thread[Thread-3,5,main], count:1
//2018-04-10 21:58:29,010 | [DEBUG] | Thread-3 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:31) | 222 current thread:Thread[Thread-3,5,main] , count:1
//2018-04-10 21:58:29,010 | [DEBUG] | Thread-1 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:25) | 000 current thread:Thread[Thread-1,5,main], count:1
//2018-04-10 21:58:29,012 | [DEBUG] | Thread-1 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:31) | 222 current thread:Thread[Thread-1,5,main] , count:1
//2018-04-10 21:58:29,012 | [DEBUG] | Thread-2 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:25) | 000 current thread:Thread[Thread-2,5,main], count:1
//2018-04-10 21:58:29,012 | [DEBUG] | Thread-2 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:31) | 222 current thread:Thread[Thread-2,5,main] , count:1
//2018-04-10 21:58:29,012 | [DEBUG] | Thread-4 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:25) | 000 current thread:Thread[Thread-4,5,main], count:1
//2018-04-10 21:58:29,013 | [DEBUG] | Thread-4 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:31) | 222 current thread:Thread[Thread-4,5,main] , count:1



```

# Java7以上的try-with-resources，自动资源释放
## 比较
### 原有方法
```java
class Foo{
    public boolean isExists(final String tableNameStr) {
        boolean result = false;
        Admin admin = null;
        try {
            admin = connection.getAdmin();
            result = admin.tableExists(TableName.valueOf(tableNameStr));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (admin != null) {
                    admin.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
```

### 现有方法
```java
class Foo{
    public boolean isExists(final String tableNameStr) {
        boolean result = false;
        try (Admin admin = connection.getAdmin()) {
            result = admin.tableExists(TableName.valueOf(tableNameStr));
            // admin继承了AutoCloseable，在try-with-resources中不需要手动关闭。
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
```

# jackson
```java
class Foo{
    public void foo(){
        // 构造tree
        JsonNode jsonNode;
        ObjectNode rootNode = jsonNodeFactory.objectNode();
        rootNode.put("id", 666);
        rootNode.put("name", "htzy");
        jsonNode = rootNode;
        // 或者通过对象转为tree
//        simple = new Simple();
//        simple.setId(666);
//        simple.setName("htzy");
//        jsonNode = JsonUtil.objToTree(simple);

        // 取值方法，一律推荐使用asXXX方法
        // 其他方法有坑！如textValue()只有节点内是String类型才有返回值，否则返回null
        jsonNode.asText("name");
    }  
    
    /**
     * 返回json节点，当遇到null时，返回MissingNode.
     *
     * @param json      json
     * @param dataField 数据项
     * @return json节点
     */
    public static JsonNode getNodeNeverNull(final String json, final String dataField) {
        // 首先要判断strToTree返回的类型是什么？，假设strToTree返回的类型是ObjectNode，
        // 节点为空时：path返回：MissingNode；而get返回null（从LinkedHashMap中获取）
        // 先有的所有JsonNode类型，遇到空节点，这里都将返回MissingNode，值为""
        return strToTree(json).path(dataField);
    }

    /**
     * 返回json节点，可能是null.
     *
     * @param json
     * @param dataField
     * @return
     */
    public static JsonNode getNodeWithNull(final String json, final String dataField) {
        return strToTree(json).get(dataField);
    }
}

```
# for循环
## 增强型for循环：for(E e: Collection<E> es)
这种的for循环中的es不能为空，否则会导致空指针，实际上内部调用的为：iterator()，以下两种方式等同
```java
class Foo {
    void fooFor() {
        if (root.exists() && root.listFiles() != null) {
            // File.listFiles()可能为null，下面可能触发NullPointerException，所以需要提前检查
            for (File file : root.listFiles()) {
                // ...
            }

        }
    }
    
    void fooIterator() {
        if (root.exists() && root.listFiles() != null) {
            Collection collection = Arrays.asList(root.listFiles());
            for (Iterator iter = collection.iterator(); iter.hasNext(); ) {
                // ...
            }
        }
    }
    
    public static void main(String[] args) {
        List<Object> re = null;
        for (Object i : re) {   // NullPointerException
            System.out.println(i);
        }
        System.out.println("can't be done...");
    }
}
```


# java正则表达式
```java
class Foo {
    void foo() {
        // ...
        // 匹配以.xml为后缀名的文件，而\\.xml，xml等作为匹配符，都不能使用String.matches()匹配成功
        // 由于String.matches()的方式会默认增加^$符号：<pattern> => ^<pattern>$，所以无法使用"xml"来匹配。
        String pattern = "(.*)\\.xml"; 
        if (file.getName().matches(pattern)) {
            results.add(file);
        }
        // ...
        // 也可使用Pattern，compile可以使用.xml、xml等作为匹配符，使用Pattern.matcher()匹配成功
        Pattern pattern = Pattern.compile("xml");
//        Pattern pattern = Pattern.compile(".xml");
        String name = "simple_data.xml";
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            System.out.println(matcher.group()); // xml
//            System.out.println(matcher.group()); // .xml
        }
    }
}

```

# Map
```java
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("1", "1");
        String result = map.computeIfAbsent("1", string -> "3");
        System.out.println(result);     // 1
        result = map.get("1");
        System.out.println(result);     // 1

        result = map.computeIfAbsent("2", string -> "5");
        System.out.println(result);     // 5
        result = map.get("2");
        System.out.println(result);     // 5
    }
}

```

# 流
## 遇见的错误记录
```shell
# 报错：stream has already been operated upon or closed
# 流只能使用一次！不能多次使用
```


# mvn
```shell
# 打包，并跳过测试代码
mvn clean install -Dmaven.test.skip=true

```

# 解压jar
```shell
# 以下命令不可用：C只有在创建或者更新jar包时作用才是指定目录 
# jar xvf *.jar -C 路径
# 解压到当前目录下
jar xvf *.jar

unzip *.jar -d 路径


```

# 参考
[The "Double-Checked Locking is Broken" Declaration](http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html)  
[Java 7 新的 try-with-resources 语句，自动资源释放](http://www.oschina.net/question/12_10706)  
[jackson readTree](https://www.cnblogs.com/yangy608/p/3939315.html)  
[使用 Jackson 树模型(tree model) API 处理 JSON](http://blog.csdn.net/gao1440156051/article/details/54091702)    
[JavaFX 8 教程](http://code.makery.ch/library/javafx-8-tutorial/zh-cn/part1/)  
[javafx中固定Stage窗口大小](https://blog.csdn.net/littlesshark/article/details/51804870)  
[EA如何打包可运行jar的一个问题](http://bglmmz.iteye.com/blog/2058785)    
[Protobuf的那些事](http://blog.csdn.net/u013022222/article/details/50521835)  
[利用JAXB实现xml和bean之间的转换（一）](http://blog.csdn.net/lchinam/article/details/51785036)  
[Jaxb2 实现JavaBean与xml互转](http://blog.csdn.net/wugouzi/article/details/25044655/)  
[java for循环的几种写法](https://www.cnblogs.com/wangcp-2014/p/5379208.html)  
[Java matches() 方法](http://www.runoob.com/java/java-string-matches.html)  
[Regex doesn't work in String.matches()](https://stackoverflow.com/questions/8923398/regex-doesnt-work-in-string-matches)  
[Java正则-String.matches的坑](http://blog.csdn.net/qq_24505485/article/details/54799882)  
[Jackson Annotation Examples](http://www.baeldung.com/jackson-annotations)  
[More Jackson Annotations](http://www.baeldung.com/jackson-advanced-annotations)  
[Java笔试面试题整理第六波（修正版）](http://blog.csdn.net/shakespeare001/article/details/51330745)  
[java中基于线程池和反射机制实现定时任务](http://blog.csdn.net/5iasp/article/details/10949925)  
[用 Java 实现拦截器 Interceptor 的拦截功能](http://blog.csdn.net/qq_35246620/article/details/68484407)  
[debug模式下发现toString()抛NullPointerException](http://blog.csdn.net/lzjansing/article/details/50319817)  
[CGLIB(Code Generation Library)详解](http://blog.csdn.net/danchu/article/details/70238002)  
[Java 的方法签名与字段类型表示-\[Ljava.lang.String;](https://unmi.cc/java-signatures-data-types/)  
[ScheduleExecutorService定时周期执行指定的任务](https://www.jianshu.com/p/4fe0b09fdbfc)  
[CGLIB动态代理介绍](http://ifeve.com/cglib-desc/)  
[详细介绍Java虚拟机（JVM）](https://www.cnblogs.com/IUbanana/p/7067362.html)  
[JVM内幕：Java虚拟机详解](https://www.cnblogs.com/aishangJava/p/7357867.html)  
[Java基础之实现解压和压缩jar、zip、rar等源码分享](https://blog.yoodb.com/yoodb/article/detail/1319)  
[一心多用多线程-细谈java线程池submit与execute的区别](https://blog.csdn.net/hayre/article/details/53314599)  
[java-源码解读-线程池提交之execute和submit有何不同](https://blog.csdn.net/abountwinter/article/details/78123422)  
[13.ThreadPoolExecutor线程池之submit方法](https://www.cnblogs.com/yulinfeng/p/7039979.html)  
[Java并发编程：线程池的使用](https://www.cnblogs.com/dolphin0520/p/3932921.html)  
[聊聊并发（三）Java线程池的分析和使用](http://ifeve.com/java-threadpool/)  
[volatile关键字解析(jdk1.5之后)](https://blog.csdn.net/canot/article/details/51295228)  
[Java随机生成Mac地址（GitHub源码下载）](https://blog.csdn.net/kyyee/article/details/72873148)  
[JavaFX TextField文本域值改变监听事件](https://blog.csdn.net/qq_20336817/article/details/53213430)  
[用MR生成HFile文件格式后，数据批量导入HBase](https://www.cnblogs.com/zhanggl/p/5562993.html)  
[Java 8中10个不易察觉的错误](https://www.evget.com/article/2014/6/18/21196.html)  
[如何在Java中使用正则表达式匹配多个内容?](https://ask.csdn.net/questions/646311?utm_source=debugrun&utm_medium=referral)  
[调整Hbase中cell大小](https://blog.csdn.net/godspeedlaile9/article/details/36875391)  
[hadoop(mapreduce):java.lang.NoSuchMethodException: ******Mapper.<init>()](https://blog.csdn.net/drhhyh/article/details/43407495)  
[Bulk Load－HBase数据导入最佳实践](https://blog.csdn.net/opensure/article/details/47054861)  
[使用Spark通过Bulkload的方式导数据到Hbase](https://www.2cto.com/net/201710/692437.html)  
[笔记-MapReduce入门](https://blog.csdn.net/mr_zhuqiang/article/details/45013081)  
[使用BulkLoad快速向HBase导入数据](https://zhuanlan.zhihu.com/p/28841351)  
[Mac OS X 下查看和设置JAVA_HOME](https://blog.csdn.net/done58/article/details/51138057)  
[通过反射，如何操作私有成员变量（取/赋值），如何调用私有方法？](https://www.cnblogs.com/jiangyi-uestc/p/5689771.html)  
[Java 反射 调用私有构造方法](https://www.cnblogs.com/WJQ2017/p/7762292.html)  


