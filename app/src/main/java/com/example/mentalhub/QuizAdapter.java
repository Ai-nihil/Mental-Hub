package com.example.mentalhub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class QuizAdapter extends FirestoreRecyclerAdapter<Quiz, QuizAdapter.QuizViewHolder> {

    Context context;

    public QuizAdapter(@NonNull FirestoreRecyclerOptions<Quiz> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull QuizViewHolder holder, int position, @NonNull Quiz quiz) {
        holder.nameTextView.setText(quiz.name);

        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, QuizDetailsActivity.class);
            intent.putExtra("name", quiz.name);

            // Gets the snapshot of the current position of what was clicked
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent, false);
        return new QuizViewHolder(view);
    }

    class QuizViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.quizName);
        }
    }
}
