package com.huangshihe.ecommerce.ecommercehbase.util;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * HBaseDao工具类.
 * <p>
 * Create Date: 2017-12-18 20:56
 *
 * @author huangshihe
 */
public final class HBaseDaoUtil {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseDaoUtil.class);

    /**
     * 获得Cells.
     *
     * @param result result
     * @return cells
     */
    public static List<Cell> getCells(final Result result) {
        // cell-key: rowKey + cf + column + version; cell-value: value
        if (result == null) {
            LOGGER.debug("[queryTableByRowKey] return null!");
            return null;
        } else {
            // listCells 可能为null
            LOGGER.debug("[queryTableByRowKey] return listCell: {}, isEmpty:{}", result.listCells(), result.isEmpty()); //NOPMD
            return result.listCells(); //NOPMD
        }
    }


    /**
     * 私有构造方法.
     */
    private HBaseDaoUtil() {

    }
}
