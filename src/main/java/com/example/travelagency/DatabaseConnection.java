package com.example.travelagency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return open(connectionUrl(getDatabaseName()), getUsername(), getPassword());
    }

    public static Connection getServerConnection() throws SQLException {
        return open(connectionUrl(""), getUsername(), getPassword());
    }

    public static String getDatabaseName() {
        String database = DatabaseConfig.valueOrDefault(
                "TRAVEL_AGENCY_DB_NAME", "travel.agency.db.name", "travel_agency");
        if (!database.matches("[A-Za-z0-9_]+")) {
            throw new IllegalStateException("TRAVEL_AGENCY_DB_NAME may contain only letters, numbers, and underscores");
        }
        return database;
    }

    private static Connection open(String url, String username, String password) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException exception) {
            throw new SQLException("MySQL JDBC driver is unavailable", exception);
        }
        return DriverManager.getConnection(url, username, password);
    }

    private static String connectionUrl(String database) {
        String host = DatabaseConfig.valueOrDefault(
                "TRAVEL_AGENCY_DB_HOST", "travel.agency.db.host", "127.0.0.1");
        String port = DatabaseConfig.valueOrDefault(
                "TRAVEL_AGENCY_DB_PORT", "travel.agency.db.port", "3306");
        if (!port.matches("[0-9]{1,5}")) {
            throw new IllegalStateException("TRAVEL_AGENCY_DB_PORT must be a number");
        }
        return "jdbc:mysql://" + host + ":" + port + "/" + database
                + "?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    }

    private static String getUsername() {
        return DatabaseConfig.require("TRAVEL_AGENCY_DB_USER", "travel.agency.db.user");
    }

    private static String getPassword() {
        return DatabaseConfig.require("TRAVEL_AGENCY_DB_PASSWORD", "travel.agency.db.password");
    }
}
