package com.huangshihe.ecommerce.llt.pub.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * simple work 1
 * <p>
 * Create Date: 2018-03-05 20:05
 *
 * @author huangshihe
 */
public class SimpleService1 {

    private static Integer count = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleService1.class);

    public void drawLine() {
        LOGGER.info("{}++++{}-----------------------------------------",
                Thread.currentThread().getId(), Thread.currentThread().getName());
    }

    public static void increaseCount() {
        count++;
    }

    public static int getCount() {
        return count;
    }

    @Override
    public String toString() {
        LOGGER.info("{}++++{}-----------------", Thread.currentThread().getId(), Thread.currentThread().getName());
        return "SimpleService1{}";
    }
}
