package com.huangshihe.ecommerce.hbasesimulation;

import com.huangshihe.ecommerce.common.kits.FileKit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * stage.
     */
    private Stage primaryStage;

    private GridPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        initRootLayout();
        primaryStage.setTitle("模拟数据生成器");
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    private void initRootLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/sample.fxml"));

        rootLayout = loader.load();

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);

        // 获取controller
        Controller controller = loader.getController();
        LOGGER.debug("currentPath:{}", FileKit.getCurrentPath());
        String path = FileKit.getCurrentPath() + "/simulation-data/";
        // 如果路径不存在，则使用resources下的配置文件
        if (!FileKit.isExists(path)) {
            LOGGER.debug("simulation-data is missing, use inner data...");
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("data/origin.properties");

            if (in != null) {
                // 将内部文件拷到外面
                File tmp = new File("simulation-data/origin.properties");
                FileKit.copyOrReplace(in, tmp);
                controller.buildItems(Collections.singletonList(tmp));
            } else {
                LOGGER.error("data in jar is missing...");
                throw new IllegalArgumentException("data in jar is missing...");
            }
        } else {
            // 加载配置文件
            List<File> configs = FileKit.getAllFiles(path, "(.*)\\.properties");
            // 将配置文件名作为item放入checkbox中待选
            controller.buildItems(configs);
        }


        // 都在controller中做：// 设置"启动"事件 // 读取配置文件

    }

    public static void main(String[] args) {
        launch(args);
    }
}
