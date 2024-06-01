package pokemon_kanto_adventure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

//A class used to create a connection to sqlite and create table if not exist the sql table
public class Load {
    // a query used to create users table
    private static final String USERS_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS users (" +
            "username VARCHAR(255) PRIMARY KEY," +
            "password VARCHAR(255) NOT NULL," +
            "email VARCHAR(255) UNIQUE NOT NULL" +
            ")";

    // a query used to create badges table
    private static final String BADGES_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS badges (" +
            "username VARCHAR(255)," + // Add this line
            "slot_number INT," +
            "badge_name1 VARCHAR(255)," +
            "badge_name2 VARCHAR(255)," +
            "badge_name3 VARCHAR(255)," +
            "badge_name4 VARCHAR(255)," +
            "badge_name5 VARCHAR(255)," +
            "badge_name6 VARCHAR(255)," +
            "badge_name7 VARCHAR(255)," +
            "badge_name8 VARCHAR(255)," +
            "PRIMARY KEY (slot_number, username)," + // Modify primary key
            "FOREIGN KEY (username) REFERENCES users(username)" + // Add foreign key
            ")";

    // a query used to create item table
    private static final String ITEMS_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS items (" +
            "username VARCHAR(255)," + // Add this line
            "slot_number INT," +
            "item_name VARCHAR(255)," +
            "item_count INT," +
            "PRIMARY KEY (slot_number, item_name, username)," + // Modify primary key
            "FOREIGN KEY (username) REFERENCES users(username)" + // Add foreign key
            ")";

    // a query used to create pc table
    private static final String PC_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS pc (" +
            "username VARCHAR(255)," + // Add this line
            "slot_number INT," +
            "pokemon_name VARCHAR(255)," +
            "pokemon_level INT," +
            "PRIMARY KEY (slot_number, pokemon_name, username)," + // Modify primary key
            "FOREIGN KEY (username) REFERENCES users(username)" + // Add foreign key
            ")";

    // a query used to create saveslots table
    private static final String SAVE_SLOTS_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS saveslots (" +
            "username VARCHAR(255)," + // Add this line
            "player_name VARCHAR(255)," +
            "slot_number INT," +
            "numofbadge INT," +
            "pokemon1 VARCHAR(255)," +
            "pokemon1_level INT," +
            "pokemon2 VARCHAR(255)," +
            "pokemon2_level INT," +
            "pokemon3 VARCHAR(255)," +
            "pokemon3_level INT," +
            "pokemon4 VARCHAR(255)," +
            "pokemon4_level INT," +
            "pokemon5 VARCHAR(255)," +
            "pokemon5_level INT," +
            "pokemon6 VARCHAR(255)," +
            "pokemon6_level INT," +
            "money INT," +
            "rivalracewins INT," +
            "battlewon INT," +
            "currentCity VARCHAR(255)," +
            "PRIMARY KEY (player_name, slot_number, username)," + // Modify primary key
            "FOREIGN KEY (username) REFERENCES users(username)," + // Add foreign key
            "CONSTRAINT slot_number_check CHECK (slot_number BETWEEN 1 AND 3)" +
            ")";

    // used to store connection object
    private Connection con;

    // used to initialize the load class
    public Load() {
        try {
            createConnection();
            createTables();
        } catch (SQLException e) {
            Logger.getLogger(Load.class.getName()).log(Level.SEVERE, "Table creation failed", e);
        }
    }

    // method used to reture the connection
    public Connection getConnection() {
        return con;
    }

    // mehtod used to create the table
    private void createTables() throws SQLException {
        executeQuery(BADGES_TABLE_QUERY);
        executeQuery(ITEMS_TABLE_QUERY);
        executeQuery(PC_TABLE_QUERY);
        executeQuery(SAVE_SLOTS_TABLE_QUERY);
        executeQuery(USERS_TABLE_QUERY); // Add this line
    }

    // mehotd used to execute the query
    private void executeQuery(String query) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            stmt.execute(query);
        }
    }

    // method used to create the connection
    private void createConnection() {
        try {
            Class.forName("org.sqlite.JDBC"); // Ensure the SQLite driver is loaded
            con = DriverManager.getConnection("jdbc:sqlite:pokemon.db");
            Logger.getLogger(Load.class.getName()).log(Level.INFO, "Database connection established");
        } catch (ClassNotFoundException e) {
            Logger.getLogger(Load.class.getName()).log(Level.SEVERE, "SQLite JDBC driver not found", e);
        } catch (SQLException e) {
            Logger.getLogger(Load.class.getName()).log(Level.SEVERE, "Failed to connect to database", e);
        }
    }
}
