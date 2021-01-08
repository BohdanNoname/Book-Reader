package com.nedashkovskiy.bookreader.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nedashkovskiy.bookreader.R;

public class LastBookLinkAdapter extends RecyclerView.Adapter<LastBookLinkAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_component_books_link, parent,false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

/*_____________________________________________________*/
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
