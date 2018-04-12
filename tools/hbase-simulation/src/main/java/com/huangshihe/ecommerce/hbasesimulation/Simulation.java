package com.huangshihe.ecommerce.hbasesimulation;

import com.huangshihe.ecommerce.common.configs.SimpleConfig;
import com.huangshihe.ecommerce.common.kits.DigitKit;
import com.huangshihe.ecommerce.common.kits.StringKit;
import org.apache.hadoop.hbase.util.Bytes;
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

    public Simulation(LocalDate begin, LocalDate end, SimpleConfig config) {
        _begin = new Date(begin.getYear(), begin.getMonthValue() - 1, begin.getDayOfMonth());
        _end = new Date(end.getYear(), end.getMonthValue() - 1, end.getDayOfMonth());

        _config = config;

        build();
    }

    private void build() {
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

    @Deprecated
    private String getIdentity(String str) {
        if (StringKit.isEmpty(str)) {
            LOGGER.error("get identity failed! str:{}", str);
            return null;
        }
        String[] results = str.split(".");
        if (results.length != 2) {
            LOGGER.error("get identity failed! str:{}", str);
            return null;
        }
        return results[1];
    }


    /**
     * 生成一条以,分割的模拟数据.
     *
     * @return 模拟数据
     */
    public String simulate(Date currentTime) {
        StringBuilder sb = new StringBuilder();

        _rowkey.forEach(record -> record.setCurrentTime(currentTime));
        _qualifier.forEach(record -> record.setCurrentTime(currentTime));

        // 获取rowkey
        List<byte[]> rowkeyList = _rowkey.stream()
                .sorted(Comparator.comparingInt(Record::getIndex))
                .map(Record::getRandomValue)
                .collect(Collectors.toList());

        StringBuilder rowkey = new StringBuilder();
        for (byte[] b : rowkeyList) {
            rowkey.append(Arrays.toString(b));
        }
        sb.append(rowkey);

        // 获取qualifier
        List<byte[]> qualifierList = _qualifier.stream()
                .sorted(Comparator.comparingInt(Record::getIndex))
                .map(Record::getRandomValue)
                .collect(Collectors.toList());

        StringBuilder qualifier = new StringBuilder();
        for (byte[] b : qualifierList) {
            qualifier.append(Arrays.toString(b));
        }
        sb.append(qualifier);

        return sb.toString();
    }

    /**
     * 生成多条模拟数据.
     *
     * @param count 条数
     * @return 模拟数据
     */
    public String[] toSimulate(int count) {
        // 模拟时间，5秒5秒的过
        String[] results = new String[count];
        for (int i = 0; i < count; i++) {
            results[i] = simulate(new Date(_begin.getTime() + i * 5000));
        }
        return results;
    }
}

class Record {

    private static final Logger LOGGER = LoggerFactory.getLogger(Record.class);

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

    public byte[] getRandomValue() {
        byte[] result = new byte[0];

        switch (_type) {
            case HASHCODE: {
                if (_range == 0) {
                    result = new byte[_len];
                } else {
                    result = Bytes.toBytes(_random.nextInt(_range));
                    result = DigitKit.adjustLen(result, _len);
                }
            }
            break;
            case MAC: {
                if (_range == 0) {
                    result = DigitKit.adjustLen(result, _len, (byte) 'F');
                } else {
                    result = Bytes.toBytes(_random.nextInt(_range));
                    result = DigitKit.adjustLen(result, _len, (byte) 'F');
                }
            }
            break;
            case TIME: {
                // 传进来的时间Str取小时
                int currentHour = _currentTime.getHours();
                // 如果该小时在List里，那么生成数据，
                if (_timeRangeSet.contains(currentHour)) {
                    result = Bytes.toBytes(_currentTime.getTime());
                    result = DigitKit.adjustLen(result, _len);
                } else {
                    // 若不在List里，则生成范围为24的随机数，如果生成的随机数在范围里，那么则放当前时间；否则返回空
                    int randomHour = _random.nextInt(24);
                    if (_timeRangeSet.contains(randomHour)) {
                        result = Bytes.toBytes(_currentTime.getTime());
                        result = DigitKit.adjustLen(result, _len);
                    } else {
                        result = DigitKit.adjustLen(result, _len);
                    }
                }
            }
            break;
            case NUM: {
                result = Bytes.toBytes(_random.nextInt(_range));
            }
            break;
            case _NUM: {
                result = Bytes.toBytes(0 - _random.nextInt(_range));
            }
            break;
            case BOOL: {
                if (_random.nextInt(2) == 1) {
                    result = new byte[]{1};
                } else {
                    result = new byte[]{0};
                }
            }
        }
        return result;
    }


}


enum RecordType {

    HASHCODE("hashcode"), MAC("mac"), TIME("time"), NUM("num"), _NUM("_num"), BOOL("bool");

    private String _name;

    RecordType(String name) {
        this._name = name;
    }

}