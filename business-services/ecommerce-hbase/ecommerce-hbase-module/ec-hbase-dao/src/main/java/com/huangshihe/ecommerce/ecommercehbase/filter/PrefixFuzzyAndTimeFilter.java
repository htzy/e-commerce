package com.huangshihe.ecommerce.ecommercehbase.filter;

import com.huangshihe.ecommerce.common.kits.DigitKit;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.filter.FilterBase;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 前缀模糊和时间范围过滤器。
 * 结构为：前n的字节模糊匹配，后跟时间范围匹配。
 * <p>
 * Create Date: 2018-01-24 20:29
 *
 * @author huangshihe
 */
public class PrefixFuzzyAndTimeFilter extends FilterBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrefixFuzzyAndTimeFilter.class);

    private int prefixFuzzyLength = 0;
    private long startTimeStamp = 0;
    private long stopTimeStamp = 0;

    private boolean filterRow = true;

    /**
     * map中存放需要读的行RowKey
     */
    public Map<Object, Object> map = new HashMap<Object, Object>();

    public PrefixFuzzyAndTimeFilter(int prefixFuzzyLength, long startTimeStamp, long stopTimeStamp) {
        this.prefixFuzzyLength = prefixFuzzyLength;
        this.startTimeStamp = startTimeStamp;
        this.stopTimeStamp = stopTimeStamp;
    }

    /**
     * Filters that are purely stateless and do nothing in their reset() methods can inherit
     * this null/empty implementation.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void reset() throws IOException {
        filterRow = true;
    }

    // TODO ?????原意是解析rowkey，如果rowkey不满足要求，则把整个rowkey干掉，而这里实际是Cell，会不会影响性能？
    @Override
    public ReturnCode filterKeyValue(Cell ignored) throws IOException {
        byte[] row = CellUtil.cloneRow(ignored);

        // time: \x00\x00\x00\x00ZE\xC8L 十进制：1514522700
        byte[] time = Bytes.copy(row, prefixFuzzyLength, 8);
        LOGGER.debug("time:{}", time);
        String timeStr = Bytes.toString(time);
        LOGGER.debug("timeStr:{}", timeStr);
        long timeStamp = DigitKit.fromHexStr(Bytes.toString(time));
        if (timeStamp >= startTimeStamp && timeStamp <= stopTimeStamp) {
            filterRow = false;
        }
        return ReturnCode.INCLUDE;
    }

    /**
     * Filters that never filter by rows based on previously gathered state from
     * {@link #filterKeyValue(Cell)} can inherit this implementation that
     * never filters a row.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public boolean filterRow() throws IOException {
        return filterRow;
    }

    /////////////////////////////////////////////////////////////////

    /**
     * Filters that do not filter by row key can inherit this implementation that
     * never filters anything. (ie: returns false).
     * <p>
     * {@inheritDoc}
     *
     * @param buffer
     * @param offset
     * @param length
     */
    @Override
    public boolean filterRowKey(byte[] buffer, int offset, int length) throws IOException {
        // false保留，true丢弃
        LOGGER.debug("buffer to Str: {}, offset:{}, length:{}", Bytes.toString(buffer), offset, length);
        return super.filterRowKey(buffer, offset, length);
    }


}
