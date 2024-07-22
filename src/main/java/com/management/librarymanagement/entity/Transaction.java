package com.management.librarymanagement.entity;

import java.time.LocalDate;

public class Transaction {
    private int transactionId;
    private int bookId;
    private int patronId;
    private LocalDate borrowDate;
    private LocalDate returnDate;


    public Transaction(int transactionId, int bookId, int patronId, LocalDate borrowDate, LocalDate returnDate) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.patronId = patronId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;

    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getBookId() {
        return bookId;
    }

    public int getPatronId() {
        return patronId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void completeTransaction() {
        this.returnDate = LocalDate.now();
    }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(returnDate);
    }

    @Override
    public String toString() {
        return "Transaction [transactionId=" + transactionId + ", itemId=" + bookId + ", patronId=" + patronId
                + ", borrowDate=" + borrowDate + ", returnDate=" + returnDate + "]";
    }
}

