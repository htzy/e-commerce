# 归档

## 存在争议？
1. 循环中创建对象？
[AvoidInstantiatingObjectsInLoops](https://pmd.github.io/pmd-5.8.1/pmd-java/rules/java/optimizations.html#AvoidInstantiatingObjectsInLoops)  
[Should we avoid object creation in Java?](https://softwareengineering.stackexchange.com/questions/149563/should-we-avoid-object-creation-in-java)  
[The “Why” behind PMD's rules](https://stackoverflow.com/questions/2509856/the-why-behind-pmds-rules)  


2. DataflowAnomalyAnalysis之UR，DU，DD
    - UR：指向一个之前没有定义的变量，这是bug且可导致错误
    - DU：一个刚刚定义的变量是未定义的。这些异常可能出现在普通的源代码文本中
    - DD：一个刚刚定义的变量重新定义。这是不好的但并非一定是个bug
    
[PMD DataflowAnomalyAnalysis之DU - Anomaly](https://www.cnblogs.com/cookie3ms/p/4364666.html)
[Java for each loop being flagged as UR anomaly by PMD](https://stackoverflow.com/questions/21592497/java-for-each-loop-being-flagged-as-ur-anomaly-by-pmd)

