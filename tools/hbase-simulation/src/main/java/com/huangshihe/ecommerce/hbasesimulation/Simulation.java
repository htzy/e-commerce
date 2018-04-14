package com.huangshihe.ecommerce.hbasesimulation;

import com.huangshihe.ecommerce.common.configs.SimpleConfig;
import com.huangshihe.ecommerce.common.kits.DigitKit;
import com.huangshihe.ecommerce.common.kits.StringKit;
import com.huangshihe.ecommerce.common.kits.TimeKit;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 模拟生成数据.
 * 将明文写到日志系统里，返回值是字节
 * <p>
 * Create Date: 2018-04-11 19:38
 *
 * @author huangshihe
 */
public class Simulation {

    private static final Logger LOGGER = LoggerFactory.getLogger(Simulation.class);

    private Date _begin;

    private Date _end;

    private SimpleConfig _config;

    private List<Record> _rowkey = new ArrayList<>();

    private List<Record> _qualifier = new ArrayList<>();

    private Random _random = new Random();

    public Simulation(LocalDate begin, LocalDate end, SimpleConfig config) {
        _begin = new Date(begin.getYear() - 1900, begin.getMonthValue() - 1, begin.getDayOfMonth());
        _end = new Date(end.getYear() - 1900, end.getMonthValue() - 1, end.getDayOfMonth());

        _config = config;

        buildConfig();
    }

    /**
     * 构建配置.
     */
    private void buildConfig() {
        Properties properties = _config.getProperties();
        Enumeration en = properties.propertyNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            LOGGER.debug("key:{}", key);
            String value = _config.getProperty(key);
            LOGGER.debug("value:{}", value);
            if (key.startsWith("rowkey")) {
                _rowkey.add(new Record(key, value));
            } else if (key.startsWith("qualifier")) {
                _qualifier.add(new Record(key, value));
            }
        }
    }


    /**
     * 生成多条模拟数据.
     *
     * @param count 条数
     * @return 模拟数据
     */
    public List<Pair<Pair<String, String>, Pair<String, String>>> toSimulate(int count) {
        // 模拟时间，5秒5秒的过
        // key为rowkey，value为qualifier
        List<Pair<Pair<String, String>, Pair<String, String>>> pairs = new ArrayList<>(count);
        // i模拟时间，模拟5秒5秒的增加过程，size表示当前数目
        for (int i = 0, size = 0; size < count && i < (_end.getTime() / 1000 - _begin.getTime() / 1000); i++) {
            // 每5秒可能有1000条数据？
            int random = _random.nextInt(1000);
            for (int j = 0; j < random; j++) {
                // 这里删除key为null的pair
                Pair<Pair<String, String>, Pair<String, String>> pair =
                        simulate(new Date(_begin.getTime() + i * 5000));

                if (pair.getFirst() != null && pair.getFirst().getFirst() != null && i < count) {
                    pairs.add(pair);
                    // size为当前条数，即pairs.size()
                    size++;
                }
            }
        }
        return pairs;
    }

    /**
     * 生成一对值，key明文(rowkey+qualifier)，value为byte数组(rowkey+qualifier).
     *
     * @return 模拟数据
     */
    public Pair<Pair<String, String>, Pair<String, String>> simulate(Date currentTime) {
        Pair<Pair<String, String>, Pair<String, String>> result = new Pair<>();

        // 设置当前时间
        _rowkey.forEach(record -> record.setCurrentTime(currentTime));
        _qualifier.forEach(record -> record.setCurrentTime(currentTime));
        // 获取rowkey
        Pair<String, String> rowkey = simulate(_rowkey, '|');
        // 获取qualifier
        Pair<String, String> qualifier = simulate(_qualifier, ',');

        result.setFirst(rowkey);
        result.setSecond(qualifier);
        return result;
    }

    /**
     * @param records records
     * @param c       明文分隔符
     * @return pair
     */
    private Pair<String, String> simulate(List<Record> records, char c) {
        Pair<String, String> result = new Pair<>();

        // 流只能用一次！
        List<Pair<String, byte[]>> list = records.stream()
                .sorted(Comparator.comparingInt(Record::getIndex))
                .map(Record::getRandomValue).collect(Collectors.toList());

        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        for (Pair<String, byte[]> pair : list) {
            // 拼接明文
            key.append(pair.getFirst());
            key.append(c);
            // 拼接byte数组
            value.append(Arrays.toString(pair.getSecond()));
        }
        // 删掉最后一个分隔符
        if (key.length() > 0) {
            key.deleteCharAt(key.length() - 1);
        }
        if (key.toString().contains(Record.ILLEGAL_VALUE)) {
            // 如果有任意匹配到非法值，则返回空
            LOGGER.debug("illegal value, drop it...");
            return null;
        }
        result.setFirst(key.toString());
        result.setSecond(value.toString());
        return result;
    }

}

class Record {

    private static final Logger LOGGER = LoggerFactory.getLogger(Record.class);

    public static final String ILLEGAL_VALUE = "illegal value";

    private String _name;
    private int _index;
    private int _len;
    private int _range;//0即没有范围
    private RecordType _type;
    // 小时为单位
    private Set<Integer> _timeRangeSet;
    private Date _currentTime;

    private Random _random = new Random();

    /**
     * 配置文件的key、value和当前时间
     *
     * @param key   key
     * @param value value
     */
    public Record(String key, String value) {
        LOGGER.debug("new Record: key:{}, value:{}", key, value);
        String[] keys = key.split("\\.");
        LOGGER.debug("keys:{}", Arrays.toString(keys));

        _name = keys[1];
        String[] values = value.split(",");
        _index = Integer.valueOf(values[0]);
        _len = Integer.valueOf(values[1]);
        // 如果是时间，则_range表示的是范围：如：8-12;14-17;

        String range = values[2];
        if (DigitKit.isTenNum(range)) {
            _range = Integer.valueOf(range);
        } else {
            // 如果不是数字，说明是范围，例：9-12;14-17
            _timeRangeSet = getTimeRange(range);
        }

        LOGGER.debug("_type:{}", values[3]);
        _type = RecordType.valueOf(values[3].toUpperCase());
    }

    public void setCurrentTime(Date currentTime) {
        this._currentTime = currentTime;
    }

    /**
     * 这里将时间范围（小时）放到Set里.
     *
     * @param timeRange 时间范围
     * @return set
     */
    private Set<Integer> getTimeRange(String timeRange) {
        Set<Integer> result = new HashSet<>();
        if (StringKit.isEmpty(timeRange)) {
            return result;
        }
        String[] ranges = timeRange.split(";");
        for (String range : ranges) {
            String[] items = range.split("-");
            int begin = Integer.valueOf(items[0]);
            int end = Integer.valueOf(items[1]);
            for (int i = begin; i <= end; i++) {
                result.add(i);
            }
        }
        return result;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public int getIndex() {
        return _index;
    }

    public void setIndex(int index) {
        this._index = index;
    }

    /**
     * 生成当前Record的随机值，key为明文，value为byte数组.
     *
     * @return pair
     */
    public Pair<String, byte[]> getRandomValue() {
        Pair<String, byte[]> result = new Pair<>();
        byte[] bytes = new byte[0];
        String str = "";
        switch (_type) {
            case HASHCODE: {
                if (_range == 0) {
                    bytes = new byte[_len];
                    str = "";
                } else {
                    int value = _random.nextInt(_range);
                    bytes = Bytes.toBytes(value);
                    bytes = DigitKit.adjustLen(bytes, _len);
                    str += value;
                }
            }
            break;
            case MAC: {
                if (_range == 0) {
                    bytes = DigitKit.adjustLen(bytes, _len, (byte) 'F');
                    str = StringKit.fillChar(_len, 'F');
                } else {
                    int value = _random.nextInt(_range);
                    bytes = Bytes.toBytes(value);
                    bytes = DigitKit.adjustLen(bytes, _len, (byte) 'F');
                    str += value;
                    str = StringKit.fillChar(str, _len, 'F');
                }
            }
            break;
            case TIME: {
                // 传进来的时间Str取小时
                int currentHour = _currentTime.getHours();
                // 如果该小时在List里，那么生成数据，
                if (_timeRangeSet.contains(currentHour)) {
                    bytes = Bytes.toBytes(_currentTime.getTime());
                    bytes = DigitKit.adjustLen(bytes, _len);

                    str += TimeKit.toTimeStr(_currentTime);
                } else {
                    // 若不在Set里，则生成范围为24的随机数，如果生成的随机数在范围里，那么则放当前时间；否则返回空
                    int randomHour = _random.nextInt(24);
                    if (_timeRangeSet.contains(randomHour)) {
                        bytes = Bytes.toBytes(_currentTime.getTime());
                        bytes = DigitKit.adjustLen(bytes, _len);
                        str += TimeKit.toTimeStr(_currentTime);
                    } else {
                        bytes = DigitKit.adjustLen(bytes, _len);
                        // 若不在范围里，算为非法值
                        str += ILLEGAL_VALUE;
                    }
                }
            }
            break;
            case NUM: {
                int value = _random.nextInt(_range);
                bytes = Bytes.toBytes(value);
                str += value;
            }
            break;
            case _NUM: {
                int value = _random.nextInt(_range);
                bytes = Bytes.toBytes(0 - value);
                str += (-value);
            }
            break;
            case BOOL: {
                if (_random.nextInt(2) == 1) {
                    bytes = new byte[]{1};
                    str += 1;
                } else {
                    bytes = new byte[]{0};
                    str += 0;
                }
            }
        }
        result.setFirst(str);
        result.setSecond(bytes);
        return result;
    }
}


enum RecordType {

    HASHCODE("hashcode"), MAC("mac"), TIME("time"), NUM("num"), _NUM("_num"), BOOL("bool");

    private String name;

    RecordType(String name) {
        this.name = name;
    }

}