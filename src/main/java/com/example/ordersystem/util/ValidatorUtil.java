package com.example.ordersystem.util;

public class ValidatorUtil {

  private ValidatorUtil() {
  }

  public static boolean isNotBlank(String value) {
    return value != null && !value.trim().isEmpty();
  }

  public static boolean isValidEmail(String email) {
    return isNotBlank(email) && email.contains("@") && email.contains(".");
  }

  public static boolean isValidPhone(String phone) {
    return isNotBlank(phone) && phone.trim().length() >= 7;
  }

  public static boolean isPositiveNumber(double value) {
    return value > 0;
  }

  public static boolean isValidPassword(String password) {
    return isNotBlank(password) && password.length() >= 3;
  }

  public static boolean isValidStatus(String status) {
    if (!isNotBlank(status)) {
      return false;
    }

    return status.equals("NEW")
        || status.equals("IN_PROGRESS")
        || status.equals("DONE")
        || status.equals("CANCELLED");
  }
}