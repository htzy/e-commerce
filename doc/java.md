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

# 参考
[The "Double-Checked Locking is Broken" Declaration](http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html)  
