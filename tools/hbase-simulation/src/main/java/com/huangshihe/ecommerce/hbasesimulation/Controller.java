package com.huangshihe.ecommerce.hbasesimulation;

import com.csvreader.CsvWriter;
import com.huangshihe.ecommerce.common.configs.SimpleConfig;
import com.huangshihe.ecommerce.common.constants.Constants;
import com.huangshihe.ecommerce.common.kits.DigitKit;
import com.huangshihe.ecommerce.common.kits.FileKit;
import com.huangshihe.ecommerce.common.kits.TimeKit;
import com.huangshihe.ecommerce.ecommercehbase.hbasedao.dao.HBaseDaoImpl;
import com.huangshihe.ecommerce.ecommercehbase.hbasedao.dao.IHBaseDao;
import com.huangshihe.ecommerce.ecommercehbase.hbasedao.manager.HBaseConnectionManager;
import com.huangshihe.ecommerce.ecommercehbase.hbaseservice.constants.CommonConstant;
import com.huangshihe.ecommerce.ecommercehbase.hbaseservice.constants.OriginalConstant;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;
import org.apache.hadoop.hbase.util.Pair;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.hadoop.mapred.FileOutputFormat;

@SuppressWarnings("unchecked")
public class Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    @FXML
    private ChoiceBox configCb;

    @FXML
    private DatePicker beginTime;

    @FXML
    private DatePicker endTime;

    @FXML
    private Button activeButton;

    @FXML
    private TextField recordCount;

    @FXML
    private Button createHFileButton;

    private Map<String, SimpleConfig> _map = new HashMap<>();

    private Simulation _simulation;

    @FXML
    private void initialize() {

        recordCount.textProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("observable:{}, oldValue:{}, newValue:{}", observable, oldValue, newValue);
            if (!DigitKit.isTenNum(newValue)) {
                LOGGER.warn("输入的不是数字！{}", recordCount.getText());
                recordCount.setText(oldValue);
            } else {
                try {
                    int value = Integer.parseInt(newValue);
                } catch (NumberFormatException e) {
                    LOGGER.warn("不支持这么大的数字！{}", recordCount.getText());
                    recordCount.setText(oldValue);
                }
            }
        });
    }

    @FXML
    private void onActive() {
        LOGGER.debug("active...");
        // 禁用"启动"键
        activeButton.setDisable(true);
        String configName = (String) configCb.getValue();
        SimpleConfig config = _map.get(configName);
        LOGGER.debug("config:{}", config);
        if (configName != null && DigitKit.isTenNum(recordCount.getText())) {
            // 创建新的线程，将耗时任务放到子线程中执行
            Thread thread = new Thread(() -> {
                // 按照配置文件生成数据到csv
                LOGGER.debug("begin to simulation data...");

                _simulation = new Simulation(beginTime.getValue(), endTime.getValue(), config);

                int count = Integer.valueOf(recordCount.getText());
                // List结果为：rowkey+qualifier组成的pair，其中rowkey/qualifier的key为明文，value为byte数组
                List<Pair<Pair<String, String>, Pair<String, String>>> result = _simulation.toSimulate(count);
                // 生成的是：byte数组，如：[0,1,2,-1]
                // 将rowkey的key和qualifier的key写到csv文件中
                saveToCsv(result);
                // 这里将内容写到文件，另外再开一个线程去写HFile文件
                saveToDat(result);

                LOGGER.debug("end simulation data...");
                //执行完毕，将"启动"键恢复
                activeButton.setDisable(false);
            });

            thread.start();
        }
    }

    @FXML
    private void createHFiles() {
        LOGGER.debug("sync to HBase...");
        // 暂时需要先执行"启动"生成数据之后，才能导入数据
        if (_simulation == null) {
            LOGGER.warn("当前需要先'启动'生成数据后，才能导入数据！");
            return;
        } else {
            // 将生成的配置传到类变量，供HFileCreate中的qualifier生成

            // 设置HFileCreate中的配置
            HFileCreate.buildSimulation(_simulation);

        }

        // 禁用该键
        createHFileButton.setDisable(true);
        // 创建新的线程，将耗时任务放到子线程中运行
        Thread thread = new Thread(() -> {
            LOGGER.debug("begin create HFiles...");
            // 连接HBase
            Configuration conf = HBaseConnectionManager.getInstance().getNewConfiguration();
            // 这里如何获取dao对象？？？？？？？？
            /////////////////////////////////
            IHBaseDao hbaseDao = new HBaseDaoImpl();
            /////////////////////////////////

            // 获取所有需要保存到HFile为文件
            for (File datFile : FileKit.getAllFiles(Constants.SIMULATION_HBASE_DIR, "(.*)\\.dat")) {
                // tableName，表名：表名前缀+文件名（去后缀）
                String tableName = OriginalConstant.TABLE_NAME_PRE + FileKit.getFileNameStr(datFile);
                String datFilePath = datFile.getAbsolutePath();
                // 运行前，删除已存在的中间输出目录
                try {
                    LOGGER.debug("simulation file dir:{}", Constants.SIMULATION_HFILE_DIR);
                    FileSystem fs = FileSystem.get(URI.create(Constants.SIMULATION_HFILE_DIR), conf);

                    fs.delete(new Path(Constants.SIMULATION_HFILE_DIR), true);
                    fs.close();
                } catch (IOException e) {
                    LOGGER.error("io exception... detail:{}", e);
                    throw new IllegalArgumentException(e);
                }

                // 如果表不存在，创建表
                if (!hbaseDao.isExists(tableName)) {
                    hbaseDao.createTable(tableName, CommonConstant.FAMILY_NAME, OriginalConstant.TTL);
                }
                // 获取表
                Table table = hbaseDao.getTable(tableName);

                // 生成Job，注意以下的包别导错！
                try {

                    ///////////////////////////
                    // 通过调试，发现默认添加的序列器只有三个，这里手动添加其他的
                    // 其中Put.class用的是org.apache.hadoop.hbase.mapreduce.MutationSerialization，否则将报空指针
                    conf.set("io.serializations","org.apache.hadoop.io.serializer.JavaSerialization,"
                            + "org.apache.hadoop.io.serializer.WritableSerialization,"
                            + "org.apache.hadoop.hbase.mapreduce.KeyValueSerialization,"
                            + "org.apache.hadoop.hbase.mapreduce.MutationSerialization,"
                            + "org.apache.hadoop.hbase.mapreduce.ResultSerialization"
                    );
                    ///////////////////////////

                    Job job = Job.getInstance(conf, "create HFile");
                    job.setJarByClass(Controller.class);
                    // 设置mapper类
                    job.setMapperClass(HFileCreate.HFileImportMapper2.class);
                    // 这里没有用到reduce，否则这里也需设置reducer类

                    // 设置输出的key和value类
                    job.setMapOutputKeyClass(ImmutableBytesWritable.class);
                    job.setMapOutputValueClass(Put.class);

                    // speculation
                    job.setSpeculativeExecution(false);
                    job.setReduceSpeculativeExecution(false);

                    // in/out format
                    job.setInputFormatClass(TextInputFormat.class);
                    job.setOutputFormatClass(HFileOutputFormat2.class);

                    // 设置输入/输出路径
                    FileInputFormat.setInputPaths(job, datFilePath);
                    FileOutputFormat.setOutputPath(job, new Path(Constants.SIMULATION_HFILE_DIR));

                    HFileOutputFormat2.configureIncrementalLoadMap(job, table);

                    // 设置等待
                    job.waitForCompletion(true);
                } catch (IOException e) {
                    LOGGER.error("io exception... detail:{}", e);
                    throw new IllegalArgumentException(e);
                } catch (InterruptedException e) {
                    LOGGER.error("interrupted exception... detail:{}", e);
                    throw new IllegalArgumentException(e);
                } catch (ClassNotFoundException e) {
                    LOGGER.error("class not found! detail:{}", e);
                    throw new IllegalArgumentException(e);
                } catch (Exception e) {
                    LOGGER.error("unknown error, detail:{}", e);
                    throw new IllegalArgumentException(e);
                } finally {
                    try {
                        if (table != null) {
                            table.close();
                        }
                    } catch (IOException e) {
                        LOGGER.error("close table error!");
                    }
                }


                // TODO 将HFile 导入到HBase表中

            }
            LOGGER.debug("end create HFiles...");
            // 执行完毕，将按键恢复
            createHFileButton.setDisable(false);
        });
        thread.start();

    }


    /**
     * 将生成的明文数据写入到文件.
     *
     * @param list 生成的数据
     */
    private void saveToCsv(List<Pair<Pair<String, String>, Pair<String, String>>> list) {
        if (list == null) {
            return;
        }
        // 如果月份和日子不是两位，则需要加前缀0
        String fileName = TimeKit.toDateStr(beginTime.getValue().getYear(),
                beginTime.getValue().getMonthValue(), beginTime.getValue().getDayOfMonth()) + ".csv";

        // 创建文件，如果文件已存在，这里不会删除原文件，但是writer会覆盖写！
        FileKit.createFile(Constants.SIMULATION_DIR, fileName);

        CsvWriter writer = new CsvWriter(Constants.SIMULATION_DIR + fileName, ',', Charset.forName("UTF-8"));
        try {
            for (Pair<Pair<String, String>, Pair<String, String>> pair : list) {
                writer.writeRecord(new String[]{pair.getFirst().getFirst(), pair.getSecond().getFirst()});
            }
//            writer.flush();// 加上会报空指针
        } catch (IOException e) {
            LOGGER.error("io exception... detail:{}", e);
            throw new IllegalArgumentException(e);
        } finally {
            writer.close();
        }
    }


    /**
     * 将生成的byte数据写到文件.
     *
     * @param list 生成的数据
     */
    private void saveToDat(List<Pair<Pair<String, String>, Pair<String, String>>> list) {
        if (list == null) {
            return;
        }
        // 如果月份和日子不是两位，则需要加前缀0
        String fileName = TimeKit.toDateStr(beginTime.getValue().getYear(),
                beginTime.getValue().getMonthValue(), beginTime.getValue().getDayOfMonth()) + ".dat";

        // 创建文件，如果文件已存在，这里不会删除原文件，但是writer会覆盖写！
        FileKit.createFile(Constants.SIMULATION_HBASE_DIR, fileName);

        CsvWriter writer = new CsvWriter(Constants.SIMULATION_HBASE_DIR + fileName, ',', Charset.forName("UTF-8"));
        try {
            for (Pair<Pair<String, String>, Pair<String, String>> pair : list) {
                writer.writeRecord(new String[]{pair.getFirst().getSecond(), pair.getSecond().getSecond()});
            }
//            writer.flush();// 加上会报空指针
        } catch (IOException e) {
            LOGGER.error("io exception... detail:{}", e);
            throw new IllegalArgumentException(e);
        } finally {
            writer.close();
        }
    }


    public void buildItems(List<File> configs) {
        List<Object> fileNames = new ArrayList<>(configs.size());
        for (File config : configs) {

            String fileName = config.getName();
            fileNames.add(fileName);
            try {
                _map.put(fileName, new SimpleConfig(new FileInputStream(config)));
            } catch (FileNotFoundException e) {
                LOGGER.debug("文件未找到！");
                throw new IllegalArgumentException("Properties file not found in classpath", e);
            }
        }
        configCb.setItems(FXCollections.observableList(fileNames));
    }


}
