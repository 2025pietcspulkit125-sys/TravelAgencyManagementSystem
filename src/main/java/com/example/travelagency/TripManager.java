package com.example.travelagency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class TripManager {

    public static void addTrip(Trip trip) {
        String sql = "INSERT INTO trips (trip_name, destination, start_date, end_date, price, description) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, trip.getTripName());
            statement.setString(2, trip.getDestination());
            statement.setString(3, trip.getStartDate());
            statement.setString(4, trip.getEndDate());
            statement.setDouble(5, trip.getPrice());
            statement.setString(6, trip.getDescription());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trip.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to add trip", exception);
        }
    }

    public static List<Trip> getTrips() {
        List<Trip> trips = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, trip_name, destination, start_date, end_date, price, description " +
                             "FROM trips ORDER BY id");
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                trips.add(new Trip(
                        result.getInt("id"),
                        result.getString("trip_name"),
                        result.getString("destination"),
                        result.getString("start_date"),
                        result.getString("end_date"),
                        result.getDouble("price"),
                        result.getString("description")
                ));
            }
            return trips;
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to load trips", exception);
        }
    }

    public static void deleteTrip(int id) {
        executeIdChange("DELETE FROM trips WHERE id = ?", id, "delete trip");
    }

    public static void updateTrip(Trip trip) {
        String sql = "UPDATE trips SET trip_name = ?, destination = ?, start_date = ?, end_date = ?, " +
                "price = ?, description = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, trip.getTripName());
            statement.setString(2, trip.getDestination());
            statement.setString(3, trip.getStartDate());
            statement.setString(4, trip.getEndDate());
            statement.setDouble(5, trip.getPrice());
            statement.setString(6, trip.getDescription());
            statement.setInt(7, trip.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to update trip", exception);
        }
    }

    private static void executeIdChange(String sql, int id, String operation) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to " + operation, exception);
        }
    }
}
