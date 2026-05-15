package com.example.ordersystem.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

  private static final String URL =
      "jdbc:h2:./database/ordersdb;AUTO_SERVER=TRUE";

  private DatabaseManager() {
  }

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, "sa", "");
  }
}