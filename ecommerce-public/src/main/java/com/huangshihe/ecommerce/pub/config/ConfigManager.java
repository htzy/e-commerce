package com.huangshihe.ecommerce.pub.config;

import com.huangshihe.ecommerce.common.kits.FileKit;
import com.huangshihe.ecommerce.common.kits.StringKit;
import com.huangshihe.ecommerce.pub.constants.ConfigConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置管理类.
 * <p>
 * Create Date: 2018-02-26 21:20
 *
 * @author huangshihe
 */
public class ConfigManager {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);

    /**
     * 存放所有的配置信息.
     */
    private static ConcurrentHashMap<String, Config> _map = new ConcurrentHashMap<String, Config>();

    /**
     * 私有的构造方法.
     */
    private ConfigManager() {
        // 使用单例，将加载配置放入构造方法，作为初始化任务，如果没有执行到该ConfigManager，那么将不会执行初始化任务。
        // 如果主动执行，如果配置文件出现问题？获取失败了，如何重新加载处理？如果配置文件过多，而只有启动前期使用过，但长期占用内存，却无法释放？
        // 归放到Main包中
    }

    private static ConfigManager _configManager = new ConfigManager();

    public static ConfigManager getInstance() {
        return _configManager;
    }

    /**
     * 初始化.
     */
    public void init() {
        List<File> list = FileKit.getAllFiles(ConfigConstant.CONFIG_FILE_DIR, ConfigConstant.CONFIG_FILE_PATTERN);
        if (list != null) {
            for (File file : list) {
                load(file.getAbsolutePath());
            }
        }
    }

    /**
     * 通过文件名加载配置文件到map.
     *
     * @param fileName 配置文件名
     * @return 配置信息类
     */
    private Config load(final String fileName) {
        LOGGER.info("loading config, filename:{}", fileName);
        // 这里不需要另外加"同步"，也不需要"两步检查"，computeIfAbsent中已完成。
        Config result = _map.computeIfAbsent(fileName, key -> new Config((fileName), Config.ConfigType.FILETYPE));
        LOGGER.info("loaded config, filename:{}", fileName);
        return result;
    }

    /**
     * 根据文件名获取配置类.
     *
     * @param fileName 文件名
     * @return 配置信息
     */
    public Config getConfig(final String fileName) {
        Config conf = _map.get(fileName);
        if (conf == null) {
            conf = load(fileName);
        }
        return conf;
    }

    /**
     * 根据文件夹名获取配置类.
     *
     * @param dirName 文件夹名
     * @return 配置对象
     */
    public List<Config> getConfigs(final String dirName) {
        List<Config> list = new ArrayList<Config>();
        if (StringKit.isNotEmpty(dirName)) {
            for (Map.Entry<String, Config> item : _map.entrySet()) {
                if (item.getKey().toLowerCase().contains(dirName.toLowerCase())) {
                    list.add(item.getValue());
                }
            }
        }

        return list;
    }

    /**
     * 获取配置线程池配置.
     *
     * @return 线程池配置
     */
    public List<Config> getThreadPoolConfig() {
        return getConfigs(ConfigConstant.THREAD_POOL_CONFIG_DIR);
    }

}
