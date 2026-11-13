# Travel Agency Management System

This is the existing Java 17 Swing desktop application. The original screens and
manager interfaces are preserved; customer, trip, and login data are stored in
the local MySQL database `travel_agency`.

## Architecture

- Entry point: `com.example.travelagency.Main`
- UI: Java Swing frames under `src/main/java/com/example/travelagency`
- Persistence: MySQL Connector/J through `DatabaseConnection`
- Initialization: `DatabaseInitializer` creates the database, tables, indexes,
  and booking foreign keys without deleting existing records
- Existing managers (`UserManager`, `CustomerManager`, and `TripManager`) expose
  the same operations while using prepared SQL statements
- `BookingManager` provides reservation CRUD through the same MySQL connection

The original UI did not include a booking screen, so the visual screens remain
unchanged; the normalized booking table and `BookingManager` are ready for a
booking screen or another caller to use.

## Running in VS Code

1. Install a Java 17 JDK, local MySQL, and the **Extension Pack for Java** in VS
   Code.
2. Open the project root folder (the folder containing `pom.xml`) in VS Code.
   Wait for the Java project and Maven dependencies to finish loading.
3. Make sure `.env` exists in the project root and contains your local MySQL
   username and password. The application reads `.env`, environment variables,
   or JVM properties; environment variables take precedence.

   ```powershell
   Copy-Item .env.example .env
   notepad .env
   ```

   You can also configure credentials without a file:

   ```powershell
   $env:TRAVEL_AGENCY_DB_USER = "your_mysql_username"
   $env:TRAVEL_AGENCY_DB_PASSWORD = "your_mysql_password"
   ```

   Alternatively, pass `-Dtravel.agency.db.user=...` and
   `-Dtravel.agency.db.password=...` to Java. The application uses
   `jdbc:mysql://localhost:3306/travel_agency`.

4. Press **F5** and choose **Run Travel Agency Management System**. The same
   launch option is available under **Run and Debug**. On first start, the app
   creates `travel_agency` and its tables. It does not create a hardcoded
   password. To bootstrap an admin on first start, set both optional
   `TRAVEL_AGENCY_DEFAULT_ADMIN_USERNAME` and
   `TRAVEL_AGENCY_DEFAULT_ADMIN_PASSWORD` in `.env`; the password is hashed with
   PBKDF2-HMAC-SHA256.

Do not use Code Runner's **Run Code** command or run `javac Main.java` from the
package folder. The app is a multi-file Maven project, and it needs the MySQL
JDBC driver on the classpath.

If you prefer the VS Code PowerShell terminal and Maven is not installed, run
these commands from the project root:

```powershell
$mysqlJar = "$env:USERPROFILE\.m2\repository\com\mysql\mysql-connector-j\9.5.0\mysql-connector-j-9.5.0.jar"
$sources = Get-ChildItem src\main\java -Recurse -Filter *.java | ForEach-Object FullName
New-Item -ItemType Directory -Force target\classes | Out-Null
javac -cp $mysqlJar -d target\classes $sources
java -cp "target\classes;$mysqlJar" com.example.travelagency.Main
```

The existing `data/travel_agency.db` is never deleted or modified by the
application. To migrate its records, install the Python connector and run:

```powershell
pip install mysql-connector-python
python database\migrate_sqlite_to_mysql.py
```

The migration preserves IDs, hashes, salts, timestamps, and all existing fields.
It is safe to run repeatedly because rows are upserted by their IDs or unique
usernames. The older SQLite file currently contains users, customers, and trips;
bookings are available in MySQL for new reservations.

## Database

The schema is documented in [database/schema.sql](database/schema.sql), and
example statements are in [database/sample_queries.sql](database/sample_queries.sql).
The database contains:

- `users`: PBKDF2-HMAC-SHA256 password hashes, per-user salts, roles, and status
- `customers`: existing customer fields
- `trips`: existing trip fields
- `bookings`: normalized reservations linking a customer, trip, and optional
  booking user, with passenger count, status, amount, and notes

Passwords are never stored in plain text. `UserManager.registerUser` and the
Create an account screen generate a random salt and PBKDF2-HMAC-SHA256 hash.
Do not insert users directly in Workbench with a plaintext password.

To inspect the database in MySQL Workbench:

```sql
SHOW DATABASES;
USE travel_agency;
SHOW TABLES;
SELECT * FROM users;
SELECT * FROM customers;
SELECT * FROM trips;
SELECT * FROM bookings;
```

For a visual inspection in MySQL Workbench:

1. Open the local MySQL connection and run the statements above in a new SQL tab.
2. In the Navigator, refresh the `travel_agency` schema and expand Tables.
3. Right-click a table and choose **Select Rows - Limit 1000** to inspect data.
4. Use **Database > Reverse Engineer** if you want a relationship diagram; the
   booking foreign keys connect customers, trips, and users.

Back up the MySQL schema using Workbench's Server > Data Export, or with
`mysqldump`. Keep the old SQLite file as an untouched fallback. The SQL setup is
also available in `database/schema.sql`, and read-only/reporting examples are in
`database/sample_queries.sql`.
