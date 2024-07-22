package com.management.librarymanagement.controller;

import com.management.librarymanagement.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionControllerTest {
    private TransactionController transactionController;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        transactionController = new TransactionController();
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
    }

    @Test
    public void testInsertTransaction() throws SQLException {
        Transaction transaction = new Transaction(1, 1, 1, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1));

        when(connection.prepareStatement(TransactionController.INSERT_TRANSACTION_SQL)).thenReturn(preparedStatement);

        transactionController = spy(transactionController);
        doReturn(connection).when(transactionController).getConnection();

        transactionController.insertTransaction(transaction);

        verify(preparedStatement).setInt(1, transaction.getTransactionId());
        verify(preparedStatement).setInt(2, transaction.getBookId());
        verify(preparedStatement).setInt(3, transaction.getPatronId());
        verify(preparedStatement).setDate(4, Date.valueOf(transaction.getBorrowDate()));
        verify(preparedStatement).setDate(5, Date.valueOf(transaction.getReturnDate()));
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testSelectTransaction() throws SQLException {
        int transactionId = 1;
        when(connection.prepareStatement(TransactionController.SELECT_TRANSACTION_BY_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("transactionId")).thenReturn(transactionId);
        when(resultSet.getInt("bookId")).thenReturn(1);
        when(resultSet.getInt("patronId")).thenReturn(1);
        when(resultSet.getDate("borrowDate")).thenReturn(Date.valueOf(LocalDate.of(2023, 1, 1)));
        when(resultSet.getDate("returnDate")).thenReturn(Date.valueOf(LocalDate.of(2023, 2, 1)));

        transactionController = spy(transactionController);
        doReturn(connection).when(transactionController).getConnection();

        Transaction transaction = transactionController.selectTransaction(transactionId);

        assertNotNull(transaction);
        assertEquals(transactionId, transaction.getTransactionId());
        assertEquals(1, transaction.getBookId());
        assertEquals(1, transaction.getPatronId());
        assertEquals(LocalDate.of(2023, 1, 1), transaction.getBorrowDate());
        assertEquals(LocalDate.of(2023, 2, 1), transaction.getReturnDate());
    }

    @Test
    public void testSelectAllTransactions() throws SQLException {
        when(connection.prepareStatement(TransactionController.SELECT_ALL_TRANSACTIONS)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("transactionId")).thenReturn(1, 2);
        when(resultSet.getInt("bookId")).thenReturn(1, 2);
        when(resultSet.getInt("patronId")).thenReturn(1, 2);
        when(resultSet.getDate("borrowDate")).thenReturn(Date.valueOf(LocalDate.of(2023, 1, 1)), Date.valueOf(LocalDate.of(2023, 3, 1)));
        when(resultSet.getDate("returnDate")).thenReturn(Date.valueOf(LocalDate.of(2023, 2, 1)), Date.valueOf(LocalDate.of(2023, 4, 1)));

        transactionController = spy(transactionController);
        doReturn(connection).when(transactionController).getConnection();

        List<Transaction> transactions = transactionController.selectAllTransactions();

        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        assertEquals(1, transactions.get(0).getTransactionId());
        assertEquals(2, transactions.get(1).getTransactionId());
    }

    @Test
    public void testDeleteTransaction() throws SQLException {
        int transactionId = 1;

        when(connection.prepareStatement(TransactionController.DELETE_TRANSACTION_SQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        transactionController = spy(transactionController);
        doReturn(connection).when(transactionController).getConnection();

        boolean result = transactionController.deleteTransaction(transactionId);

        assertTrue(result);
        verify(preparedStatement).setInt(1, transactionId);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateTransaction() throws SQLException {
        Transaction transaction = new Transaction(1, 1, 1, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1));

        when(connection.prepareStatement(TransactionController.UPDATE_TRANSACTION_SQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        transactionController = spy(transactionController);
        doReturn(connection).when(transactionController).getConnection();

        boolean result = transactionController.updateTransaction(transaction);

        assertTrue(result);
        verify(preparedStatement).setDate(1, Date.valueOf(transaction.getReturnDate()));
        verify(preparedStatement).setInt(2, transaction.getTransactionId());
        verify(preparedStatement).executeUpdate();
    }

}