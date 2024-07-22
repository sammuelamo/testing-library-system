# Library Management System
This is the link to the loom video recoding
https://www.loom.com/share/a77c1662f28340ff952d8a07101b6485?sid=03ec753d-8411-4b38-8a25-58402c6ef290


This is a simple library management system implemented in Java using JavaFX for the user interface and JDBC for database connectivity.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Code Overview](#code-overview)
  - [Classes](#classes)
  - [Database Operations](#database-operations)
- [Contributing](#contributing)
- [License](#license)

## Features

- Add, update, and delete books in the library.
- Add patrons (library members).
- Manage book borrow and return transactions.
- View book details in a table format.

## Installation

1. **Clone the repository:**
   ```sh
   git clone https://github.com/yourusername/library-management-system.git
   cd library-management-system

## Setup the database
- Ensure you have a database named LibraryDB.
- Create the necessary tables (LibraryItem, Patron, Transactions).


## Build the project:
- Use your favorite Java IDE (e.g., IntelliJ IDEA, Eclipse) to open the project.
- Ensure you have JavaFX and JDBC configured.

## Run the application:
- Run the LibraryApplication class to start the application.

## Usage
## Add a Book:
- Fill in the book details (title, author, genre, publisher) in the provided text fields.
- Click the "Add" button to add the book to the database.
## View Books:
- The book table will display all the books in the database with their details.
## Update or Delete a Book:
- Select a book from the table.
- Modify the details in the text fields and click "Update" to save changes or "Delete" to remove the book.
## Manage Patrons:
- Add patrons by filling in their details and associating them with a borrowed book.
## Manage Transactions:
- Record book borrow and return transactions for patrons.


## Code Overview
## Classes
- LibraryApplication: The main entry point of the JavaFX application.
- LibraryItem: Abstract class representing a library item with common attributes like id, title, author, and genre.
- Book: Extends LibraryItem to include a publisher attribute.
- Patron: Represents a library member with details such as name, email, phone, address, and a list of borrowed books.
- Transaction: Represents a borrow/return transaction with details such as transaction ID, item ID, patron ID, borrow date, and return date.
- BookController: Handles the interaction between the user interface and the database for book-related operations.


## Database Operations
- __DatabaseConnection:__ Provides methods to connect to the database.
- __BookController:__ Contains methods to add, update, delete books, and manage patrons and transactions.
  * __bookObservableList():__ Retrieves a list of books from the database.
  * __showBook():__ Displays books in the table view.
  * __addBook():__ Adds a new book to the database.
  * __deleteBook():__ Deletes a book from the database.
  * __addPatron():__ Adds a new patron to the database.
  * __addTransaction():__ Records a new transaction in the database.
