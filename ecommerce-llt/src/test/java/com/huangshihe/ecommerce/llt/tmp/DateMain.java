package com.huangshihe.ecommerce.llt.tmp;

import com.huangshihe.ecommerce.common.kits.TimeKit;

import java.util.Calendar;
import java.util.Date;

/**
 * date
 * <p>
 * Create Date: 2018-05-05 10:43
 *
 * @author huangshihe
 */
public class DateMain {
    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date.getHours());

        Calendar calendar = Calendar.getInstance();

        calendar.set(2018, 4 - 1, 23, 0, 0, 0);
        String timeStr = TimeKit.toTimeStr(calendar.getTime());
        System.out.println(timeStr);// 2018-04-23 00:00:00

        calendar.add(Calendar.SECOND, 5);
        timeStr = TimeKit.toTimeStr(calendar.getTime());//2018-04-23 00:00:05
        System.out.println(timeStr);

        calendar.set(2018, 4, 23, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        for (int i = 0; i < 100; i++) {
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            System.out.println(calendar.getTimeInMillis());
        }

    }
}
