package com.management.librarymanagement.controller;

import com.management.librarymanagement.entity.Transaction;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionController {
    private final String jdbcURL = "jdbc:mysql://localhost:3306/library_db";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = "your_new_password";

    public static final String INSERT_TRANSACTION_SQL = "INSERT INTO transactions (transactionId, bookId, patronId, borrowDate, returnDate) VALUES (?, ?, ?, ?, ?)";
    public static final String SELECT_TRANSACTION_BY_ID = "SELECT transactionId, bookId, patronId, borrowDate, returnDate FROM transactions WHERE transactionId = ?";
    public static final String SELECT_ALL_TRANSACTIONS = "SELECT * FROM transactions";
    public static final String DELETE_TRANSACTION_SQL = "DELETE FROM transactions WHERE transactionId = ?";
    public static final String UPDATE_TRANSACTION_SQL = "UPDATE transactions SET returnDate = ? WHERE transactionId = ?";

    public TransactionController() {}

    protected Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void insertTransaction(Transaction transaction) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRANSACTION_SQL)) {
            preparedStatement.setInt(1, transaction.getTransactionId());
            preparedStatement.setInt(2, transaction.getBookId());
            preparedStatement.setInt(3, transaction.getPatronId());
            preparedStatement.setDate(4, Date.valueOf(transaction.getBorrowDate()));
            preparedStatement.setDate(5, Date.valueOf(transaction.getReturnDate()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Transaction selectTransaction(int transactionId) {
        Transaction transaction = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TRANSACTION_BY_ID)) {
            preparedStatement.setInt(1, transactionId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int bookId = rs.getInt("bookId");
                int patronId = rs.getInt("patronId");
                LocalDate borrowDate = rs.getDate("borrowDate").toLocalDate();
                LocalDate returnDate = rs.getDate("returnDate").toLocalDate();
                transaction = new Transaction(transactionId, bookId, patronId, borrowDate, returnDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    public List<Transaction> selectAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TRANSACTIONS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int transactionId = rs.getInt("transactionId");
                int bookId = rs.getInt("bookId");
                int patronId = rs.getInt("patronId");
                LocalDate borrowDate = rs.getDate("borrowDate").toLocalDate();
                LocalDate returnDate = rs.getDate("returnDate").toLocalDate();
                transactions.add(new Transaction(transactionId, bookId, patronId, borrowDate, returnDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public boolean deleteTransaction(int transactionId) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TRANSACTION_SQL)) {
            preparedStatement.setInt(1, transactionId);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updateTransaction(Transaction transaction) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TRANSACTION_SQL)) {
            preparedStatement.setDate(1, Date.valueOf(transaction.getReturnDate()));
            preparedStatement.setInt(2, transaction.getTransactionId());
            rowUpdated = preparedStatement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

}
