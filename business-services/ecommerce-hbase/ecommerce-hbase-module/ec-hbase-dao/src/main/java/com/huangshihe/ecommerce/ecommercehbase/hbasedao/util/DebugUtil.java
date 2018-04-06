package com.huangshihe.ecommerce.ecommercehbase.hbasedao.util;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 调试工具类.
 * <p>
 * Create Date: 2017-12-18 20:15
 *
 * @author huangshihe
 */
public final class DebugUtil {


    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DebugUtil.class);

    /**
     * 私有构造方法.
     */
    private DebugUtil() {

    }

    /**
     * 打印Cells.
     *
     * @param cells cells
     */
    public static void printCells(final List<Cell> cells) {
        for (final Cell cell : cells) {
            printCell(cell);
        }
    }

    /**
     * 打印Cell.
     *
     * @param cell cell
     */
    public static void printCell(final Cell cell) {
        LOGGER.debug(cellInfo(cell));
    }

    /**
     * Cell 信息.
     *
     * @param cell cell
     * @return cell info
     */
    public static String cellInfo(final Cell cell) {
        return Bytes.toString(CellUtil.cloneFamily(cell)) +
                ":" +
                Bytes.toString(CellUtil.cloneQualifier(cell)) +
                "->" +
                Bytes.toString(CellUtil.cloneValue(cell));
    }

    /**
     * debug用：打印Result中的信息：1.rowKey；2.families；3.qualifiers；4.values
     * TODO 移到DebugUtil
     *
     * @param result result
     */
    public static void printResultInfo(final Result result) {
        LOGGER.debug("begin to print result: {}", result);
        if (result == null) {
            LOGGER.warn("0. result is null");
        } else {
            LOGGER.debug("1. rowKey: {}, result.size: {}", Bytes.toString(result.getRow()), result.size());
            for (Cell cell : result.listCells()) {
                LOGGER.debug("2. cell-family: {}, cell-qualifier: {}, cell-values: {}",
                        Bytes.toString(CellUtil.cloneFamily(cell)),
                        Bytes.toString(CellUtil.cloneQualifier(cell)),
                        Bytes.toString(CellUtil.cloneValue(cell)));
            }
        }
        LOGGER.debug("end print result.");
    }

    /**
     * debug用：打印多个Result中的信息：1.rowKey；2.families；3.qualifiers；4.values
     * TODO 移到DebugUtil
     *
     * @param results results
     */
    public static void printResultsInfo(final List<Result> results) {
        LOGGER.debug("begin to print results: {}", results);
        if (results == null) {
            LOGGER.warn("0. results is null");
        } else {
            for (Result result : results) {
                printResultInfo(result);
            }
        }
        LOGGER.debug("end print results");
    }

}
