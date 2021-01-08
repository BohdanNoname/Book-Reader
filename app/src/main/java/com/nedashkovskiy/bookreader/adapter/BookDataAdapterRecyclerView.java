package com.nedashkovskiy.bookreader.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nedashkovskiy.bookreader.R;
import com.nedashkovskiy.bookreader.downloading.Downloading;
import com.nedashkovskiy.bookreader.pojo.BookData;

import java.util.List;

public class BookDataAdapterRecyclerView extends RecyclerView.Adapter<BookDataAdapterRecyclerView.ViewHolderAdapter> {

    private List<BookData> dataList;

    public BookDataAdapterRecyclerView(List<BookData> dataList){
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_compose_main_act, parent, false);
        return new ViewHolderAdapter(layout, dataList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAdapter holder, int position) {
        BookData bookData = dataList.get(position);

        holder.position.setText(String.valueOf(bookData.getPosition()));
        holder.book.setText(bookData.getBook());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    /*ViewHolderAdapter*/
    public static class ViewHolderAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView position;
        TextView book;
        List<BookData> bookDataList;


        public ViewHolderAdapter(@NonNull View itemView, List<BookData> bookDataList) {
            super(itemView);
            book = itemView.findViewById(R.id.book_name_recycler_view);
            position = itemView.findViewById(R.id.position_recycler_view);
            this.bookDataList = bookDataList;

            itemView.setOnClickListener(this);
        }

        @SuppressLint("WrongConstant")
        @Override
        public void onClick(View v) {
            String bookName = bookDataList.get(getAdapterPosition()).getBook();
            String bookLink = bookDataList.get(getAdapterPosition()).getBookLink();

            Intent intent = new Intent(v.getContext(), Downloading.class);
            intent.putExtra("bookName", bookName);
            intent.putExtra("bookLink", bookLink);
            v.getContext().startService(intent);
        }
    }
}
