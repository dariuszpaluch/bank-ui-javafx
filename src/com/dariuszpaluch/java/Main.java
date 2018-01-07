package com.dariuszpaluch.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main_layout.fxml"));
        primaryStage.setTitle("Bank by Dariusz Paluch");
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.setResizable(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
