package com.management.librarymanagement.utils;

import com.management.librarymanagement.databaseConnect.DatabaseConnection;
import com.management.librarymanagement.entity.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MAinController implements Initializable {

    public TextField authorfield;
    public TextField titleField;
    public TextField genre_field;
    public TextField publisherField;
    public Button deleteButton;
    public Button updateButton;
    public Button addButton;
    public TextField id_button;
    public TextField name_filed;
    public TextField email_field;
    public TextField phone_field;
    public TextField address_field;
    public TextField book_field;
    public DatePicker date_field;
    public DatePicker returnfield;
    @FXML
    private TableView<Book> table_users;

    @FXML
    private TableColumn<Book, String> author_column;

    @FXML
    private TableColumn<Book, String> genre_column;

    @FXML
    private TableColumn<Book, Integer> id_column;

    @FXML
    private TableColumn<Book, String> publisher_column;

    @FXML
    private TableColumn<Book, String> title_column;


   // Connection connection = null;
  //  ResultSet resultSet = null;
  //  PreparedStatement preparedStatement = null;
    DatabaseConnection databaseConnection = new DatabaseConnection();

    ObservableList<Book> booklist = FXCollections.observableArrayList();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            showBook();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ObservableList<Book> bookObservableList() throws SQLException {
        ObservableList<Book> bookList = FXCollections.observableArrayList();
        Connection conn = databaseConnection.getConnection();
        String sql = "SELECT * FROM Libraryitem";

        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery(sql);
        Book book;
        while (resultSet.next()){
            book = new Book(resultSet.getInt("id"),resultSet.getString("title"),
                    resultSet.getString("author"), resultSet.getString("genre"),
                    resultSet.getString("publisher"));

            bookList.add(book);
        }
        return bookList;
    }

    public void showBook() throws SQLException {
        ObservableList<Book> books = bookObservableList();

        id_column.setCellValueFactory(new PropertyValueFactory<Book, Integer>("id"));
        title_column.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        author_column.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        publisher_column.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));
        genre_column.setCellValueFactory(new PropertyValueFactory<Book, String>("genre"));
        table_users.setItems(books);
    }

    public void updateBook() throws SQLException {
        String id = id_button.getText();
        String newTitle = titleField.getText();
        String newAuthor = authorfield.getText();
        String newGenre = genre_field.getText();
        String newPublisher = publisherField.getText();

        String sql = "UPDATE Libraryitem SET title=?, author=?, genre=?, publisher=? WHERE id=?";
        Connection conn = databaseConnection.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, newTitle);
            preparedStatement.setString(2, newAuthor);
            preparedStatement.setString(3, newGenre);
            preparedStatement.setString(4, newPublisher);
            preparedStatement.setInt(5, Integer.parseInt(id));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book updated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No book found with the given ID. Update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the book.");
        }

        showBook();
        clearFields();
    }


    //delete book from the database
    public void deleteBook() throws SQLException {
        String id = id_button.getText();
        if (id.isEmpty()) {
            System.out.println("ID field is empty. Cannot delete book.");
            return;
        }

        String sql = "DELETE FROM Libraryitem WHERE id = ?";
        Connection conn = databaseConnection.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, Integer.parseInt(id));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book issued successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No book found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while issuing the book.");
        }

        showBook();
        clearFields();
        addPatron();
        addTransaction();
    }

    //add book to the database
    public void addBook() throws SQLException {
        String sql = "INSERT INTO Libraryitem (title, author, genre, publisher) VALUES (?, ?, ?, ?)";
        Connection conn = databaseConnection.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, titleField.getText());
            preparedStatement.setString(2, authorfield.getText());
            preparedStatement.setString(3, genre_field.getText());
            preparedStatement.setString(4, publisherField.getText());

            //preparedStatement.executeUpdate();
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Book Updated", "Book updated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Failed", "No book found with the given ID. Update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the book.");
        }
        showBook();
        clearFields();
 }
    private void clearFields() {
        titleField.clear();
        authorfield.clear();
        genre_field.clear();
        publisherField.clear();
        id_button.clear();
    }

    public void addPatron() throws SQLException {
        String sql = "INSERT INTO Patron (name, email, phone, address, book) VALUES (?, ?, ?, ?, ?)";
        Connection conn = databaseConnection.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, name_filed.getText());
            preparedStatement.setString(2, email_field.getText());
            preparedStatement.setString(3, phone_field.getText());
            preparedStatement.setString(4, address_field.getText());
            preparedStatement.setString(5, book_field.getText());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
               // showAlert(Alert.AlertType.INFORMATION, "Patron Added", "Patron added successfully.");
            } else {
               // showAlert(Alert.AlertType.ERROR, "Error", "Failed to add patron.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while adding the patron.");
        }
        name_filed.clear();
        email_field.clear();
        phone_field.clear();
        address_field.clear();
        book_field.clear();
    }


    public void addTransaction() {
        String sql = "INSERT INTO Transactions (itemId, patronId, borrowDate, returnDate) " +
                "VALUES (" +
                "    (SELECT id FROM Libraryitem WHERE title = ? AND author = ?), " +
                "    (SELECT id FROM Patron WHERE name = ? AND email = ?), " +
                "    ?, " +
                "    ?);";
        Connection conn = databaseConnection.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            // Set parameters for the item subquery
            preparedStatement.setString(1, titleField.getText());
            preparedStatement.setString(2, authorfield.getText());

            // Set parameters for the patron subquery
            preparedStatement.setString(3, name_filed.getText());
            preparedStatement.setString(4, email_field.getText());

            // Set parameters for the dates
            preparedStatement.setDate(5, Date.valueOf(date_field.getValue()));
            preparedStatement.setDate(6, Date.valueOf(returnfield.getValue()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
               // showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction added successfully.");
            } else {
               // showAlert(Alert.AlertType.ERROR, "Error", "No transaction was created.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while adding the transaction.");
        }

    }



    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
