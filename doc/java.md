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

# 参考
[The "Double-Checked Locking is Broken" Declaration](http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html)  
[Java 7 新的 try-with-resources 语句，自动资源释放](http://www.oschina.net/question/12_10706)  
