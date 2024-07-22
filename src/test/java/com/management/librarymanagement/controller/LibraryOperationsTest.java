package com.management.librarymanagement.controller;

import com.management.librarymanagement.databaseConnect.DatabaseConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryOperationsTest {
    private LibraryOperations libraryOperations;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    private MockedStatic<DatabaseConnection> mockedStatic;

    @BeforeEach
    public void setUp() throws SQLException {
        libraryOperations = new LibraryOperations();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        mockedStatic = mockStatic(DatabaseConnection.class);
        mockedStatic.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        mockedStatic.close();
    }
    @Test
    public void testBorrowBook_BookAlreadyBorrowed() throws SQLException {
        int bookId = 1;
        int patronId = 1;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);  // Simulate book already borrowed

        boolean result = libraryOperations.borrowBook(bookId, patronId);

        assertFalse(result);
        verify(mockPreparedStatement, times(1)).setInt(1, bookId);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testBorrowBook_BookNotBorrowed() throws SQLException {
        int bookId = 1;
        int patronId = 1;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);  // Simulate book not borrowed

        when(mockPreparedStatement.executeUpdate()).thenReturn(1);  // Simulate successful insert

        boolean result = libraryOperations.borrowBook(bookId, patronId);

        assertTrue(result);
        verify(mockPreparedStatement, times(2)).setInt(1, bookId);
        verify(mockPreparedStatement, times(1)).setInt(2, patronId);
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }
    @Test
    public void testReturnBook_Success() throws SQLException {
        int bookId = 1;
        int patronId = 1;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);  // Simulate successful delete

        boolean result = libraryOperations.returnBook(bookId, patronId);

        assertTrue(result);
        verify(mockPreparedStatement, times(1)).setInt(1, bookId);
        verify(mockPreparedStatement, times(1)).setInt(2, patronId);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testReturnBook_Failure() throws SQLException {
        int bookId = 1;
        int patronId = 1;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);  // Simulate failed delete

        boolean result = libraryOperations.returnBook(bookId, patronId);

        assertFalse(result);
        verify(mockPreparedStatement, times(1)).setInt(1, bookId);
        verify(mockPreparedStatement, times(1)).setInt(2, patronId);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testPrintBorrowedBooks() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, false);  // Simulate one borrowed book record
        when(mockResultSet.getString("title")).thenReturn("Book Title");
        when(mockResultSet.getString("name")).thenReturn("Patron Name");

        libraryOperations.printBorrowedBooks();

        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockResultSet, times(2)).next();
        verify(mockResultSet, times(1)).getString("title");
        verify(mockResultSet, times(1)).getString("name");
    }

}