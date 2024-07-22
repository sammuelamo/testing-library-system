package com.management.librarymanagement.controller;

import com.management.librarymanagement.databaseConnect.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibraryOperations {

    DatabaseConnection databaseConnection = new DatabaseConnection();
    public boolean borrowBook(int bookId, int patronId) {
        try (Connection connection = databaseConnection.getConnection()) {
            String checkQuery = "SELECT * FROM borrowed_books WHERE book_id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, bookId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    return false;
                }
            }

            String insertQuery = "INSERT INTO borrowed_books (book_id, patron_id) VALUES (?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, bookId);
                insertStmt.setInt(2, patronId);
                insertStmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean returnBook(int bookId, int patronId) {
        try (Connection connection = databaseConnection.getConnection()) {
            String deleteQuery = "DELETE FROM borrowed_books WHERE book_id = ? AND patron_id = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, bookId);
                deleteStmt.setInt(2, patronId);
                int affectedRows = deleteStmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void printBorrowedBooks() {
        try (Connection connection = databaseConnection.getConnection()) {
            String query = "SELECT books.title, patrons.name FROM borrowed_books" +
                    "JOIN books ON book_id = books.id " +
                    "JOIN patrons ON borrowed_books.patron_id = patrons.id";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String bookTitle = rs.getString("title");
                    String patronName = rs.getString("name");
//                    System.out.println(patronName + " has borrowed " + bookTitle);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

