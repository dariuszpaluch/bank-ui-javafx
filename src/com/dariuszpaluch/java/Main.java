package com.dariuszpaluch.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main extends Application {

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;

    public static void main(String[] args) {
        Application.launch(args);
        //        launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(Main.class);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main_layout.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Bank by Dariusz Paluch");
        primaryStage.setScene(new Scene(rootNode, 800, 700));
        primaryStage.setResizable(true);
        primaryStage.show();

//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main_layout.fxml"));
//        primaryStage.setTitle("Bank by Dariusz Paluch");
//        primaryStage.setScene(new Scene(root, 500, 400));
//        primaryStage.setResizable(true);
//        primaryStage.show();
    }
//



    @Override
    public void stop() throws Exception {
        springContext.close();
    }
}
