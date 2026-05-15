package com.example.ordersystem.repository;

import com.example.ordersystem.config.DatabaseManager;
import com.example.ordersystem.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository {

  public void save(Client client) {
    String sql = "INSERT INTO clients(name, phone, email) VALUES(?,?,?)";

    try (
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setString(1, client.getName());
      statement.setString(2, client.getPhone());
      statement.setString(3, client.getEmail());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void update(Client client) {
    String sql = "UPDATE clients SET name=?, phone=?, email=? WHERE id=?";

    try (
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setString(1, client.getName());
      statement.setString(2, client.getPhone());
      statement.setString(3, client.getEmail());
      statement.setInt(4, client.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void delete(int id) {
    String sql = "DELETE FROM clients WHERE id=?";

    try (
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setInt(1, id);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Client> findAll() {
    List<Client> clients = new ArrayList<>();
    String sql = "SELECT * FROM clients";

    try (
        Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql)
    ) {
      while (resultSet.next()) {
        clients.add(
            new Client(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("phone"),
                resultSet.getString("email")
            )
        );
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return clients;
  }

  public List<Client> search(String keyword) {
    List<Client> clients = new ArrayList<>();

    String sql = """
        SELECT *
        FROM clients
        WHERE name LIKE ?
           OR phone LIKE ?
           OR email LIKE ?
        """;

    try (
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      String searchValue = "%" + keyword + "%";

      statement.setString(1, searchValue);
      statement.setString(2, searchValue);
      statement.setString(3, searchValue);

      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        clients.add(
            new Client(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("phone"),
                resultSet.getString("email")
            )
        );
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return clients;
  }
}