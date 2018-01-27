package com.huangshihe.ecommerce.ecommercehbase.filter;

import com.google.protobuf.InvalidProtocolBufferException;
import com.huangshihe.ecommerce.ecommercehbase.proto.PrefixFuzzyAndTimeFilterProto;
import com.huangshihe.ecommerce.ecommercehbase.util.DebugUtil;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.exceptions.DeserializationException;
import org.apache.hadoop.hbase.filter.FilterBase;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 前缀模糊和时间范围过滤器，包头不包尾.
 * 结构为：前n的字节模糊匹配，后跟时间范围匹配。
 * <p>
 * Create Date: 2018-01-24 20:29
 *
 * @author huangshihe
 */
public class PrefixFuzzyAndTimeFilter extends FilterBase {
    // LOGGER在client端不会打印，因为这是运行在HBase服务端
    private static final Logger LOGGER = LoggerFactory.getLogger(PrefixFuzzyAndTimeFilter.class);

    private int prefixFuzzyLength = 0;
    private long startTimeStamp = 0;
    private long stopTimeStamp = 0;

    private boolean filterRow = true;

    public PrefixFuzzyAndTimeFilter(int prefixFuzzyLength, long startTimeStamp, long stopTimeStamp) {
        LOGGER.debug("prefixFuzzyLength:{}, startTimeStamp:{}, stopTimeStamp:{}",
                prefixFuzzyLength, startTimeStamp, stopTimeStamp);
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
        try {
            byte[] row = CellUtil.cloneRow(ignored);
            // time: [0, 0, 1, 96, -96, -106, 104, -32] 十进制：1514522700000
            byte[] time = Bytes.copy(row, prefixFuzzyLength, 8);
            long timeStamp = Bytes.toLong(time);
            if (timeStamp >= startTimeStamp && timeStamp < stopTimeStamp) {
                filterRow = false;
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error("wrong argument, cell info:{}, detail:{}", DebugUtil.cellInfo(ignored), e);
            filterRow = true;
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


    /**
     * 序列化：由byte转为对象.
     *
     * @param pbBytes row bytes
     * @return 序列化后的row
     * @throws DeserializationException wrong row？
     */
    public static PrefixFuzzyAndTimeFilter parseFrom(final byte[] pbBytes)
            throws DeserializationException {
        PrefixFuzzyAndTimeFilterProto.PrefixFuzzyAndTimeFilter proto;
        try {
            proto = PrefixFuzzyAndTimeFilterProto.PrefixFuzzyAndTimeFilter.parseFrom(pbBytes);
        } catch (InvalidProtocolBufferException e) {
            throw new DeserializationException(e);
        }
        return new PrefixFuzzyAndTimeFilter(proto.getPrefixFuzzyLength(),
                proto.getStartTimeStamp(), proto.getStopTimeStamp());
    }

    /**
     * 序列化用：由对象转为byte.
     *
     * @return byteArray
     */
    public byte[] toByteArray() {
        PrefixFuzzyAndTimeFilterProto.PrefixFuzzyAndTimeFilter.Builder builder =
                PrefixFuzzyAndTimeFilterProto.PrefixFuzzyAndTimeFilter.newBuilder();
        builder.setPrefixFuzzyLength(this.prefixFuzzyLength);
        builder.setStartTimeStamp(this.startTimeStamp);
        builder.setStopTimeStamp(this.stopTimeStamp);
        return builder.build().toByteArray();
    }

    @Override
    public String toString() {
        return "PrefixFuzzyAndTimeFilter{" +
                "prefixFuzzyLength=" + prefixFuzzyLength +
                ", startTimeStamp=" + startTimeStamp +
                ", stopTimeStamp=" + stopTimeStamp +
                ", filterRow=" + filterRow +
                '}';
    }

}
