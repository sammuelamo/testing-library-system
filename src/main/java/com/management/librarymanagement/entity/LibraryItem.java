package com.management.librarymanagement.entity;

import com.management.librarymanagement.utils.ItemDetailsProvider;

public abstract class LibraryItem  implements ItemDetailsProvider {
    protected int id;
    protected String title;
    protected String author;
    protected String genre;

    public LibraryItem(int id, String title, String author, String genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public abstract String getDetails();


}
