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
```

### 对于静态类而言
```java
public class Foo {
    static Helper singleton = new Helper();
}

class Helper {

}
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


```shell
brew 
# 查看hadoop.rb的历史
git log master -- Formula/hadoop.rb

git checkout 9d524e4
```

# 参考
[The "Double-Checked Locking is Broken" Declaration](http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html)  
[Java 7 新的 try-with-resources 语句，自动资源释放](http://www.oschina.net/question/12_10706)  
[jackson readTree](https://www.cnblogs.com/yangy608/p/3939315.html)  
[使用 Jackson 树模型(tree model) API 处理 JSON](http://blog.csdn.net/gao1440156051/article/details/54091702)    
[JavaFX 8 教程](http://code.makery.ch/library/javafx-8-tutorial/zh-cn/part1/)  
[EA如何打包可运行jar的一个问题](http://bglmmz.iteye.com/blog/2058785)    
[Protobuf的那些事](http://blog.csdn.net/u013022222/article/details/50521835)  
[利用JAXB实现xml和bean之间的转换（一）](http://blog.csdn.net/lchinam/article/details/51785036)  
