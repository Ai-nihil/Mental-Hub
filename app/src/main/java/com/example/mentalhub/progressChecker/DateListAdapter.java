// DateListAdapter.java
package com.example.mentalhub.progressChecker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhub.R;

import java.util.List;

public class DateListAdapter extends RecyclerView.Adapter<DateListAdapter.DateViewHolder> {
    private List<String> dateList;
    private OnItemClickListener itemClickListener;

    public DateListAdapter(List<String> dateList, OnItemClickListener itemClickListener) {
        this.dateList = dateList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        String date = dateList.get(position);
        holder.bind(date);
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String date);
    }

    public class DateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView dateTextView;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            itemView.setOnClickListener(this);
        }

        public void bind(String date) {
            dateTextView.setText(date);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                String date = dateList.get(position);
                itemClickListener.onItemClick(date);
            }
        }
    }
}
