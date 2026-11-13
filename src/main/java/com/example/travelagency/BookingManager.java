package com.example.travelagency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** CRUD operations for reservations; all data is stored in MySQL. */
public final class BookingManager {

    private BookingManager() {
    }

    public static void addBooking(Booking booking) {
        String sql = "INSERT INTO bookings "
                + "(customer_id, trip_id, booked_by_user_id, passenger_count, status, total_amount, notes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(statement, booking);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to add booking", exception);
        }
    }

    public static List<Booking> getBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT id, customer_id, trip_id, booked_by_user_id, passenger_count, status, "
                + "total_amount, notes FROM bookings ORDER BY id";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                bookings.add(readBooking(result));
            }
            return bookings;
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to load bookings", exception);
        }
    }

    public static Booking getBooking(int id) {
        String sql = "SELECT id, customer_id, trip_id, booked_by_user_id, passenger_count, status, "
                + "total_amount, notes FROM bookings WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? readBooking(result) : null;
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to load booking", exception);
        }
    }

    public static void updateBooking(Booking booking) {
        String sql = "UPDATE bookings SET customer_id = ?, trip_id = ?, booked_by_user_id = ?, "
                + "passenger_count = ?, status = ?, total_amount = ?, notes = ?, "
                + "updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            bind(statement, booking);
            statement.setInt(8, booking.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to update booking", exception);
        }
    }

    public static void deleteBooking(int id) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM bookings WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to delete booking", exception);
        }
    }

    private static void bind(PreparedStatement statement, Booking booking) throws SQLException {
        statement.setInt(1, booking.getCustomerId());
        statement.setInt(2, booking.getTripId());
        if (booking.getBookedByUserId() == null) {
            statement.setNull(3, java.sql.Types.INTEGER);
        } else {
            statement.setInt(3, booking.getBookedByUserId());
        }
        statement.setInt(4, booking.getPassengerCount());
        statement.setString(5, booking.getStatus());
        statement.setBigDecimal(6, booking.getTotalAmount());
        statement.setString(7, booking.getNotes());
    }

    private static Booking readBooking(ResultSet result) throws SQLException {
        int bookedByUserId = result.getInt("booked_by_user_id");
        Integer nullableUserId = result.wasNull() ? null : bookedByUserId;
        return new Booking(
                result.getInt("id"),
                result.getInt("customer_id"),
                result.getInt("trip_id"),
                nullableUserId,
                result.getInt("passenger_count"),
                result.getString("status"),
                result.getBigDecimal("total_amount"),
                result.getString("notes"));
    }
}
