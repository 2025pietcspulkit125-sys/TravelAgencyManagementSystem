CREATE DATABASE IF NOT EXISTS travel_agency;
USE travel_agency;

-- Users (create users through the application so PBKDF2 hashes and salts are generated)
SELECT id, username, role, created_at, is_active FROM users;
-- Never insert or update a plaintext password in this table.

-- Customers
INSERT INTO customers (name, email, phone, address) VALUES ('Example Customer', 'example@example.com', '555-0100', 'Example address');
SELECT id, name, email, phone, address FROM customers;
UPDATE customers SET name = 'Updated Customer', email = 'updated@example.com', phone = '555-0101',
    address = 'Updated address' WHERE id = 1;
DELETE FROM customers WHERE id = 1;

-- Trips
INSERT INTO trips (trip_name, destination, start_date, end_date, price, description)
    VALUES ('Example Trip', 'Paris', '2026-01-01', '2026-01-05', 1000.00, 'Example description');
SELECT id, trip_name, destination, start_date, end_date, price, description FROM trips;
UPDATE trips SET trip_name = 'Updated Trip', destination = 'Rome', start_date = '2026-02-01',
    end_date = '2026-02-05', price = 1200.00, description = 'Updated description' WHERE id = 1;
DELETE FROM trips WHERE id = 1;
SELECT * FROM trips WHERE destination LIKE '%Paris%';
SELECT * FROM customers WHERE name LIKE '%Example%';
SELECT * FROM trips WHERE start_date >= '2026-01-01' AND end_date <= '2026-12-31';

-- Bookings / reservations
INSERT INTO bookings (customer_id, trip_id, passenger_count, status, total_amount, notes)
    VALUES (1, 1, 2, 'CONFIRMED', 2400.00, 'Window seats requested');
SELECT b.id, b.status, b.passenger_count, b.total_amount,
       c.name AS customer_name, t.trip_name, t.destination
FROM bookings b
JOIN customers c ON c.id = b.customer_id
JOIN trips t ON t.id = b.trip_id
ORDER BY b.id;
UPDATE bookings SET status = 'CANCELLED' WHERE id = 1;
DELETE FROM bookings WHERE id = 1;

-- Reporting examples for the currently implemented schema
SELECT COUNT(*) AS total_customers FROM customers;
SELECT COUNT(*) AS total_trips FROM trips;
