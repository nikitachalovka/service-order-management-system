CREATE TABLE IF NOT EXISTS users (
                                     id INTEGER AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS clients (
                                       id INTEGER AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS services (
                                        id INTEGER AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
    description VARCHAR(500) NOT NULL,
    price DOUBLE NOT NULL
    );

CREATE TABLE IF NOT EXISTS orders (
                                      id INTEGER AUTO_INCREMENT PRIMARY KEY,
                                      client_id INTEGER NOT NULL,
                                      status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_price DOUBLE NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS order_services (
                                              id INTEGER AUTO_INCREMENT PRIMARY KEY,
                                              order_id INTEGER NOT NULL,
                                              service_id INTEGER NOT NULL,
                                              quantity INTEGER NOT NULL,
                                              price_at_moment DOUBLE NOT NULL,
                                              FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE
    );