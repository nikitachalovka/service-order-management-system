package com.example.ordersystem.model;

public class Order {

  private int id;
  private String clientName;
  private String serviceName;
  private String status;
  private String createdAt;
  private double totalPrice;

  public Order(int id, String clientName, String serviceName, String status, String createdAt, double totalPrice) {
    this.id = id;
    this.clientName = clientName;
    this.serviceName = serviceName;
    this.status = status;
    this.createdAt = createdAt;
    this.totalPrice = totalPrice;
  }

  public int getId() {
    return id;
  }

  public String getClientName() {
    return clientName;
  }

  public String getServiceName() {
    return serviceName;
  }

  public String getStatus() {
    return status;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public double getTotalPrice() {
    return totalPrice;
  }
}