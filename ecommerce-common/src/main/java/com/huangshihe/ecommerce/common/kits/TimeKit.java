package com.huangshihe.ecommerce.common.kits;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类.
 * 所有地方的时间格式都来自于TimeKit这里
 * <p>
 * Create Date: 2018-01-15 17:11
 *
 * @author huangshihe
 */
public class TimeKit {

    /**
     * 时间格式.
     */
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式.
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 时间格式.
     */
    private static final SimpleDateFormat _timePattern = new SimpleDateFormat(TIME_PATTERN);

    private static final SimpleDateFormat _datePattern = new SimpleDateFormat(DATE_PATTERN);

    /**
     * 时间戳转为时间格式.
     *
     * @param timestamp 时间戳
     * @return 时间格式
     */
    public static String toTime(long timestamp) {
        Date date = new Date(timestamp);
        return _timePattern.format(date);
    }

    /**
     * 时间戳转为时间格式.
     *
     * @param timestamp 时间戳
     * @return 时间格式
     */
    public static String toTime(String timestamp) {
        if (StringKit.isNotEmpty(timestamp)) {
            long lt = new Long(timestamp.trim());
            return toTime(lt);
        }
        return StringKit.emptyString;
    }

    /**
     * 获取今天日期.
     * @return 今天日期
     */
    public static String getTodayDate() {
        Date date = new Date();
        return _datePattern.format(date);
    }


}
