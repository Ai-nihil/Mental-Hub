package com.example.mentalhub.progressChecker;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ViewHolder> {

    private List<UserProgress> progressList;

    public ProgressAdapter(List<UserProgress> progressList) {
        this.progressList = progressList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout here and return a ViewHolder
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data from progressList to your ViewHolder
    }

    @Override
    public int getItemCount() {
        return progressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Define your ViewHolder views here
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your views
        }
    }
}

