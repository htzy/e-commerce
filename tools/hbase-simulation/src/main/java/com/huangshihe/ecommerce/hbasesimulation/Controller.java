package com.huangshihe.ecommerce.hbasesimulation;

import com.csvreader.CsvWriter;
import com.huangshihe.ecommerce.common.configs.SimpleConfig;
import com.huangshihe.ecommerce.common.constants.Constants;
import com.huangshihe.ecommerce.common.kits.DigitKit;
import com.huangshihe.ecommerce.common.kits.FileKit;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.apache.hadoop.hbase.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<String, SimpleConfig> _map = new HashMap<>();

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

                Simulation simulation = new Simulation(beginTime.getValue(), endTime.getValue(), config);
                int count = Integer.valueOf(recordCount.getText());
                // List结果为：rowkey+qualifier组成的pair，其中rowkey/qualifier的key为明文，value为byte数组
                List<Pair<Pair<String, String>, Pair<String, String>>> result = simulation.toSimulate(count);
                // 生成的是：byte数组，如：[0,1,2,-1]
                // 将rowkey的key和qualifier的key写到csv文件中
                saveToCsv(result);
                // TODO 增加写到HFile的方法

//                、、或者直接将该byte生成HFile文件

                LOGGER.debug("end simulation data...");
                //执行完毕，将"启动"键恢复
                activeButton.setDisable(false);
            });

            thread.start();
        }
    }

    /**
     * 将生成的数据写入到文件.
     *
     * @param list 生成的数据
     */
    private void saveToCsv(List<Pair<Pair<String, String>, Pair<String, String>>> list) {
        if (list == null) {
            return;
        }
        String fileName = beginTime.getValue().getYear() + "-"
                + beginTime.getValue().getMonthValue() + "-" +
                beginTime.getValue().getDayOfMonth() + ".csv";
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
