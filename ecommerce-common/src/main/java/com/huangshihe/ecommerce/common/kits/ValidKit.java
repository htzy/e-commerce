package com.huangshihe.ecommerce.common.kits;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 校验工具类.
 * <p>
 * Create Date: 2018-01-02 22:26
 *
 * @author huangshihe
 */
public final class ValidKit {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidKit.class);

    /**
     * 匹配data数据中的dataField项，是否存在于param数据的paramField数据项中，field中数据以,分割，
     * 若field中的数据有相同的则返回true，否则返回false.
     *
     * @param jsonData   给定数据的json字符串
     * @param dataField  数据项
     * @param jsonParam  参数数据的json字符串
     * @param paramField 数据项
     * @return 是否存在
     */
    public static boolean in(String jsonData, String dataField, String jsonParam, String paramField) {
        LOGGER.debug("jsonData:{}, dataField:{}, jsonParam:{}, paramField:{}",
                jsonData, dataField, jsonParam, paramField);
        String dataFieldValueStr = JsonKit.getValueFromJson(jsonData, dataField);
        String paramFieldValueStr = JsonKit.getValueFromJson(jsonParam, paramField);
        // 如果参数中的数据项为空，或者不存在该数据项，则返回false
        if (StringKit.isEmpty(paramFieldValueStr)) {
            LOGGER.warn("jsonParam:{}, paramField:{}, paramFieldValueStr:{}",
                    jsonParam, paramField, paramFieldValueStr);
            return false;
        }
        if (StringKit.isNotEmpty(dataFieldValueStr)) {
            String[] dataFieldValues = dataFieldValueStr.split(",");
            String[] paramFieldValues = paramFieldValueStr.split(",");
            for (String dataFieldValue : dataFieldValues) {
                for (String paramFieldValue : paramFieldValues) {
                    if (dataFieldValue.equals(paramFieldValue)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断在data中的dataField项的值，是否在参数的start数据项和stop数据项之间（两边闭区间）.
     *
     * @param jsonData        给定数据的json字符串
     * @param dataField       数据项
     * @param jsonParam       参数数据的json字符串
     * @param startParamField 起始数据项
     * @param stopParamField  终止数据项
     * @return 数据项的值是否在起始数据项值和终止数据项值之间
     */
    public static boolean between(String jsonData, String dataField, String jsonParam, String startParamField,
                                  String stopParamField) {

        String dataFieldValueStr = JsonKit.getValueFromJson(jsonData, dataField);
        LOGGER.debug("jsonData:{}, dataField:{}, dataFieldValueStr:{}",
                jsonData, dataField, dataFieldValueStr);
        // 如果data中的dataField项的值为null，则返回不包含
        if (StringKit.isEmpty(dataFieldValueStr)) {
            LOGGER.warn("contain empty. jsonData:{}, dataField:{}, dataFieldValueStr:{}",
                    jsonData, dataField, dataFieldValueStr);
            return false;
        }

        JsonNode paramNode = JsonKit.strToTree(jsonParam);
        String startParamValueStr = JsonKit.getValue(paramNode, startParamField);
        String stopParamValueStr = JsonKit.getValue(paramNode, stopParamField);

        LOGGER.debug("jsonParam:{}, startParamField:{}, startParamValueStr:{}",
                jsonParam, startParamField, startParamValueStr);
        LOGGER.debug("jsonParam:{}, stopParamField:{}, stopParamValueStr:{}",
                jsonParam, stopParamField, stopParamValueStr);
        // 如果参数中的start和stop都为空，则返回false
        if (StringKit.isAllEmpty(startParamValueStr, stopParamValueStr)) {
            LOGGER.warn("may contain empty? jsonParam:{}, startParamField:{}, startParamValueStr:{}",
                    jsonParam, startParamField, startParamValueStr);
            LOGGER.warn("may contain empty? jsonParam:{}, stopParamField:{}, stopParamValueStr:{}",
                    jsonParam, stopParamField, stopParamValueStr);
            return false;
        }

        try {
            int dataValue = StringKit.toInt(dataFieldValueStr);
            // 如果start为空，则默认值为负无穷；若stop为空，则默认值为正无穷
            int startValue = StringKit.toInt(startParamValueStr, Integer.MIN_VALUE);
            int stopValue = StringKit.toInt(stopParamValueStr, Integer.MAX_VALUE);
            return dataValue >= startValue && dataValue <= stopValue;
        } catch (NumberFormatException e) {
            LOGGER.error("dataFieldValueStr:{} between startParamValueStr:{} and stopParamValueStr:{}, error:{}",
                    dataFieldValueStr, startParamValueStr, stopParamValueStr, e);
        }
        return false;
    }

    /**
     * 私有构造方法.
     */
    private ValidKit() {

    }
}
