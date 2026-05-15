package com.example.ordersystem.controller;

import com.example.ordersystem.model.Client;
import com.example.ordersystem.model.Order;
import com.example.ordersystem.model.ServiceEntity;
import com.example.ordersystem.repository.ClientRepository;
import com.example.ordersystem.repository.OrderRepository;
import com.example.ordersystem.repository.ServiceRepository;
import com.example.ordersystem.util.ValidatorUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class DashboardController {

  @FXML private TextField nameField;
  @FXML private TextField phoneField;
  @FXML private TextField emailField;
  @FXML private TextField clientSearchField;

  @FXML private TableView<Client> clientTable;
  @FXML private TableColumn<Client, Integer> idColumn;
  @FXML private TableColumn<Client, String> nameColumn;
  @FXML private TableColumn<Client, String> phoneColumn;
  @FXML private TableColumn<Client, String> emailColumn;

  @FXML private TextField serviceNameField;
  @FXML private TextField serviceDescriptionField;
  @FXML private TextField servicePriceField;

  @FXML private TableView<ServiceEntity> serviceTable;
  @FXML private TableColumn<ServiceEntity, Integer> serviceIdColumn;
  @FXML private TableColumn<ServiceEntity, String> serviceNameColumn;
  @FXML private TableColumn<ServiceEntity, String> serviceDescriptionColumn;
  @FXML private TableColumn<ServiceEntity, Double> servicePriceColumn;

  @FXML private ComboBox<Client> orderClientComboBox;
  @FXML private ComboBox<ServiceEntity> orderServiceComboBox;
  @FXML private TextField orderStatusField;
  @FXML private TextField orderQuantityField;
  @FXML private TextField orderSearchField;

  @FXML private TableView<Order> orderTable;
  @FXML private TableColumn<Order, Integer> orderIdColumn;
  @FXML private TableColumn<Order, String> orderClientColumn;
  @FXML private TableColumn<Order, String> orderServiceColumn;
  @FXML private TableColumn<Order, String> orderStatusColumn;
  @FXML private TableColumn<Order, String> orderDateColumn;
  @FXML private TableColumn<Order, Double> orderTotalColumn;

  @FXML private Button updateClientButton;
  @FXML private Button deleteClientButton;
  @FXML private Button addServiceButton;
  @FXML private Button updateServiceButton;
  @FXML private Button deleteServiceButton;
  @FXML private Button deleteOrderButton;

  private final ClientRepository clientRepository = new ClientRepository();
  private final ServiceRepository serviceRepository = new ServiceRepository();
  private final OrderRepository orderRepository = new OrderRepository();

  private final ObservableList<Client> clients = FXCollections.observableArrayList();
  private final ObservableList<ServiceEntity> services = FXCollections.observableArrayList();
  private final ObservableList<Order> orders = FXCollections.observableArrayList();

  @FXML
  public void initialize() {

    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
    emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

    serviceIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    serviceNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    serviceDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    servicePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    orderClientColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
    orderServiceColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
    orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    orderTotalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

    clientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selectedClient) -> {
      if (selectedClient != null) {
        nameField.setText(selectedClient.getName());
        phoneField.setText(selectedClient.getPhone());
        emailField.setText(selectedClient.getEmail());
      }
    });

    serviceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selectedService) -> {
      if (selectedService != null) {
        serviceNameField.setText(selectedService.getName());
        serviceDescriptionField.setText(selectedService.getDescription());
        servicePriceField.setText(String.valueOf(selectedService.getPrice()));
      }
    });

    setupClientComboBox();
    setupServiceComboBox();

    loadClients();
    loadServices();
    loadOrders();
  }

  public void setUserRole(String role) {

    if ("USER".equals(role)) {
      updateClientButton.setVisible(false);
      deleteClientButton.setVisible(false);

      addServiceButton.setVisible(false);
      updateServiceButton.setVisible(false);
      deleteServiceButton.setVisible(false);

      deleteOrderButton.setVisible(false);
    }
  }

  private void setupClientComboBox() {

    orderClientComboBox.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(Client item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty || item == null ? null : item.getName());
      }
    });

    orderClientComboBox.setButtonCell(new ListCell<>() {
      @Override
      protected void updateItem(Client item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty || item == null ? null : item.getName());
      }
    });
  }

  private void setupServiceComboBox() {

    orderServiceComboBox.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(ServiceEntity item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty || item == null ? null : item.getName());
      }
    });

    orderServiceComboBox.setButtonCell(new ListCell<>() {
      @Override
      protected void updateItem(ServiceEntity item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty || item == null ? null : item.getName());
      }
    });
  }

  @FXML
  private void handleSearchClients() {

    String keyword = clientSearchField.getText();

    clients.clear();

    if (keyword == null || keyword.isBlank()) {
      clients.addAll(clientRepository.findAll());
    } else {
      clients.addAll(clientRepository.search(keyword));
    }

    clientTable.setItems(clients);
  }

  @FXML
  private void handleSearchOrders() {

    String keyword = orderSearchField.getText();

    orders.clear();

    if (keyword == null || keyword.isBlank()) {
      orders.addAll(orderRepository.findAll());
    } else {
      orders.addAll(orderRepository.search(keyword));
    }

    orderTable.setItems(orders);
  }

  @FXML
  private void handleAddClient() {

    if (!isClientInputValid()) {
      return;
    }

    Client client = new Client(
        nameField.getText(),
        phoneField.getText(),
        emailField.getText()
    );

    clientRepository.save(client);
    loadClients();
    clearClientFields();
  }

  @FXML
  private void handleUpdateClient() {

    Client selectedClient = clientTable.getSelectionModel().getSelectedItem();

    if (selectedClient == null) {
      showAlert("Error", "Select client");
      return;
    }

    if (!isClientInputValid()) {
      return;
    }

    selectedClient.setName(nameField.getText());
    selectedClient.setPhone(phoneField.getText());
    selectedClient.setEmail(emailField.getText());

    clientRepository.update(selectedClient);
    loadClients();
    clearClientFields();
  }

  @FXML
  private void handleDeleteClient() {

    Client selectedClient = clientTable.getSelectionModel().getSelectedItem();

    if (selectedClient == null) {
      showAlert("Error", "Select client");
      return;
    }

    clientRepository.delete(selectedClient.getId());
    loadClients();
    clearClientFields();
  }

  @FXML
  private void handleAddService() {

    Double price = getValidServicePrice();

    if (price == null) {
      return;
    }

    ServiceEntity service = new ServiceEntity(
        serviceNameField.getText(),
        serviceDescriptionField.getText(),
        price
    );

    serviceRepository.save(service);
    loadServices();
    clearServiceFields();
  }

  @FXML
  private void handleUpdateService() {

    ServiceEntity selectedService = serviceTable.getSelectionModel().getSelectedItem();

    if (selectedService == null) {
      showAlert("Error", "Select service");
      return;
    }

    Double price = getValidServicePrice();

    if (price == null) {
      return;
    }

    selectedService.setName(serviceNameField.getText());
    selectedService.setDescription(serviceDescriptionField.getText());
    selectedService.setPrice(price);

    serviceRepository.update(selectedService);
    loadServices();
    clearServiceFields();
  }

  @FXML
  private void handleDeleteService() {

    ServiceEntity selectedService = serviceTable.getSelectionModel().getSelectedItem();

    if (selectedService == null) {
      showAlert("Error", "Select service");
      return;
    }

    serviceRepository.delete(selectedService.getId());
    loadServices();
    clearServiceFields();
  }

  @FXML
  private void handleAddOrder() {

    Client selectedClient = orderClientComboBox.getSelectionModel().getSelectedItem();
    ServiceEntity selectedService = orderServiceComboBox.getSelectionModel().getSelectedItem();

    if (selectedClient == null) {
      showAlert("Validation Error", "Select client");
      return;
    }

    if (selectedService == null) {
      showAlert("Validation Error", "Select service");
      return;
    }

    if (!ValidatorUtil.isValidStatus(orderStatusField.getText())) {
      showAlert("Validation Error", "Status must be NEW, IN_PROGRESS, DONE or CANCELLED");
      return;
    }

    int quantity;

    try {
      quantity = Integer.parseInt(orderQuantityField.getText());
    } catch (NumberFormatException e) {
      showAlert("Validation Error", "Quantity must be number");
      return;
    }

    if (quantity <= 0) {
      showAlert("Validation Error", "Quantity must be greater than 0");
      return;
    }

    orderRepository.save(
        selectedClient.getId(),
        selectedService.getId(),
        orderStatusField.getText(),
        quantity,
        selectedService.getPrice()
    );

    loadOrders();
    clearOrderFields();
  }

  @FXML
  private void handleDeleteOrder() {

    Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();

    if (selectedOrder == null) {
      showAlert("Error", "Select order");
      return;
    }

    orderRepository.delete(selectedOrder.getId());
    loadOrders();
    clearOrderFields();
  }

  private boolean isClientInputValid() {

    if (!ValidatorUtil.isNotBlank(nameField.getText())) {
      showAlert("Validation Error", "Client name is required");
      return false;
    }

    if (!ValidatorUtil.isValidPhone(phoneField.getText())) {
      showAlert("Validation Error", "Phone must contain at least 7 characters");
      return false;
    }

    if (!ValidatorUtil.isValidEmail(emailField.getText())) {
      showAlert("Validation Error", "Email must be valid");
      return false;
    }

    return true;
  }

  private Double getValidServicePrice() {

    if (!ValidatorUtil.isNotBlank(serviceNameField.getText())
        || !ValidatorUtil.isNotBlank(serviceDescriptionField.getText())
        || !ValidatorUtil.isNotBlank(servicePriceField.getText())) {

      showAlert("Validation Error", "Fill all service fields");
      return null;
    }

    double price;

    try {
      price = Double.parseDouble(servicePriceField.getText());
    } catch (NumberFormatException e) {
      showAlert("Validation Error", "Price must be number");
      return null;
    }

    if (!ValidatorUtil.isPositiveNumber(price)) {
      showAlert("Validation Error", "Price must be greater than 0");
      return null;
    }

    return price;
  }

  private void loadClients() {

    clients.clear();
    clients.addAll(clientRepository.findAll());
    clientTable.setItems(clients);

    if (orderClientComboBox != null) {
      orderClientComboBox.setItems(clients);
    }
  }

  private void loadServices() {

    services.clear();
    services.addAll(serviceRepository.findAll());
    serviceTable.setItems(services);

    if (orderServiceComboBox != null) {
      orderServiceComboBox.setItems(services);
    }
  }

  private void loadOrders() {

    orders.clear();
    orders.addAll(orderRepository.findAll());
    orderTable.setItems(orders);
  }

  private void clearClientFields() {

    nameField.clear();
    phoneField.clear();
    emailField.clear();
    clientTable.getSelectionModel().clearSelection();
  }

  private void clearServiceFields() {

    serviceNameField.clear();
    serviceDescriptionField.clear();
    servicePriceField.clear();
    serviceTable.getSelectionModel().clearSelection();
  }

  private void clearOrderFields() {

    orderClientComboBox.getSelectionModel().clearSelection();
    orderServiceComboBox.getSelectionModel().clearSelection();
    orderStatusField.clear();
    orderQuantityField.clear();
  }

  private void showAlert(String title, String message) {

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}