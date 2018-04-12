package com.huangshihe.ecommerce.common.kits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 文件工具类.
 * <p>
 * Create Date: 2018-03-03 22:15
 *
 * @author huangshihe
 */
public class FileKit {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileKit.class);

    /**
     * 获取绝对路径.
     *
     * @param folderName 文件夹名/文件名
     * @return 绝对路径
     */
    public static String getAbsolutePath(String folderName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(folderName);
        if (url == null) {
            LOGGER.warn("给定的文件夹路径不存在");
            return null;
        }
        return url.getPath();
    }

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

    /**
     * 覆盖式复制文件.
     *
     * @param inputStream 源文件
     * @param target      目标文件
     */
    public static void copyOrReplace(InputStream inputStream, File target) {
        if (inputStream == null || target == null) {
            return;
        }
        try {
            Files.copy(inputStream, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.error("copyOrReplace failed! inputStream:{}, target:{}, detail:{}", inputStream, target, e);
            throw new IllegalArgumentException("copy failed!");
        }

    }

    /**
     * 获取文件inputStream.
     *
     * @param fileName 文件名
     * @return stream
     */
    public static InputStream getStream(String fileName) {
        if (StringKit.isNotEmpty(fileName)) {
            try {
                return new FileInputStream(fileName);
            } catch (FileNotFoundException e) {
                LOGGER.error("get stream failed! may the file not exists! fileName:{}, detail:{}", fileName, e);
            }
        }
        return null;
    }

    /**
     * 解压配置类型的jar包，并删除META-INF。
     *
     * @param sourceJarFile jar源文件
     * @param targetDir     目标目录
     * @throws IOException 文件异常
     */
    public static void uncompressConfigJar(File sourceJarFile, File targetDir) throws IOException {
        if (sourceJarFile == null || targetDir == null) {
            return;
        } else if (!sourceJarFile.getName().endsWith("jar")) {
            return;
        }
        JarFile jarFile = new JarFile(sourceJarFile);
        Enumeration enumEntry = jarFile.entries();
        while (enumEntry.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumEntry.nextElement();
            File targetFile = new File(targetDir, jarEntry.getName());
            if (jarEntry.getName().contains("META-INF")) {
                continue;
            }
            if (!jarEntry.isDirectory()) {
                // 复制文件
                copyOrReplace(jarFile.getInputStream(jarEntry), targetFile);
            } else {
                // 创建目录
                // 检查目录是否存在
                if (!targetFile.exists()) {
                    if (!targetFile.mkdirs()) {
                        LOGGER.error("create dir fail! sourceJarFile:{}, targetDir:{}, targetFile:{}",
                                sourceJarFile, targetDir, targetFile);
                    }
                }
            }
        }
    }

    @Deprecated
    public static void main(String[] args) {
        String jar = "/Users/huangshihe/.m2/repository/com/huangshihe/ecommerce/ecommerce-spark/ec-spark-common-config/0.0.1/ec-spark-common-config-0.0.1.jar";
        String target = "/usr/local/opt/ecommerce/tmp";
        try {
            uncompressConfigJar(new File(jar), new File(target));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
