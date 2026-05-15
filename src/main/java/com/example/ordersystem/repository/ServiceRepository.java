package com.example.ordersystem.repository;

import com.example.ordersystem.config.DatabaseManager;
import com.example.ordersystem.model.ServiceEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRepository {

  public void save(ServiceEntity service) {

    String sql =
        "INSERT INTO services(name, description, price) VALUES(?,?,?)";

    try (
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {

      statement.setString(1, service.getName());
      statement.setString(2, service.getDescription());
      statement.setDouble(3, service.getPrice());

      statement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void update(ServiceEntity service) {

    String sql =
        "UPDATE services SET name=?, description=?, price=? WHERE id=?";

    try (
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {

      statement.setString(1, service.getName());
      statement.setString(2, service.getDescription());
      statement.setDouble(3, service.getPrice());
      statement.setInt(4, service.getId());

      statement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void delete(int id) {

    String sql =
        "DELETE FROM services WHERE id=?";

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

  public List<ServiceEntity> findAll() {

    List<ServiceEntity> services =
        new ArrayList<>();

    String sql =
        "SELECT * FROM services";

    try (
        Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql)
    ) {

      while (resultSet.next()) {

        ServiceEntity service =
            new ServiceEntity(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDouble("price")
            );

        services.add(service);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return services;
  }
}