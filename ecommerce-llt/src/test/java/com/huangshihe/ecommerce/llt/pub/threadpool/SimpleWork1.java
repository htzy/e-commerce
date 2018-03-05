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
public class SimpleWork1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleWork1.class);

    public static void drawLine() {
        LOGGER.info("{}+{}------------------------------------------",
                Thread.currentThread().getId(), Thread.currentThread().getName());
    }
}
