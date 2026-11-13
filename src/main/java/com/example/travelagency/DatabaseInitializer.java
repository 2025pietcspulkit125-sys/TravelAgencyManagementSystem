package com.example.travelagency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public final class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void initialize() throws SQLException {
        try (Connection serverConnection = DatabaseConnection.getServerConnection();
             Statement createDatabase = serverConnection.createStatement()) {
            createDatabase.executeUpdate(
                    "CREATE DATABASE IF NOT EXISTS `" + DatabaseConnection.getDatabaseName() + "`");
        }
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        username VARCHAR(255) NOT NULL UNIQUE,
                        password_hash VARCHAR(512) NOT NULL,
                        salt VARCHAR(512) NOT NULL,
                        role VARCHAR(50) NOT NULL DEFAULT 'admin',
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        is_active BOOLEAN NOT NULL DEFAULT TRUE
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS customers (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        phone VARCHAR(100) NOT NULL,
                        address TEXT NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS trips (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        trip_name VARCHAR(255) NOT NULL,
                        destination VARCHAR(255) NOT NULL,
                        start_date VARCHAR(20) NOT NULL,
                        end_date VARCHAR(20) NOT NULL,
                        price DECIMAL(12, 2) NOT NULL,
                        description TEXT NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS bookings (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        customer_id INT NOT NULL,
                        trip_id INT NOT NULL,
                        booked_by_user_id INT NULL,
                        passenger_count INT NOT NULL DEFAULT 1,
                        status VARCHAR(30) NOT NULL DEFAULT 'CONFIRMED',
                        total_amount DECIMAL(12, 2) NOT NULL,
                        notes TEXT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        CONSTRAINT fk_bookings_customer FOREIGN KEY (customer_id)
                            REFERENCES customers(id) ON UPDATE CASCADE ON DELETE RESTRICT,
                        CONSTRAINT fk_bookings_trip FOREIGN KEY (trip_id)
                            REFERENCES trips(id) ON UPDATE CASCADE ON DELETE RESTRICT,
                        CONSTRAINT fk_bookings_user FOREIGN KEY (booked_by_user_id)
                            REFERENCES users(id) ON UPDATE CASCADE ON DELETE SET NULL
                    )
                    """);
            createIndexIfMissing(connection, "idx_users_active", "users", "is_active");
            createIndexIfMissing(connection, "idx_customers_name", "customers", "name");
            createIndexIfMissing(connection, "idx_trips_destination", "trips", "destination");
            createIndexIfMissing(connection, "idx_bookings_customer", "bookings", "customer_id");
            createIndexIfMissing(connection, "idx_bookings_trip", "bookings", "trip_id");
            createIndexIfMissing(connection, "idx_bookings_status", "bookings", "status");
        }
        UserManager.initializeConfiguredAdmin();
    }

    private static void createIndexIfMissing(
            Connection connection, String indexName, String tableName, String columnName) throws SQLException {
        try (PreparedStatement check = connection.prepareStatement(
                "SELECT COUNT(*) FROM information_schema.statistics " +
                        "WHERE table_schema = DATABASE() AND table_name = ? AND index_name = ?");
             PreparedStatement create = connection.prepareStatement(
                     "CREATE INDEX " + indexName + " ON " + tableName + "(" + columnName + ")")) {
            check.setString(1, tableName);
            check.setString(2, indexName);
            try (ResultSet result = check.executeQuery()) {
                result.next();
                if (result.getInt(1) == 0) {
                    create.executeUpdate();
                }
            }
        }
    }
}
