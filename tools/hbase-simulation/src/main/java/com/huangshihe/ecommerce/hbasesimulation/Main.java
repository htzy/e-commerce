package com.huangshihe.ecommerce.hbasesimulation;

import com.huangshihe.ecommerce.common.kits.FileKit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main extends Application {

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
        // TODO 这里需要设置配置文件路径
        String path = FileKit.getAbsolutePath("./data");
        // 加载配置文件
        List<File> configs = FileKit.getAllFiles(path, "(.*)\\.properties");
        // 将配置文件名作为item放入checkbox中待选

        controller.buildItems(configs);

        // 都在controller中做：// 设置"启动"事件 // 读取配置文件

    }

    public static void main(String[] args) {
        launch(args);
    }
}
