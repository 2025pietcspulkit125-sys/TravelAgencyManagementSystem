package com.example.travelagency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {

    private UserManager() {
    }

    public static boolean authenticate(
            String enteredUsername,
            String enteredPassword) {
        String query = "SELECT password_hash, salt FROM users WHERE username = ? AND is_active = 1";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, enteredUsername);
            try (ResultSet result = statement.executeQuery()) {
                return result.next()
                        && PasswordUtil.matches(
                        enteredPassword,
                        result.getString("salt"),
                        result.getString("password_hash")
                );
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to authenticate user", exception);
        }
    }

    public static boolean usernameExists(
            String enteredUsername) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT 1 FROM users WHERE username = ?")) {
            statement.setString(1, enteredUsername);
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to check username", exception);
        }
    }

    public static void changePassword(String username, String newPassword) {
        if (username == null || username.isBlank() || newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("Username and password are required; password must be at least 8 characters");
        }
        String salt = PasswordUtil.createSalt();
        String hash = PasswordUtil.hash(newPassword, salt);
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE users SET password_hash = ?, salt = ?, updated_at = CURRENT_TIMESTAMP WHERE username = ?")) {
            statement.setString(1, hash);
            statement.setString(2, salt);
            statement.setString(3, username);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to change password", exception);
        }
    }

    public static void registerUser(String username, String password, String role) {
        if (username == null || username.isBlank() || username.length() > 255
                || password == null || password.length() < 8) {
            throw new IllegalArgumentException("Username and password are required");
        }
        String salt = PasswordUtil.createSalt();
        String hash = PasswordUtil.hash(password, salt);
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO users (username, password_hash, salt, role) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, username.trim());
            statement.setString(2, hash);
            statement.setString(3, salt);
            statement.setString(4, role == null || role.isBlank() ? "user" : role.trim());
            statement.executeUpdate();
        } catch (SQLException exception) {
            if ("23000".equals(exception.getSQLState())) {
                throw new IllegalArgumentException("Username already exists", exception);
            }
            throw new IllegalStateException("Unable to register user", exception);
        }
    }

    public static void initializeConfiguredAdmin() {
        String username = DatabaseConfig.optional(
                "TRAVEL_AGENCY_DEFAULT_ADMIN_USERNAME", "travel.agency.default.admin.username");
        String password = DatabaseConfig.optional(
                "TRAVEL_AGENCY_DEFAULT_ADMIN_PASSWORD", "travel.agency.default.admin.password");
        if (username == null && password == null) {
            return;
        }
        if (username == null || password == null || password.length() < 8) {
            throw new IllegalStateException(
                    "Configure both TRAVEL_AGENCY_DEFAULT_ADMIN_USERNAME and "
                            + "TRAVEL_AGENCY_DEFAULT_ADMIN_PASSWORD (at least 8 characters)");
        }
        if (!usernameExists(username)) {
            registerUser(username, password, "admin");
        }
    }
}
