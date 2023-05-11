package com.example.mentalhub.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhub.R;
import com.example.mentalhub.models.Lesson;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;


public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private List<Lesson> myListDataArrayList;

    // creating a constructor for our variables.
    public LessonAdapter(List<Lesson> myListDataArrayList) {
        this.myListDataArrayList = myListDataArrayList;
    }

    public void setFilteredList(List<Lesson> filteredList) {
        // below line is to add our filtered
        // list in our course array list.
        this.myListDataArrayList = filteredList;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LessonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        Lesson model = myListDataArrayList.get(position);
        holder.textView.setText(model.getDescription());
        holder.imageView.setImageResource(model.getImgId());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(model.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                view.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        // returning the size of array list.
        return myListDataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            // initializing our views with their ids.
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}


