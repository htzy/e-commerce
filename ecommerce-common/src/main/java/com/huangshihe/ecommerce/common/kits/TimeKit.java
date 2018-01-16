package com.huangshihe.ecommerce.common.kits;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类.
 * <p>
 * Create Date: 2018-01-15 17:11
 *
 * @author huangshihe
 */
public class TimeKit {
    /**
     * 最完整的格式.
     */
    private static final SimpleDateFormat _completePattern = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 时间戳转为最完整格式时间.
     *
     * @param timestamp 时间戳
     * @return 完整格式时间
     */
    public static String toCompleteDate(long timestamp) {
        Date date = new Date(timestamp);
        return _completePattern.format(date);
    }

    /**
     * 时间戳转为最完整格式时间.
     *
     * @param timestamp 时间戳
     * @return 完整格式时间
     */
    public static String toCompleteDate(String timestamp) {
        if (StringKit.isNotEmpty(timestamp)) {
            long lt = new Long(timestamp.trim());
            return toCompleteDate(lt);
        }
        return StringKit.emptyString;
    }


}
