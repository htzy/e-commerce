package com.huangshihe.ecommerce.common.kits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeKit.class);

    /**
     * 时间格式.
     */
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式.
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 当前时间.
     */
    public static final String NOW = "now()";

    /**
     * 1970-1-1 0:0:0
     */
    public static final long ZERO_TIME = 0;

    /**
     * 一天时间毫秒.
     */
    public static final long ONE_DAY = 24 * 60 * 60 * 1000;

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
    public static String toTimeStr(long timestamp) {
        Date date = new Date(timestamp);
        return _timePattern.format(date);
    }

    /**
     * date转为时间格式的字符串.
     *
     * @param time date
     * @return 时间格式
     */
    public static String toTimeStr(Date time) {
        return _timePattern.format(time);
    }

    /**
     * 时间戳转为时间格式.
     *
     * @param timestamp 时间戳
     * @return 时间格式
     */
    public static String toTimeStr(String timestamp) {
        if (StringKit.isNotEmpty(timestamp)) {
            if (timestamp.trim().equalsIgnoreCase(NOW)) {
                return getNowTime();
            }
            long lt = new Long(timestamp.trim());
            return toTimeStr(lt);
        }
        return StringKit.emptyString;
    }


    /**
     * 年月日转为日期格式.
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 日期格式
     */
    public static String toDateStr(int year, int month, int day) {
        String result = "" + year;
        if (month < 10) {
            result += "-0" + month;
        } else {
            result += "-" + month;
        }
        if (day < 10) {
            result += "-0" + day;
        } else {
            result += "-" + day;
        }
        return result;
    }

    /**
     * 将时间格式的字符串转为毫秒.
     *
     * @param timeStr 时间格式的字符串
     * @return 毫秒
     */
    public static long toTimeSec(String timeStr) {
        if (StringKit.isNotEmpty(timeStr)) {
            if (timeStr.trim().equalsIgnoreCase(NOW)) {
                return getCurrentTime();
            }
            try {
                Date time = _timePattern.parse(timeStr.trim());
                return time.getTime();
            } catch (ParseException e) {
                LOGGER.error("timeStr to time sec error!, timeStr:{}, detail:{}", timeStr, e);
            }
        }
        // 返回格林威治时间.
        return ZERO_TIME;
    }


    /**
     * 获取今天日期.
     *
     * @return 今天日期
     */
    public static String getTodayDate() {
        Date date = new Date();
        return _datePattern.format(date);
    }

    /**
     * 获取当前时间.
     *
     * @return 当前时间
     */
    public static String getNowTime() {
        Date time = new Date();
        return _timePattern.format(time);
    }

    /**
     * 获取当前时间.
     *
     * @return 当前时间
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }


}
