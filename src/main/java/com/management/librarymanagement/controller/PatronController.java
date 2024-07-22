package com.management.librarymanagement.controller;



import com.management.librarymanagement.entity.Patron;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatronController {
    private final String jdbcURL = "jdbc:mysql://localhost:3306/library_db";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = "your_new_password";

    public static final String INSERT_PATRON_SQL = "INSERT INTO patrons (id, name, email, phone, address) VALUES (?, ?, ?, ?, ?)";
    public static final String SELECT_PATRON_BY_ID = "SELECT id, name, email, phone, address FROM patrons WHERE id = ?";
    public static final String SELECT_ALL_PATRONS = "SELECT * FROM patrons";
    public static final String DELETE_PATRON_SQL = "DELETE FROM patrons WHERE id = ?";
    public static final String UPDATE_PATRON_SQL = "UPDATE patrons SET name = ?, email = ?, phone = ?, address = ? WHERE id = ?";

    public PatronController() {}

    protected Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void insertPatron(Patron patron) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PATRON_SQL)) {
            preparedStatement.setInt(1, patron.getId());
            preparedStatement.setString(2, patron.getName());
            preparedStatement.setString(3, patron.getEmail());
            preparedStatement.setString(4, patron.getPhone());
            preparedStatement.setString(5, patron.getAddress());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }

    public Patron selectPatron(int id) {
        Patron patron = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PATRON_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                patron = new Patron(id, name, email, phone, address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patron;
    }

    public List<Patron> selectAllPatrons() {
        List<Patron> patrons = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PATRONS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                patrons.add(new Patron(id, name, email, phone, address));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patrons;
    }

    public boolean deletePatron(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PATRON_SQL)) {
            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updatePatron(Patron patron) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PATRON_SQL)) {
            preparedStatement.setString(1, patron.getName());
            preparedStatement.setString(2, patron.getEmail());
            preparedStatement.setString(3, patron.getPhone());
            preparedStatement.setString(4, patron.getAddress());
            preparedStatement.setInt(5, patron.getId());
            rowUpdated = preparedStatement.executeUpdate() > 0;
        }
        return rowUpdated;
    }


}
