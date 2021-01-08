package com.nedashkovskiy.bookreader.pojo;

import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Query;

@Entity
public class LastBooks {

    @PrimaryKey
    private int id;
    @ColumnInfo(name = "bookLocalLink")
    private String bookLocalLink;
    @ColumnInfo(name = "nameOfBook")
    private String nameOfBook;

    public LastBooks(int id, String bookLocalLink, String nameOfBook) {
        this.id = id;
        this.bookLocalLink = bookLocalLink;
        this.nameOfBook = nameOfBook;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookLocalLink() {
        return bookLocalLink;
    }

    public void setBookLocalLink(String bookLocalLink) {
        this.bookLocalLink = bookLocalLink;
    }

    public String getNameOfBook() {
        return nameOfBook;
    }

    public void setNameOfBook(String nameOfBook) {
        this.nameOfBook = nameOfBook;
    }
}
