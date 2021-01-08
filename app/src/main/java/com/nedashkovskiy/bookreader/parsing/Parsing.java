package com.nedashkovskiy.bookreader.parsing;

import com.nedashkovskiy.bookreader.downloading.Downloading;
import com.nedashkovskiy.bookreader.pojo.BookData;

import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parsing{
    List<BookData> bookDataList = new ArrayList<>();
    private final String bookName;

    public Parsing(String bookName){
        this.bookName = bookName;
    }

    public int getListSize(){
        HttpConnection connect = (HttpConnection) Jsoup.connect(Downloading.MAIN_LINK + bookName);
        int listSize = 0;
        try {
            listSize = connect.get().select("div[id=main]").select("ol").select("li").size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listSize;
    }

    public List<BookData> getFullBooksList(int listSize){
        HttpConnection connection = (HttpConnection) Jsoup.connect("https://flibusta.is/book/" + bookName);
        if (listSize != 0){
            for (int index = 0; index < listSize; index++){
                try {
                    String author = connection.get().select("div[id=main]").select("ol").select("li")
                            .get(index).select("a").last().text();

                    String bookName = connection.get().select("div[id=main]").select("ol").select("li")
                            .get(index).select("a").first().text();

                    String bookLink = connection.get().select("div[id=main]").select("ol").select("li")
                            .get(index).select("a").first().attr("href");


                    BookData bookData = new BookData((index+1), author, bookName, bookLink);
                    bookDataList.add(bookData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bookDataList;
        } else {
            return null;
        }
    }
}
