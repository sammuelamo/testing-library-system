package com.management.librarymanagement.controller;

import com.management.librarymanagement.entity.Book;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Supplier;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;




class BookControllerTest {
    private BookController bookController;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;



    @BeforeEach
    public void setUp() throws Exception {
        bookController = new BookController();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

    }



    @Test
    public void testInsertBook() throws SQLException {
        Book book = new Book(1, "Test Title", "Test Author", "Test Genre", "Test Publisher");


        when(mockConnection.prepareStatement(BookController.INSERT_BOOK_SQL)).thenReturn(mockPreparedStatement);

        bookController = spy(bookController);
        doReturn(mockConnection).when(bookController).getConnection();

        bookController.insertBook(book);

        verify(mockPreparedStatement).setInt(1, book.getId());
        verify(mockPreparedStatement).setString(2, book.getTitle());
        verify(mockPreparedStatement).setString(3, book.getAuthor());
        verify(mockPreparedStatement).setString(4, book.getGenre());
        verify(mockPreparedStatement).setString(5, book.getPublisher());
        verify(mockPreparedStatement).executeUpdate();
/*
        Exception exception = assertThrows(SQLException.class, () -> {
            bookController.insertBook(book);
        });
        assertEquals("Error inserting book: Test Title", exception.getMessage());*/
    }


    @ParameterizedTest
    @CsvSource({
            "1, Title 1, Author 1, Genre 1, Publisher 1",
            "2, Title 2, Author 2, Genre 2, Publisher 2",
            "3, Title 3, Author 3, Genre 3, Publisher 3"
    })
    public void testInsertBookParameterized(int id, String title, String author, String genre, String publisher) throws SQLException {
        Book book = new Book(id, title, author, genre, publisher);

        when(mockConnection.prepareStatement(BookController.INSERT_BOOK_SQL)).thenReturn(mockPreparedStatement);

        bookController = spy(bookController);
        doReturn(mockConnection).when(bookController).getConnection();

        bookController.insertBook(book);

        verify(mockPreparedStatement).setInt(1, book.getId());
        verify(mockPreparedStatement).setString(2, book.getTitle());
        verify(mockPreparedStatement).setString(3, book.getAuthor());
        verify(mockPreparedStatement).setString(4, book.getGenre());
        verify(mockPreparedStatement).setString(5, book.getPublisher());
        verify(mockPreparedStatement).executeUpdate();
    }


    @Test
    public void testSelectBook() throws SQLException {
        int bookId = 1;
        when(mockConnection.prepareStatement(BookController.SELECT_BOOK_BY_ID)).thenReturn(mockPreparedStatement);
        //doThrow(new SQLException("Mock SQL Exception")).when(mockPreparedStatement).executeQuery();
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("id")).thenReturn(bookId);
        when(mockResultSet.getString("title")).thenReturn("Test Title");
        when(mockResultSet.getString("author")).thenReturn("Test Author");
        when(mockResultSet.getString("genre")).thenReturn("Test Genre");
        when(mockResultSet.getString("publisher")).thenReturn("Test Publisher");

        bookController = spy(bookController);
        doReturn(mockConnection).when(bookController).getConnection();

        Book book = bookController.selectBook(bookId);

        assertNotNull(book);
        assertEquals(bookId, book.getId());
        assertEquals("Test Title", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals("Test Genre", book.getGenre());
        assertEquals("Test Publisher", book.getPublisher());

   /*     // Testing exception scenario
        doThrow(new SQLException("Mock SQL Exception")).when(mockPreparedStatement).executeQuery();

        SQLException exception = assertThrows(SQLException.class, () -> {
            bookController.selectBook(bookId);
        });
        assertEquals("Error selecting book by id: 1", exception.getMessage());*/
    }

    @Test
    public void testSelectAllBooks() throws SQLException {
        when(mockConnection.prepareStatement(BookController.SELECT_ALL_BOOKS)).thenReturn(mockPreparedStatement);
        //doThrow(new SQLException("Mock SQL Exception")).when(mockPreparedStatement).executeQuery();
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("title")).thenReturn("Test Title 1", "Test Title 2");
        when(mockResultSet.getString("author")).thenReturn("Test Author 1", "Test Author 2");
        when(mockResultSet.getString("genre")).thenReturn("Test Genre 1", "Test Genre 2");
        when(mockResultSet.getString("publisher")).thenReturn("Test Publisher 1", "Test Publisher 2");

        bookController = spy(bookController);
        doReturn(mockConnection).when(bookController).getConnection();

        List<Book> books = bookController.selectAllBooks();

        assertNotNull(books);
        assertEquals(2, books.size());
        assertEquals(1, books.get(0).getId());
        assertEquals(2, books.get(1).getId());

     /*   // Testing exception scenario
        doThrow(new SQLException("Mock SQL Exception")).when(mockPreparedStatement).executeQuery();

        SQLException exception = assertThrows(SQLException.class, () -> {
            bookController.selectAllBooks();
        });
        assertEquals("Error selecting all books", exception.getMessage());*/

    }

    @Test
    public void testDeleteBook() throws SQLException {
        int bookId = 1;

        when(mockConnection.prepareStatement(BookController.DELETE_BOOK_SQL)).thenReturn(mockPreparedStatement);
        //doThrow(new SQLException("Mock SQL Exception")).when(mockPreparedStatement).executeUpdate();
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        bookController = spy(bookController);
        doReturn(mockConnection).when(bookController).getConnection();

        boolean result = bookController.deleteBook(bookId);

        assertTrue(result);
        verify(mockPreparedStatement).setInt(1, bookId);
        verify(mockPreparedStatement).executeUpdate();
       /* // Testing exception scenario
        doThrow(new SQLException("Mock SQL Exception")).when(mockPreparedStatement).executeUpdate();

        SQLException exception = assertThrows(SQLException.class, () -> {
            bookController.deleteBook(bookId);
        });
        assertEquals("Error deleting book with id: 1", exception.getMessage());*/
    }

    @Test
    public void testUpdateBook() throws SQLException {
        Book book = new Book(1, "Updated Title", "Updated Author", "Updated Genre", "Updated Publisher");

        when(mockConnection.prepareStatement(BookController.UPDATE_BOOK_SQL)).thenReturn(mockPreparedStatement);
        //doThrow(new SQLException("Mock SQL Exception")).when(mockPreparedStatement).executeUpdate();
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        bookController = spy(bookController);
        doReturn(mockConnection).when(bookController).getConnection();

        boolean result = bookController.updateBook(book);

        assertTrue(result);
        verify(mockPreparedStatement).setString(1, book.getTitle());
        verify(mockPreparedStatement).setString(2, book.getAuthor());
        verify(mockPreparedStatement).setString(3, book.getGenre());
        verify(mockPreparedStatement).setString(4, book.getPublisher());
        verify(mockPreparedStatement).setInt(5, book.getId());
        verify(mockPreparedStatement).executeUpdate();

        /*// Testing exception scenario
        doThrow(new SQLException("Mock SQL Exception")).when(mockPreparedStatement).executeUpdate();

        SQLException exception = assertThrows(SQLException.class, () -> {
            bookController.updateBook(book);
        });
        assertEquals("Error updating book: Updated Title", exception.getMessage());*/
    }


}