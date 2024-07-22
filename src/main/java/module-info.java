module com.management.librarymanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.management.librarymanagement to javafx.fxml;
    exports com.management.librarymanagement;
    exports com.management.librarymanagement.entity;
    opens com.management.librarymanagement.entity to javafx.fxml;
    exports com.management.librarymanagement.utils;
    opens com.management.librarymanagement.utils to javafx.fxml;
}