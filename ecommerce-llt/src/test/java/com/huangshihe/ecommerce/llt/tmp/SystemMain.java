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
    }
}
