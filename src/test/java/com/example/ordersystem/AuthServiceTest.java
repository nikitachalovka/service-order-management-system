package com.example.ordersystem;

import org.junit.jupiter.api.Test;
import com.example.ordersystem.service.AuthService;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

  @Test
  void loginShouldReturnTrueForAdmin() {

    AuthService authService =
        new AuthService();

    boolean result =
        authService.login("admin", "admin");

    assertTrue(result);
  }

  @Test
  void loginShouldReturnFalseForInvalidUser() {

    AuthService authService =
        new AuthService();

    boolean result =
        authService.login("wrong", "wrong");

    assertFalse(result);
  }
}