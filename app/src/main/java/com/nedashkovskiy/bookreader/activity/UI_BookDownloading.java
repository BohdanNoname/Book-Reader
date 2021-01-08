package com.nedashkovskiy.bookreader.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nedashkovskiy.bookreader.R;
import com.nedashkovskiy.bookreader.adapter.BookDataAdapterRecyclerView;
import com.nedashkovskiy.bookreader.parsing.Parsing;
import com.nedashkovskiy.bookreader.pojo.BookData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UI_BookDownloading extends AppCompatActivity {

    @BindView(R.id.search_button)
    ImageView button;
    @BindView(R.id.search_file_book)
    EditText inputText;
    @BindView(R.id.searching_recycler_view)
    RecyclerView recyclerView;

    private List<BookData> bookDataList;
    private final String KEY_BUNDLE_BOOK_LIST = "Bundle Key UI_BookDownloading";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_file);
        ButterKnife.bind(this);
    }

    /*-----------------------------------------------------------------*/
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (bookDataList != null){
            outState.putParcelableArrayList(KEY_BUNDLE_BOOK_LIST, (ArrayList<? extends Parcelable>) bookDataList);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bookDataList = savedInstanceState.getParcelableArrayList(KEY_BUNDLE_BOOK_LIST);
        if (bookDataList != null){
            recyclerViewRealization(bookDataList);
        }
    }

    /*-----------------------------------------------------------------*/

    @OnClick(R.id.search_button)
    public void findBook() {
        String query = inputText.getText().toString().replace(" ", "+");
        if (!query.isEmpty()){
            hideKeyboard();
            AsyncTaskForDownloading task = new AsyncTaskForDownloading(query);
            task.execute();
        } else {
            Toast.makeText(this, "Введите название книги", Toast.LENGTH_LONG).show();
        }
    }

    protected void recyclerViewRealization(List<BookData> bookList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BookDataAdapterRecyclerView(bookList));
    }

    private void hideKeyboard(){
        InputMethodManager methodManager = (InputMethodManager) getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (methodManager.isActive()){
            methodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /*-----------------------------------------------------------------*/
    private class AsyncTaskForDownloading extends AsyncTask<Void, Integer, List<BookData> >{

        private String query;

        public AsyncTaskForDownloading(String query){
            this.query = query;
        }

        @Override
        protected List<BookData> doInBackground(Void... voids) {
            Parsing parsing = new Parsing(query);
            int listSize = parsing.getListSize();
            return parsing.getFullBooksList(listSize);
        }

        @Override
        protected void onPostExecute(List<BookData> bookData) {
            super.onPostExecute(bookData);
            if (bookData != null){
                bookDataList = bookData;
                recyclerViewRealization(bookData);
                inputText.setText("");
            } else {
                Toast.makeText(UI_BookDownloading.this, "По вашему запросу ничего не найдено!", Toast.LENGTH_LONG).show();
            }
        }
    }
}