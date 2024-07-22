package com.management.librarymanagement.controller;

import com.management.librarymanagement.entity.Patron;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatronControllerTest {
    private PatronController patronController;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws Exception {
        patronController = new PatronController();
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
    }

    @Test
    public void testInsertPatron() throws SQLException {
        Patron patron = new Patron(1, "John Doe", "john.doe@example.com", "1234567890", "123 Main St");

        when(connection.prepareStatement(PatronController.INSERT_PATRON_SQL)).thenReturn(preparedStatement);
        patronController = spy(patronController);
        doReturn(connection).when(patronController).getConnection();

        patronController.insertPatron(patron);

        verify(preparedStatement).setInt(1, patron.getId());
        verify(preparedStatement).setString(2, patron.getName());
        verify(preparedStatement).setString(3, patron.getEmail());
        verify(preparedStatement).setString(4, patron.getPhone());
        verify(preparedStatement).setString(5, patron.getAddress());
        verify(preparedStatement).executeUpdate();

    }

    @Test
    public void testSelectPatron() throws SQLException {
        int patronId = 1;
        when(connection.prepareStatement(PatronController.SELECT_PATRON_BY_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("id")).thenReturn(patronId);
        when(resultSet.getString("name")).thenReturn("John Doe");
        when(resultSet.getString("email")).thenReturn("john.doe@example.com");
        when(resultSet.getString("phone")).thenReturn("1234567890");
        when(resultSet.getString("address")).thenReturn("123 Main St");

        patronController = spy(patronController);
        doReturn(connection).when(patronController).getConnection();

        Patron patron = patronController.selectPatron(patronId);

        assertNotNull(patron);
        assertEquals(patronId, patron.getId());
        assertEquals("John Doe", patron.getName());
        assertEquals("john.doe@example.com", patron.getEmail());
        assertEquals("1234567890", patron.getPhone());
        assertEquals("123 Main St", patron.getAddress());
    }

    @Test
    public void testSelectAllPatrons() throws SQLException {
        when(connection.prepareStatement(PatronController.SELECT_ALL_PATRONS)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("id")).thenReturn(1, 2);
        when(resultSet.getString("name")).thenReturn("John Doe", "Jane Doe");
        when(resultSet.getString("email")).thenReturn("john.doe@example.com", "jane.doe@example.com");
        when(resultSet.getString("phone")).thenReturn("1234567890", "0987654321");
        when(resultSet.getString("address")).thenReturn("123 Main St", "456 Elm St");

        patronController = spy(patronController);
        doReturn(connection).when(patronController).getConnection();

        List<Patron> patrons = patronController.selectAllPatrons();

        assertNotNull(patrons);
        assertEquals(2, patrons.size());
        assertEquals(1, patrons.get(0).getId());
        assertEquals(2, patrons.get(1).getId());

    }

    @Test
    public void testDeletePatron() throws SQLException {
        int patronId = 1;

        when(connection.prepareStatement(PatronController.DELETE_PATRON_SQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        patronController = spy(patronController);
        doReturn(connection).when(patronController).getConnection();

        boolean result = patronController.deletePatron(patronId);

        assertTrue(result);
        verify(preparedStatement).setInt(1, patronId);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testUpdatePatron() throws SQLException {
        Patron patron = new Patron(1, "Jane Doe", "jane.doe@example.com", "0987654321", "456 Elm St");

        when(connection.prepareStatement(PatronController.UPDATE_PATRON_SQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        patronController = spy(patronController);
        doReturn(connection).when(patronController).getConnection();

        boolean result = patronController.updatePatron(patron);

        assertTrue(result);
        verify(preparedStatement).setString(1, patron.getName());
        verify(preparedStatement).setString(2, patron.getEmail());
        verify(preparedStatement).setString(3, patron.getPhone());
        verify(preparedStatement).setString(4, patron.getAddress());
        verify(preparedStatement).setInt(5, patron.getId());
        verify(preparedStatement).executeUpdate();
    }
}