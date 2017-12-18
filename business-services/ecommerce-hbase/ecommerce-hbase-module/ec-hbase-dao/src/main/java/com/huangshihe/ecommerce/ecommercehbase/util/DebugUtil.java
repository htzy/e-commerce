package com.huangshihe.ecommerce.ecommercehbase.util;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 工具类
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
        final String builder = Bytes.toString(cell.getFamilyArray()) +
                ":" +
                Bytes.toString(cell.getQualifierArray()) +
                "->" +
                Bytes.toString(cell.getValueArray());
        LOGGER.debug(builder);
    }
}
