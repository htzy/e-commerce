package com.huangshihe.ecommerce.llt.tmp;

/**
 * system
 * <p>
 * Create Date: 2018-05-06 19:19
 *
 * @author huangshihe
 */
public class SystemMain {
    public static void main(String[] args) {
        String home = System.getenv("HADOOP_HOME");
        System.out.println(home);//  /usr/local/Cellar/hadoop/2.7.3/libexec/

        home = System.getenv("JAVA_HOME");
        System.out.println(home);//   /Library/Java/JavaVirtualMachines/jdk1.8.0_111.jdk/Contents/Home
        home = System.getenv("HADOOP_CLASSPATH");
        System.out.println(home);   // 应该是：/usr/local/Cellar/hbase/1.2.6/libexec//lib/*

        home = System.getenv("HBASE_CLASSPATH");
        System.out.println(home);
    }
}
