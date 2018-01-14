package com.huangshihe.ecommerce.hbasegui;

import com.huangshihe.ecommerce.common.kits.DigitKit;
import com.huangshihe.ecommerce.common.kits.StringKit;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 主方法.
 * <p>
 * Create Date: 2018-01-13 17:08
 *
 * @author huangshihe
 */
public class Main extends Application {

    /**
     * stage.
     */
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("HBase小工具");
        initRootLayout();
    }


    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        draw(grid);

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * 绘制.
     *
     * @param grid pane
     */
    private void draw(GridPane grid) {
        Text title = new Text("翻译从HBase shell查询的结果");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title, 0, 0, 2, 1);
        Label asciiLabel = new Label("16进制&ASCII：");
        grid.add(asciiLabel, 0, 1);

        TextField asciiField = new TextField();
        grid.add(asciiField, 1, 1);

        Label tenValueLabel = new Label("十进制：");
        grid.add(tenValueLabel, 0, 2);

        TextField tenValueField = new TextField();
        grid.add(tenValueField, 1, 2);

        Button btn = new Button("get");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        btn.setOnAction(e -> {
            String asciiFieldText = asciiField.getText();
            if (StringKit.isNotEmpty(asciiFieldText)) {
                int result = DigitKit.fromHexStr(asciiFieldText);
                tenValueField.setText(String.valueOf(result));
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}