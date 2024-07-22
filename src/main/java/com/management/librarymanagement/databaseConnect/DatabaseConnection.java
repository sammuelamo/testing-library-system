package com.management.librarymanagement.databaseConnect;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "your_new_password";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}

