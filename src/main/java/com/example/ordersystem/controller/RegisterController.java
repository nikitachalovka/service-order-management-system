package com.example.ordersystem.controller;

import com.example.ordersystem.service.AuthService;
import com.example.ordersystem.service.EmailService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

  @FXML private TextField usernameField;
  @FXML private TextField emailField;
  @FXML private PasswordField passwordField;
  @FXML private TextField codeField;

  @FXML private Label usernameError;
  @FXML private Label emailError;
  @FXML private Label passwordError;
  @FXML private Label codeError;
  @FXML private Label errorLabel;

  private final AuthService authService = new AuthService();
  private final EmailService emailService = new EmailService();

  private String generatedCode;

  @FXML
  public void handleSendCode() {
    clearErrors();

    String email = emailField.getText();

    if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
      emailError.setText("Неправильний формат пошти");
      addErrorStyle(emailField);
      return;
    }

    generatedCode =
        String.valueOf((int) (100000 + Math.random() * 900000));

    boolean sent =
        emailService.sendCode(email, generatedCode);

    if (sent) {
      errorLabel.setText("Код надіслано на пошту");
    } else {
      emailError.setText("Не вдалося надіслати код");
      addErrorStyle(emailField);
    }
  }

  @FXML
  public void handleRegister() {
    clearErrors();

    boolean valid = true;

    String username = usernameField.getText();
    String email = emailField.getText();
    String password = passwordField.getText();
    String code = codeField.getText();

    if (username == null || username.trim().length() < 3) {
      usernameError.setText("Ім'я має містити мінімум 3 символи");
      addErrorStyle(usernameField);
      valid = false;
    }

    if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
      emailError.setText("Неправильний формат пошти");
      addErrorStyle(emailField);
      valid = false;
    }

    if (password == null || password.length() < 6) {
      passwordError.setText("Пароль має містити мінімум 6 символів");
      addErrorStyle(passwordField);
      valid = false;
    }

    if (generatedCode == null || generatedCode.isBlank()) {
      codeError.setText("Спочатку натисніть Get Code");
      addErrorStyle(codeField);
      valid = false;
    } else if (code == null || !code.equals(generatedCode)) {
      codeError.setText("Невірний код підтвердження");
      addErrorStyle(codeField);
      valid = false;
    }

    if (!valid) {
      return;
    }

    boolean success =
        authService.register(username, email, password);

    if (success) {
      openLogin();
    } else {
      errorLabel.setText(
          "Не вдалося створити користувача. Перевір консоль IntelliJ."
      );
    }
  }

  @FXML
  public void openLogin() {
    try {
      FXMLLoader loader =
          new FXMLLoader(getClass().getResource("/fxml/login.fxml"));

      Scene scene =
          new Scene(loader.load(), 800, 600);

      scene.getStylesheets().add(
          getClass().getResource("/styles/style.css").toExternalForm()
      );

      Stage stage =
          (Stage) usernameField.getScene().getWindow();

      stage.setScene(scene);
      stage.centerOnScreen();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void clearErrors() {
    usernameError.setText("");
    emailError.setText("");
    passwordError.setText("");
    codeError.setText("");
    errorLabel.setText("");

    removeErrorStyle(usernameField);
    removeErrorStyle(emailField);
    removeErrorStyle(passwordField);
    removeErrorStyle(codeField);
  }

  private void addErrorStyle(TextField field) {
    if (!field.getStyleClass().contains("input-error")) {
      field.getStyleClass().add("input-error");
    }
  }

  private void removeErrorStyle(TextField field) {
    field.getStyleClass().remove("input-error");
  }
}