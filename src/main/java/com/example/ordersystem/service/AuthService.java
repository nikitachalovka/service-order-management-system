package com.example.ordersystem.service;

import com.example.ordersystem.config.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

  public String login(String username, String password) {

    String sql = """
        SELECT *
        FROM users
        WHERE username = ? OR email = ?
        """;

    try (
        Connection connection =
            DatabaseManager.getConnection();

        PreparedStatement statement =
            connection.prepareStatement(sql)
    ) {

      statement.setString(1, username);
      statement.setString(2, username);

      ResultSet resultSet =
          statement.executeQuery();

      if (resultSet.next()) {

        String hashedPassword =
            resultSet.getString("password");

        if (hashedPassword != null
            && BCrypt.checkpw(password, hashedPassword)) {

          return resultSet.getString("role");
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public boolean register(
      String username,
      String email,
      String password
  ) {

    if (userExists(username, email)) {
      return false;
    }

    String hashedPassword =
        BCrypt.hashpw(password, BCrypt.gensalt());

    String sql = """
        INSERT INTO users
        (username, email, password, role)
        VALUES (?, ?, ?, ?)
        """;

    try (
        Connection connection =
            DatabaseManager.getConnection();

        PreparedStatement statement =
            connection.prepareStatement(sql)
    ) {

      statement.setString(1, username);
      statement.setString(2, email);
      statement.setString(3, hashedPassword);
      statement.setString(4, "USER");

      statement.executeUpdate();

      return true;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean userExists(
      String username,
      String email
  ) {

    String sql = """
        SELECT *
        FROM users
        WHERE username = ?
           OR email = ?
        """;

    try (
        Connection connection =
            DatabaseManager.getConnection();

        PreparedStatement statement =
            connection.prepareStatement(sql)
    ) {

      statement.setString(1, username);
      statement.setString(2, email);

      ResultSet resultSet =
          statement.executeQuery();

      return resultSet.next();

    } catch (Exception e) {

      e.printStackTrace();

      return false;
    }
  }
}