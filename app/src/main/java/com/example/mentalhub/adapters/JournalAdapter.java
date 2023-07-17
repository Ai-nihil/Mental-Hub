package com.example.mentalhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhub.R;
import com.example.mentalhub.journal.JournalDetailsActivity;
import com.example.mentalhub.models.Journal;
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
        holder.titleTextView.setText(journal.getTitle());
        holder.contentTextView.setText(journal.getContent());
        holder.dateTextView.setText(journal.getDate());
        holder.emotion.setText(journal.getEmotion());

        switch (journal.getEmotion()) {
            case "amazing":
                holder.emotion.setButtonDrawable(R.drawable.amazing);
                break;
            case "happy":
                holder.emotion.setButtonDrawable(R.drawable.happy);
                break;
            case "neutral":
                holder.emotion.setButtonDrawable(R.drawable.neutral);
                break;
            case "sad":
                holder.emotion.setButtonDrawable(R.drawable.sad);
                break;
            case "angry":
                holder.emotion.setButtonDrawable(R.drawable.angry);
                break;
        }

        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, JournalDetailsActivity.class);
            intent.putExtra("title", journal.getTitle());
            intent.putExtra("content", journal.getContent());
            intent.putExtra("storedEmotion", journal.getEmotion());

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
        RadioButton emotion;

        public JournalViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.journal_title_text_view);
            contentTextView = itemView.findViewById(R.id.journal_content_text_view);
            dateTextView = itemView.findViewById(R.id.journal_date_text_view);
            emotion = itemView.findViewById(R.id.journal_emotion_radiobutton);
        }
    }
}
