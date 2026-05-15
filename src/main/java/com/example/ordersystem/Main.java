package com.example.ordersystem;

import com.example.ordersystem.config.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage stage) throws Exception {

    DatabaseInitializer.initDatabase();

    FXMLLoader loader =
        new FXMLLoader(
            getClass().getResource(
                "/fxml/login.fxml"
            )
        );

    Scene scene =
        new Scene(loader.load(), 800, 600);

    stage.setMinWidth(800);
    stage.setMinHeight(600);
    stage.centerOnScreen();

    scene.getStylesheets().add(
        getClass()
            .getResource("/styles/style.css")
            .toExternalForm()
    );

    stage.setTitle("Service Order System");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}