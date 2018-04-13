package com.huangshihe.ecommerce.common.constants;

import java.io.File;

/**
 * 基础配置信息类.
 * <p>
 * Create Date: 2018-04-07 21:02
 *
 * @author huangshihe
 */
public class Constants {

    /**
     * 项目根目录：/usr/local/opt/ecommerce/
     */
    public static final String ROOT_DIR = File.separator + "usr" + File.separator + "local" + File.separator
            + "opt" + File.separator + "ecommerce" + File.separator;

    /**
     * 模拟数据csv格式的保存目录 /usr/local/opt/ecommerce/data/simulation/csv/
     */
    public static final String SIMULATION_DIR = ROOT_DIR + "data" +
            File.separator + "simulation" + File.separator + "csv" + File.separator;
}
