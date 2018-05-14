package com.huangshihe.ecommerce.common.kits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
     * 时间字节长度.
     */
    public static final int TIME_BYTE_LEN = 8;

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
    @Deprecated
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
     * 将时间格式的字符串转为毫秒.
     *
     * @param timeStr 时间格式的字符串
     * @return 毫秒
     */
    public static long toTimeMillSec(String timeStr) {
        return toTimeSec(timeStr);
    }

    /**
     * 生成时间，并转为毫秒.
     *
     * @param year   year
     * @param month  1-12
     * @param day    1-31？
     * @param hour   0-23
     * @param minute 0-59
     * @param second 0-59
     * @return 毫秒
     */
    public static long toTimeMillSec(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 根据millis获取指定天的小时数.
     *
     * @param millis 毫秒
     * @return 小时
     */
    public static int getHour(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 根据mills获取单位为小时，即丢弃分钟，秒，毫秒
     *
     * @param millis 毫秒
     * @return millis
     */
    public static long getHours(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 根据millis获取指定小时的分钟数.
     *
     * @param millis 毫秒
     * @return 分钟
     */
    public static int getMinute(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.MINUTE);
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
