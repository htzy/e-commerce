package com.huangshihe.ecommerce.ecommercehbase.hbaseservice.constants;

/**
 * 原始表常量类.
 * <p>
 * Create Date: 2018-03-19 19:22
 *
 * @author huangshihe
 */
public class OriginalConstant {

    public static final String TABLE_NAME_PRE = "t_performance_";

    /**
     * ttl为一周，即数据保存一周，7天，ps:单位为秒。
     */
    public static final int TTL = 7 * 24 * 60 * 60;


    // ----------qualifier info: name, offset and len---------------

    /**
     * ssid
     */
    public static final String SSID = "6";

    /**
     * ssid offset
     */
    public static final int SSID_OFFSET = 0;

    /**
     * ssid len
     */
    public static final int SSID_LEN = 6;

    /**
     * type 终端类型
     */
    public static final String TYPE = "7";

    /**
     * type offset
     */
    public static final int TYPE_OFFSET = SSID_OFFSET + SSID_LEN;

    /**
     * type len
     */
    public static final int TYPE_LEN = 4;

    /**
     * channel 信道
     */
    public static final String CHANNEL = "8";

    /**
     * channel offset
     */
    public static final int CHANNEL_OFFSET = TYPE_OFFSET + TYPE_LEN;

    /**
     * channel len
     */
    public static final int CHANNEL_LEN = 4;

    /**
     * Rssi 场强
     */
    public static final String RSSI = "9";

    /**
     * Rssi offset
     */
    public static final int RSSI_OFFSET = CHANNEL_OFFSET + CHANNEL_LEN;

    /**
     * Rssi len
     */
    public static final int RSSI_LEN = 4;

    /**
     * detailType 终端具体类型
     */
    public static final String DETAIL_TYPE = "A";

    /**
     * detailType offset
     */
    public static final int DETAIL_TYPE_OFFSET = RSSI_OFFSET + RSSI_LEN;

    /**
     * detailType len
     */
    public static final int DETAIL_TYPE_LEN = 4;

    /**
     * deleteFlag 老化标识
     */
    public static final String DELETE_FLAG = "B";

    /**
     * deleteFlag offset
     */
    public static final int DELETE_FLAG_OFFSET = DETAIL_TYPE_OFFSET + DETAIL_TYPE_LEN;

    /**
     * deleteFlag len
     */
    public static final int DELETE_FLAG_LEN = 1;

    /**
     * isAssociate 设备是否通过认证
     */
    public static final String IS_ASSOCIATED = "C";

    /**
     * isAssociated offset
     */
    public static final int IS_ASSOCIATED_OFFSET = DELETE_FLAG_OFFSET + DELETE_FLAG_LEN;

    /**
     * isAssociated len
     */
    public static final int IS_ASSOICATED_LEN = 1;


}
