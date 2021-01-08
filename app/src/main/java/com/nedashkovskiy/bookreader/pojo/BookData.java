package com.nedashkovskiy.bookreader.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class BookData implements Parcelable {
    private int position;
    private String author;
    private String book;
    private String bookLink;

    public BookData(){}

    public BookData(int position, String author, String book, String bookLink) {
        this.position = position;
        this.author = author;
        this.book = book;
        this.bookLink = bookLink;
    }

    protected BookData(Parcel in) {
        position = in.readInt();
        author = in.readString();
        book = in.readString();
        bookLink = in.readString();
    }

    public static final Creator<BookData> CREATOR = new Creator<BookData>() {
        @Override
        public BookData createFromParcel(Parcel in) {
            return new BookData(in);
        }

        @Override
        public BookData[] newArray(int size) {
            return new BookData[size];
        }
    };

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getBookLink() {
        return bookLink;
    }

    public void setBookLink(String bookLink) {
        this.bookLink = bookLink;
    }

    @Override
    public String toString() {
        return "BookData{" +
                "position=" + position +
                ", author='" + author + '\'' +
                ", book='" + book + '\'' +
                ", bookLink='" + bookLink + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(position);
        dest.writeString(author);
        dest.writeString(book);
        dest.writeString(bookLink);
    }
}
