"""Copy records from the existing SQLite file into the local MySQL database.

The migration is additive and repeatable. It never deletes the SQLite source.
Set TRAVEL_AGENCY_DB_USER and TRAVEL_AGENCY_DB_PASSWORD (or create .env) first.
"""

import os
import sqlite3
import sys
from pathlib import Path

import mysql.connector


PROJECT_ROOT = Path(__file__).resolve().parents[1]
SQLITE_PATH = PROJECT_ROOT / "data" / "travel_agency.db"


def load_dotenv():
    env_path = PROJECT_ROOT / ".env"
    if not env_path.is_file():
        return
    for raw_line in env_path.read_text(encoding="utf-8").splitlines():
        line = raw_line.strip()
        if not line or line.startswith("#") or "=" not in line:
            continue
        key, value = line.split("=", 1)
        value = value.strip().strip("\"'")
        os.environ.setdefault(key.strip(), value)


def configured(name, default=None, required=False):
    value = os.getenv(name, default)
    if required and not value:
        raise RuntimeError(f"Set {name} before running migration")
    return value


def create_schema(cursor, database):
    cursor.execute(f"CREATE DATABASE IF NOT EXISTS `{database}`")
    cursor.execute(f"USE `{database}`")
    cursor.execute(
        """CREATE TABLE IF NOT EXISTS users (
            id INT PRIMARY KEY AUTO_INCREMENT,
            username VARCHAR(255) NOT NULL UNIQUE,
            password_hash VARCHAR(512) NOT NULL,
            salt VARCHAR(512) NOT NULL,
            role VARCHAR(50) NOT NULL DEFAULT 'admin',
            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
            is_active BOOLEAN NOT NULL DEFAULT TRUE,
            INDEX idx_users_active (is_active))"""
    )
    cursor.execute(
        """CREATE TABLE IF NOT EXISTS customers (
            id INT PRIMARY KEY AUTO_INCREMENT,
            name VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL,
            phone VARCHAR(100) NOT NULL,
            address TEXT NOT NULL,
            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
            INDEX idx_customers_name (name))"""
    )
    cursor.execute(
        """CREATE TABLE IF NOT EXISTS trips (
            id INT PRIMARY KEY AUTO_INCREMENT,
            trip_name VARCHAR(255) NOT NULL,
            destination VARCHAR(255) NOT NULL,
            start_date VARCHAR(20) NOT NULL,
            end_date VARCHAR(20) NOT NULL,
            price DECIMAL(12, 2) NOT NULL,
            description TEXT NOT NULL,
            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
            INDEX idx_trips_destination (destination))"""
    )
    cursor.execute(
        """CREATE TABLE IF NOT EXISTS bookings (
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
                REFERENCES users(id) ON UPDATE CASCADE ON DELETE SET NULL,
            INDEX idx_bookings_customer (customer_id),
            INDEX idx_bookings_trip (trip_id),
            INDEX idx_bookings_status (status))"""
    )


def migrate_table(sqlite, mysql_cursor, table, columns, key_column):
    if not sqlite.execute(
        "SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = ?", (table,)
    ).fetchone():
        print(f"{table}: skipped (not present in SQLite source)")
        return

    rows = sqlite.execute(f"SELECT {', '.join(columns)} FROM {table}").fetchall()
    placeholders = ", ".join("%s" for _ in columns)
    updates = ", ".join(
        f"{column}=VALUES({column})" for column in columns if column != key_column
    )
    query = (
        f"INSERT INTO {table} ({', '.join(columns)}) VALUES ({placeholders}) "
        f"ON DUPLICATE KEY UPDATE {updates}"
    )
    if rows:
        mysql_cursor.executemany(query, rows)
    print(f"{table}: migrated {len(rows)} row(s)")


def migrate():
    load_dotenv()
    mysql_user = configured("TRAVEL_AGENCY_DB_USER", required=True)
    mysql_password = configured("TRAVEL_AGENCY_DB_PASSWORD", required=True)
    mysql_host = configured("TRAVEL_AGENCY_DB_HOST", "127.0.0.1")
    mysql_port = int(configured("TRAVEL_AGENCY_DB_PORT", "3306"))
    database = configured("TRAVEL_AGENCY_DB_NAME", "travel_agency")
    if not database.replace("_", "").isalnum():
        raise RuntimeError("TRAVEL_AGENCY_DB_NAME may contain only letters, numbers, and underscores")

    sqlite = sqlite3.connect(SQLITE_PATH)
    mysql_connection = mysql.connector.connect(
        host=mysql_host,
        port=mysql_port,
        user=mysql_user,
        password=mysql_password,
    )
    try:
        mysql_connection.autocommit = False
        cursor = mysql_connection.cursor()
        create_schema(cursor, database)
        migrate_table(
            sqlite,
            cursor,
            "users",
            ["id", "username", "password_hash", "salt", "role", "created_at", "updated_at", "is_active"],
            "username",
        )
        migrate_table(
            sqlite,
            cursor,
            "customers",
            ["id", "name", "email", "phone", "address", "created_at", "updated_at"],
            "id",
        )
        migrate_table(
            sqlite,
            cursor,
            "trips",
            ["id", "trip_name", "destination", "start_date", "end_date", "price", "description", "created_at", "updated_at"],
            "id",
        )
        migrate_table(
            sqlite,
            cursor,
            "bookings",
            ["id", "customer_id", "trip_id", "booked_by_user_id", "passenger_count", "status", "total_amount", "notes", "created_at", "updated_at"],
            "id",
        )
        mysql_connection.commit()
    except Exception:
        mysql_connection.rollback()
        raise
    finally:
        sqlite.close()
        mysql_connection.close()


if __name__ == "__main__":
    try:
        migrate()
    except Exception as error:
        print(f"Migration failed: {error}", file=sys.stderr)
        sys.exit(1)
