package com.huangshihe.ecommerce.ecommercehbase.util;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.protobuf.generated.ClientProtos;
import org.apache.hadoop.hbase.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
            LOGGER.debug("return null!");
            return null;
        } else {
            // listCells 可能为null
            LOGGER.debug("return listCell: {}, isEmpty:{}", result.listCells(), result.isEmpty()); //NOPMD
            return result.listCells(); //NOPMD
        }
    }

    /**
     * Scan转为String，转换进制
     *
     * @param scan scan
     * @throws IOException 网络或文件错误
     */
    public static String convertScanToString(final Scan scan) throws IOException {
        LOGGER.debug("begin to convert scan to String");
        ClientProtos.Scan proto = ProtobufUtil.toScan(scan);
        String scanToString = Base64.encodeBytes(proto.toByteArray());
        LOGGER.debug("end to convert");
        return scanToString;
    }


    /**
     * 私有构造方法.
     */
    private HBaseDaoUtil() {

    }
}
