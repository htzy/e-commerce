package com.huangshihe.ecommerce.common.kits;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.NullNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Json工具类.
 * <p>
 * Create Date: 2017-12-11 20:37
 *
 * @author huangshihe
 */
public final class JsonKit {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonKit.class);

    private static ObjectMapper getMapper() {
        final ObjectMapper mapper = new ObjectMapper(); //转换器
        //允许使用未带引号的字段名，如：mapper.readValue(字符串, javabean.class);
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //允许使用单引号
        mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        return mapper;
    }

    /**
     * 对象转字符串.
     *
     * @param object 对象
     * @return json字符串
     */
    public static String objToStr(final Object object) {
        // TODO 这里是否需要判断object为null？
        final ObjectMapper mapper = getMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("object to string fail, object: {}, error: {}", object, e);
            throw new IllegalArgumentException(e);
        }

    }

    /**
     * 字符串转对象.
     *
     * @param json   字符串
     * @param tClass 类
     * @param <T>    类
     * @return 对象
     */
    public static <T> T strToObj(final String json, final Class<T> tClass) {
        // TODO 这里是否需要判断object为null？
        final ObjectMapper mapper = getMapper();
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            LOGGER.error("String to object fail, json: {}, error: {}", json, e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 将字符串转为json对象，也可以将对象转为json对象，然后知道field之后，可直接通过json对象path(field)获取node值.
     *
     * @param json json字符串
     * @return JsonNode，当json为空时，返回NullNode
     */
    public static JsonNode strToTree(final String json) {
        LOGGER.debug("json:{}", json);
        if (StringKit.isEmpty(json)) {
            return NullNode.instance;
        }
        final ObjectMapper mapper = getMapper();
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            LOGGER.error("String to jsonNode fail, json:{}, error: {}", json, e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 将json对象转为字符串
     *
     * @param tree JsonNode
     * @return json字符串
     */
    public static String treeToStr(final JsonNode tree) {
        return null == tree ? null : tree.toString();
    }

    /**
     * 将object转为json对象.
     *
     * @param object 待转对象
     * @return jsonNode
     */
    public static JsonNode objToTree(final Object object) {
        final ObjectMapper mapper = getMapper();
        return mapper.valueToTree(object);
    }

    /**
     * 返回json节点，当遇到null时，返回MissingNode.
     *
     * @param json      json
     * @param dataField 数据项
     * @return json节点
     */
    public static JsonNode getNodeNeverNull(final String json, final String dataField) {
        // 首先要判断strToTree返回的类型是什么？，假设strToTree返回的类型是ObjectNode，
        // 节点为空时：path返回：MissingNode；而get返回null（从LinkedHashMap中获取）
        // 先有的所有JsonNode类型，遇到空节点，这里都将返回MissingNode，值为""
        return strToTree(json).path(dataField);
    }

    /**
     * 返回json节点，当遇到null时，返回MissingNode.
     *
     * @param node      json
     * @param dataField 数据项
     * @return json节点
     */
    public static JsonNode getNodeNeverNull(final JsonNode node, final String dataField) {
        if (null == node) {
            return MissingNode.getInstance();
        } else {
            return node.path(dataField);
        }
    }

    /**
     * 返回json节点，可能是null.
     *
     * @param json      json
     * @param dataField 数据项
     * @return json节点
     */
    public static JsonNode getNodeWithNull(final String json, final String dataField) {
        // 1. 可能json Tree为NullNode，get任何都为空；
        // 2. 可能json Tree不为NullNode，但dataField不存在，因此返回null.
        return strToTree(json).get(dataField);
    }

    /**
     * 返回json节点，可能是null.
     *
     * @param node      json
     * @param dataField 数据项
     * @return json节点
     */
    public static JsonNode getNodeWithNull(final JsonNode node, final String dataField) {
        return null == node ? null : node.get(dataField);
    }


    /**
     * 返回该节点是否为null.
     *
     * @param json      json
     * @param dataField 数据项
     * @return 是否为null
     */
    public static boolean isNull(final String json, final String dataField) {
        return getNodeWithNull(json, dataField) == null;
    }

    /**
     * 从json中读取数据项
     *
     * @param json      json
     * @param dataField 数据项
     * @return 值
     */
    public static String getValueFromJson(final String json, final String dataField) {
        LOGGER.debug("json:{}, dataField:{}", json, dataField);
        return getNodeNeverNull(json, dataField).asText();
    }

    /**
     * 从node中读取值.
     *
     * @param node node
     * @return 值
     */
    public static String getValue(final JsonNode node) {
        if (null == node) {
            return null;
        } else {
            return node.asText();
        }
    }

    /**
     * 从node中读取数据项.
     *
     * @param node      node
     * @param dataField 数据项
     * @return 值
     */
    public static String getValue(final JsonNode node, final String dataField) {
        final JsonNode n = getNodeWithNull(node, dataField);
        return getValue(n);
    }

    /**
     * 私有构造方法.
     */
    private JsonKit() {

    }


}
