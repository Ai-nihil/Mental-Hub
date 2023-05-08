package com.example.mentalhub.journal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhub.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class JournalAdapter extends FirestoreRecyclerAdapter<Journal, JournalAdapter.JournalViewHolder> {

    Context context;

    public JournalAdapter(@NonNull FirestoreRecyclerOptions<Journal> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull JournalViewHolder holder, int position, @NonNull Journal journal) {
        holder.titleTextView.setText(journal.title);
        holder.contentTextView.setText(journal.content);
        holder.dateTextView.setText(journal.date);

        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, JournalDetailsActivity.class);
            intent.putExtra("title", journal.title);
            intent.putExtra("content", journal.content);

            // Gets the snapshot of the current position of what was clicked
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent, false);
        return new JournalViewHolder(view);
    }

    class JournalViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, contentTextView, dateTextView;

        public JournalViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.journal_title_text_view);
            contentTextView = itemView.findViewById(R.id.journal_content_text_view);
            dateTextView = itemView.findViewById(R.id.journal_date_text_view);
        }
    }
}
