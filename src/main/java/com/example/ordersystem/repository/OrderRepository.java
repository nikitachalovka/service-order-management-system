package com.example.ordersystem.repository;

import com.example.ordersystem.config.DatabaseManager;
import com.example.ordersystem.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

  public void save(int clientId, int serviceId, String status, int quantity, double priceAtMoment) {

    String insertOrderSql =
        "INSERT INTO orders(client_id, status, total_price) VALUES(?,?,?)";

    String insertOrderServiceSql =
        "INSERT INTO order_services(order_id, service_id, quantity, price_at_moment) VALUES(?,?,?,?)";

    double totalPrice = quantity * priceAtMoment;

    try (Connection connection = DatabaseManager.getConnection()) {

      connection.setAutoCommit(false);

      try (
          PreparedStatement orderStatement =
              connection.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS);
          PreparedStatement orderServiceStatement =
              connection.prepareStatement(insertOrderServiceSql)
      ) {

        orderStatement.setInt(1, clientId);
        orderStatement.setString(2, status);
        orderStatement.setDouble(3, totalPrice);
        orderStatement.executeUpdate();

        ResultSet keys = orderStatement.getGeneratedKeys();

        if (keys.next()) {
          int orderId = keys.getInt(1);

          orderServiceStatement.setInt(1, orderId);
          orderServiceStatement.setInt(2, serviceId);
          orderServiceStatement.setInt(3, quantity);
          orderServiceStatement.setDouble(4, priceAtMoment);
          orderServiceStatement.executeUpdate();
        }

        connection.commit();

      } catch (SQLException e) {
        connection.rollback();
        throw e;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void delete(int id) {

    String sql = "DELETE FROM orders WHERE id=?";

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

  public List<Order> findAll() {

    List<Order> orders = new ArrayList<>();

    String sql = """
        SELECT orders.id,
               clients.name AS client_name,
               services.name AS service_name,
               orders.status,
               orders.created_at,
               orders.total_price
        FROM orders
        INNER JOIN clients ON orders.client_id = clients.id
        INNER JOIN order_services ON orders.id = order_services.order_id
        INNER JOIN services ON order_services.service_id = services.id
        ORDER BY orders.id DESC
        """;

    try (
        Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql)
    ) {

      while (resultSet.next()) {
        orders.add(
            new Order(
                resultSet.getInt("id"),
                resultSet.getString("client_name"),
                resultSet.getString("service_name"),
                resultSet.getString("status"),
                resultSet.getString("created_at"),
                resultSet.getDouble("total_price")
            )
        );
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return orders;
  }

  public List<Order> search(String keyword) {

    List<Order> orders = new ArrayList<>();

    String sql = """
        SELECT orders.id,
               clients.name AS client_name,
               services.name AS service_name,
               orders.status,
               orders.created_at,
               orders.total_price
        FROM orders
        INNER JOIN clients ON orders.client_id = clients.id
        INNER JOIN order_services ON orders.id = order_services.order_id
        INNER JOIN services ON order_services.service_id = services.id
        WHERE clients.name LIKE ?
           OR services.name LIKE ?
           OR orders.status LIKE ?
        ORDER BY orders.id DESC
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
        orders.add(
            new Order(
                resultSet.getInt("id"),
                resultSet.getString("client_name"),
                resultSet.getString("service_name"),
                resultSet.getString("status"),
                resultSet.getString("created_at"),
                resultSet.getDouble("total_price")
            )
        );
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return orders;
  }
}