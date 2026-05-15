package com.example.ordersystem.controller;

import com.example.ordersystem.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

  @FXML
  private TextField usernameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private Label errorLabel;

  private final AuthService authService =
      new AuthService();

  @FXML
  public void handleLogin() {

    String role =
        authService.login(
            usernameField.getText(),
            passwordField.getText()
        );

    if (role != null) {

      openDashboard(role);

    } else {

      errorLabel.setText(
          "Неправильний логін або пароль"
      );
    }
  }

  @FXML
  public void openRegister() {

    try {

      FXMLLoader loader =
          new FXMLLoader(
              getClass().getResource(
                  "/fxml/register.fxml"
              )
          );

      Scene scene =
          new Scene(loader.load(), 800, 600);

      scene.getStylesheets().add(
          getClass()
              .getResource("/styles/style.css")
              .toExternalForm()
      );

      Stage stage =
          (Stage) usernameField
              .getScene()
              .getWindow();

      stage.setScene(scene);
      stage.centerOnScreen();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void openDashboard(String role) {

    try {

      FXMLLoader loader =
          new FXMLLoader(
              getClass().getResource(
                  "/fxml/dashboard.fxml"
              )
          );

      Scene scene =
          new Scene(loader.load(), 1000, 600);

      scene.getStylesheets().add(
          getClass()
              .getResource("/styles/style.css")
              .toExternalForm()
      );

      DashboardController dashboardController =
          loader.getController();

      dashboardController.setUserRole(role);

      Stage stage =
          (Stage) usernameField
              .getScene()
              .getWindow();

      stage.setScene(scene);
      stage.centerOnScreen();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}