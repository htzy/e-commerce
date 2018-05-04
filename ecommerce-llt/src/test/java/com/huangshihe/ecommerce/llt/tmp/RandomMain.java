package com.huangshihe.ecommerce.llt.tmp;

import java.util.Random;

/**
 * 随机数.
 * <p>
 * Create Date: 2018-04-11 22:41
 *
 * @author huangshihe
 */
public class RandomMain {

    public static void main(String[] args) {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int result = random.nextInt(2);
            System.out.println(result);
        }
    }
}
