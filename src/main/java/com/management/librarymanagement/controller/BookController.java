package com.management.librarymanagement.controller;

import com.management.librarymanagement.databaseConnect.DatabaseConnection;
import com.management.librarymanagement.entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookController {


    private final String jdbcURL = "jdbc:mysql://localhost:3306/library_db";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = "your_new_password";

    public static final String INSERT_BOOK_SQL = "INSERT INTO books (id, title, author, genre, publisher) VALUES (?, ?, ?, ?, ?)";
    public static final String SELECT_BOOK_BY_ID = "SELECT id, title, author, genre, publisher FROM books WHERE id = ?";
    public static final String SELECT_ALL_BOOKS = "SELECT * FROM books";
    public static final String DELETE_BOOK_SQL = "DELETE FROM books WHERE id = ?";
    public static final String UPDATE_BOOK_SQL = "UPDATE books SET title = ?, author = ?, genre = ?, publisher = ? WHERE id = ?";



    protected Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {

        }
        return connection;
    }




    public void insertBook(Book book) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BOOK_SQL)) {
            preparedStatement.setInt(1, book.getId());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setString(3, book.getAuthor());
            preparedStatement.setString(4, book.getGenre());
            preparedStatement.setString(5, book.getPublisher());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Book selectBook(int id) {
        Book book = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                String publisher = rs.getString("publisher");
                book = new Book(id, title, author, genre, publisher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }



    public List<Book> selectAllBooks() {
        List<Book> books = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BOOKS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                String publisher = rs.getString("publisher");
                books.add(new Book(id, title, author, genre, publisher));
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return books;
    }

    public boolean deleteBook(int id) throws SQLException {
        boolean rowDeleted = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BOOK_SQL)) {
            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return rowDeleted;
    }

    public boolean updateBook(Book book) throws SQLException {
        boolean rowUpdated = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BOOK_SQL)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getGenre());
            preparedStatement.setString(4, book.getPublisher());
            preparedStatement.setInt(5, book.getId());
            rowUpdated = preparedStatement.executeUpdate() > 0;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return rowUpdated;
    }









}
