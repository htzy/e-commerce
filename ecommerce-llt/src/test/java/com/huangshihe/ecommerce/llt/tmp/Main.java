package com.huangshihe.ecommerce.llt.tmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Create Date: 2018-04-10 21:20
 *
 * @author huangshihe
 */
public class Main {

    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static Main main = new Main();

    private final Object object = new Object();

//    private int count;    // 这里发现不用volatile，执行结果一样
    private volatile int count;

    public void increase() {
        LOGGER.debug("111 current thread:{} , count:{}", Thread.currentThread(), count);
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
// 执行结果：
//2018-04-10 22:13:42,000 | [DEBUG] | main | com.huangshihe.ecommerce.pub.config.Main.main(Main.java:45) | m1:204349222
//2018-04-10 22:13:42,002 | [DEBUG] | main | com.huangshihe.ecommerce.pub.config.Main.main(Main.java:47) | m2:204349222
//2018-04-10 22:13:42,003 | [DEBUG] | main | com.huangshihe.ecommerce.pub.config.Main.main(Main.java:49) | m3:204349222
//2018-04-10 22:13:42,006 | [DEBUG] | main | com.huangshihe.ecommerce.pub.config.Main.main(Main.java:51) | m4:204349222
//2018-04-10 22:13:42,007 | [DEBUG] | main | com.huangshihe.ecommerce.pub.config.Main.main(Main.java:53) | m5:204349222
//2018-04-10 22:13:42,090 | [DEBUG] | Thread-0 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:24) | 111 current thread:Thread[Thread-0,5,main] , count:0
//2018-04-10 22:13:42,090 | [DEBUG] | Thread-4 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:24) | 111 current thread:Thread[Thread-4,5,main] , count:0
//2018-04-10 22:13:42,090 | [DEBUG] | Thread-3 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:24) | 111 current thread:Thread[Thread-3,5,main] , count:0
//2018-04-10 22:13:42,090 | [DEBUG] | Thread-2 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:24) | 111 current thread:Thread[Thread-2,5,main] , count:0
//2018-04-10 22:13:42,090 | [DEBUG] | Thread-1 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:24) | 111 current thread:Thread[Thread-1,5,main] , count:0
//2018-04-10 22:13:42,090 | [DEBUG] | Thread-0 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:26) | 000 current thread:Thread[Thread-0,5,main], count:0
//2018-04-10 22:13:42,092 | [DEBUG] | Thread-0 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:29) | *** current thread:Thread[Thread-0,5,main], count:1
//2018-04-10 22:13:42,092 | [DEBUG] | Thread-0 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:32) | 222 current thread:Thread[Thread-0,5,main] , count:1
//2018-04-10 22:13:42,092 | [DEBUG] | Thread-1 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:26) | 000 current thread:Thread[Thread-1,5,main], count:1
//2018-04-10 22:13:42,092 | [DEBUG] | Thread-1 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:32) | 222 current thread:Thread[Thread-1,5,main] , count:1
//2018-04-10 22:13:42,092 | [DEBUG] | Thread-2 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:26) | 000 current thread:Thread[Thread-2,5,main], count:1
//2018-04-10 22:13:42,095 | [DEBUG] | Thread-2 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:32) | 222 current thread:Thread[Thread-2,5,main] , count:1
//2018-04-10 22:13:42,095 | [DEBUG] | Thread-3 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:26) | 000 current thread:Thread[Thread-3,5,main], count:1
//2018-04-10 22:13:42,095 | [DEBUG] | Thread-3 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:32) | 222 current thread:Thread[Thread-3,5,main] , count:1
//2018-04-10 22:13:42,095 | [DEBUG] | Thread-4 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:26) | 000 current thread:Thread[Thread-4,5,main], count:1
//2018-04-10 22:13:42,096 | [DEBUG] | Thread-4 | com.huangshihe.ecommerce.pub.config.Main.increase(Main.java:32) | 222 current thread:Thread[Thread-4,5,main] , count:1
