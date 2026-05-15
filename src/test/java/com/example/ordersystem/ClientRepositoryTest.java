package com.example.ordersystem;

import com.example.ordersystem.model.Client;
import com.example.ordersystem.repository.ClientRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClientRepositoryTest {

  @Test
  void saveClientShouldWork() {

    ClientRepository repository =
        new ClientRepository();

    Client client =
        new Client(
            "Test User",
            "+380000000000",
            "test@gmail.com"
        );

    repository.save(client);

    List<Client> clients =
        repository.findAll();

    boolean exists =
        clients.stream()
            .anyMatch(c ->
                c.getEmail().equals("test@gmail.com")
            );

    assertTrue(exists);
  }

  @Test
  void findAllShouldReturnClients() {

    ClientRepository repository =
        new ClientRepository();

    List<Client> clients =
        repository.findAll();

    assertNotNull(clients);
  }
}