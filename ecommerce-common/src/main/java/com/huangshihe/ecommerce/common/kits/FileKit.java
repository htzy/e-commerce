package com.huangshihe.ecommerce.common.kits;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类.
 * <p>
 * Create Date: 2018-03-03 22:15
 *
 * @author huangshihe
 */
public class FileKit {

    /**
     * 读取某文件夹下的所有文件，使用递归方法
     *
     * @param path 文件夹路径
     * @return 文件夹下的所有文件
     */
    public static List<File> getAllFiles(String path) {
        List<File> results = new ArrayList<File>();
        File root = new File(path);
        if (root.exists()) {
            File[] files = root.listFiles();
            // File.listFiles()可能为null，下面可能触发NullPointerException，所以需要提前检查
            if (files != null) {
                // 尘归尘，土归土
                for (File file : files) {
                    if (file.isDirectory()) {
                        results.addAll(getAllFiles(file.getAbsolutePath()));
                    } else {
                        results.add(file);
                    }
                }
            }
        }
        return results;
    }

    /**
     * 实现读取某文件夹下并且文件名符合给定的正则表达式的所有文件 => 常量：符合配置文件后缀的正则表达式：***-cfg.xml
     *
     * @param path    文件夹路径
     * @param pattern 文件名匹配符
     * @return
     */
    public static List<File> getAllFiles(String path, String pattern) {
        List<File> results = new ArrayList<File>();
        File root = new File(path);
        if (root.exists()) {
            File[] files = root.listFiles();
            // File.listFiles()可能为null，下面可能触发NullPointerException，所以需要提前检查
            if (files != null) {
                // 尘归尘，土归土
                for (File file : files) {
                    if (file.isDirectory()) {
                        results.addAll(getAllFiles(file.getAbsolutePath(), pattern));
                    } else {
                        if (file.getName().matches(pattern)) {
                            results.add(file);
                        }
                    }
                }
            }
        }
        return results;
    }

    // 检查文件夹是否存在

    // 检查文件是否存在

    // 新建文件

    // 移动文件（用于部署）

}
