package com.example.ordersystem.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {

  public static void initDatabase() {
    executeSqlFile("/db/schema.sql");
    executeSqlFile("/db/data.sql");
  }

  private static void executeSqlFile(String path) {
    try (
        Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement();
        InputStream inputStream = DatabaseInitializer.class.getResourceAsStream(path)
    ) {
      if (inputStream == null) {
        throw new RuntimeException("SQL file not found: " + path);
      }

      String sql = new BufferedReader(
          new InputStreamReader(inputStream, StandardCharsets.UTF_8)
      ).lines().collect(Collectors.joining("\n"));

      for (String command : sql.split(";")) {
        if (!command.trim().isEmpty()) {
          statement.execute(command);
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}