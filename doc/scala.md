# classOf与getClass区别
```sbtshell

huangshihe (b_0_0_1 *) ecommerce $ scala
Welcome to Scala 2.11.12 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_111).
Type in expressions for evaluation. Or try :help.

scala> class A
defined class A

scala> val a = new A
a: A = A@6c25e6c4

scala> a.getClass == classOf[A]
res0: Boolean = true


```


# 参考
[scala类型系统：2) classOf与getClass方法的差异](http://hongjiang.info/scala-type-system-classof-and-getclass/)  
[scala学习](http://hongjiang.info/scala/)  

