package com.project.gamevaultcli.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.IOException;

public class DBUtil {

    private static Connection connection;

    public static Connection getConnection() throws SQLException, IOException {
        if (connection == null || connection.isClosed()) {
            Properties dbProps = CredentialUtil.getDBProperties();
            String url = dbProps.getProperty("db.url");
            String user = dbProps.getProperty("db.user");
            String password = dbProps.getProperty("db.password");

            try {
                Class.forName("com.mysql.cj.jdbc.Driver"); // Load the MySQL driver
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Database connection established.");
                createTablesIfNotExist();
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC driver not found. Make sure it's in your classpath.");
                throw new SQLException("MySQL JDBC driver not found", e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    private static void createTablesIfNotExist() throws SQLException, IOException {
        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate(SQL_CREATE_USERS_TABLE);
            statement.executeUpdate(SQL_CREATE_GAMES_TABLE);
            statement.executeUpdate(SQL_CREATE_CARTS_TABLE);
            statement.executeUpdate(SQL_CREATE_CART_ITEMS_TABLE); // Added this
            statement.executeUpdate(SQL_CREATE_ORDERS_TABLE);
            statement.executeUpdate(SQL_CREATE_TRANSACTIONS_TABLE);
            System.out.println("Tables created or already exist.");
        }
    }

    // Helper function to execute a query and process the ResultSet
    public static <T> List<T> executeQuery(String sql, ResultSetHandler<T> handler, Object... params) throws SQLException, IOException {
        List<T> results = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(handler.handle(rs));
                }
            }

        }
        return results;
    }

    // Helper function to execute an update (INSERT, UPDATE, DELETE)
    public static int executeUpdate(String sql, Object... params) throws SQLException, IOException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            return pstmt.executeUpdate(); // Returns the number of rows affected
        }
    }

     //Helper function to execute an insert and return generated keys
     public static ResultSet executeInsert(String sql, Object... params) throws SQLException, IOException {
        PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        pstmt.executeUpdate();
        return pstmt.getGeneratedKeys(); // Returns the generated keys
    }


    // Interface for handling the ResultSet and mapping to an object
    public interface ResultSetHandler<T> {
        T handle(ResultSet rs) throws SQLException;
    }


    private static final String SQL_CREATE_USERS_TABLE =
            "CREATE TABLE IF NOT EXISTS Users (" +
                    "userId INT AUTO_INCREMENT PRIMARY KEY," +
                    "email VARCHAR(255) NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "username VARCHAR(255) NOT NULL," +
                    "walletBalance FLOAT NOT NULL," +
                    "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";

    private static final String SQL_CREATE_GAMES_TABLE =
            "CREATE TABLE IF NOT EXISTS Games (" +
                    "gameId INT AUTO_INCREMENT PRIMARY KEY," +
                    "title VARCHAR(255) NOT NULL," +
                    "description TEXT," +
                    "developer VARCHAR(255)," +
                    "platform VARCHAR(255)," +
                    "price FLOAT NOT NULL," +
                    "releaseDate DATE" +
                    ")";

    private static final String SQL_CREATE_CARTS_TABLE =
            "CREATE TABLE IF NOT EXISTS Carts (" +
                    "userId INT PRIMARY KEY," +
                    "FOREIGN KEY (userId) REFERENCES Users(userId)" +
                    ")";

    private static final String SQL_CREATE_CART_ITEMS_TABLE = // Added this
            "CREATE TABLE IF NOT EXISTS CartItems (" +
                    "cartItemId INT AUTO_INCREMENT PRIMARY KEY," +
                    "userId INT NOT NULL," +
                    "gameId INT NOT NULL," +
                    "FOREIGN KEY (userId) REFERENCES Carts(userId)," +
                    "FOREIGN KEY (gameId) REFERENCES Games(gameId)" +
                    ")";

    private static final String SQL_CREATE_ORDERS_TABLE =
            "CREATE TABLE IF NOT EXISTS Orders (" +
                    "orderId INT AUTO_INCREMENT PRIMARY KEY," +
                    "userId INT NOT NULL," +
                    "totalAmount DOUBLE NOT NULL," +
                    "orderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (userId) REFERENCES Users(userId)" +
                    ")";

    private static final String SQL_CREATE_TRANSACTIONS_TABLE =
            "CREATE TABLE IF NOT EXISTS Transactions (" +
                    "transactionId INT AUTO_INCREMENT PRIMARY KEY," +
                    "orderId INT NOT NULL," + // Changed this
                    "userId INT NOT NULL," +
                    "transactionType VARCHAR(255)," +
                    "amount FLOAT NOT NULL," +
                    "transactionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (userId) REFERENCES Users(userId)," +
                    "FOREIGN KEY (orderId) REFERENCES Orders(orderId)" +
                    ")";
}