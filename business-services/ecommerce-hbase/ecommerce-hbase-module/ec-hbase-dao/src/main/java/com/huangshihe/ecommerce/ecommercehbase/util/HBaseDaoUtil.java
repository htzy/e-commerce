package com.huangshihe.ecommerce.ecommercehbase.util;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
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
     * debug用：打印Result中的信息：1.rowKey；2.families；3.qualifiers；4.values
     *
     * @param result result
     */
    public static void printResultInfo(final Result result) {
        LOGGER.debug("[printResultInfo] begin to print result: {}", result);
        if (result == null) {
            LOGGER.warn("[printResultInfo] 0. result is null");
        } else {
            LOGGER.debug("[printResultInfo] 1. rowKey: {}, result.size: {}", Bytes.toString(result.getRow()), result.size());
            for (Cell cell : result.listCells()) {
                LOGGER.debug("[printResultInfo] 2. cell-family: {}, cell-qualifier: {}, cell-values: {}",
                        Bytes.toString(CellUtil.cloneFamily(cell)),
                        Bytes.toString(CellUtil.cloneQualifier(cell)),
                        Bytes.toString(CellUtil.cloneValue(cell)));
            }
        }
        LOGGER.debug("[printResultInfo] end print result.");
    }

    /**
     * debug用：打印多个Result中的信息：1.rowKey；2.families；3.qualifiers；4.values
     *
     * @param results results
     */
    public static void printResultsInfo(final List<Result> results) {
        LOGGER.debug("[printResultsInfo] begin to print results: {}", results);
        if (results == null) {
            LOGGER.warn("[printResultsInfo] 0. results is null");
        } else {
            for (Result result : results) {
                printResultInfo(result);
            }
        }
        LOGGER.debug("[printResultsInfo] end print results");
    }


    /**
     * 私有构造方法.
     */
    private HBaseDaoUtil() {

    }
}
