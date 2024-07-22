package com.management.librarymanagement.entity;

public class Book extends LibraryItem {
    private String publisher;

    public Book(int id, String title, String author, String genre, String publisher) {
        super(id, title, author, genre);
        this.publisher = publisher;

    }


    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }


    @Override
    public String getDetails() {
        return "Book{" +
                "publisher='" + publisher + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                +'}';
    }
}
