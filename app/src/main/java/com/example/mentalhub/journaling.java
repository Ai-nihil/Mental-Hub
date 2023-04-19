package com.example.mentalhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

import java.util.Queue;

public class journaling extends AppCompatActivity {

    JournalAdapter journalAdapter;
    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journaling);

        addNoteBtn = findViewById(R.id.add_note_btn);
        recyclerView = findViewById(R.id.recycle_view);
        menuBtn = findViewById(R.id.menu_btn);

        addNoteBtn.setOnClickListener((v)
                -> startActivity(new Intent(journaling.this,
                JournalDetailsActivity.class)));
        menuBtn.setOnClickListener((v) -> showMenu());
        setupRecyclerView();
    }

    void showMenu() {
        //TODO Display menu
    }

    void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceForJournal().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Journal> options = new FirestoreRecyclerOptions.Builder<Journal>()
                .setQuery(query, Journal.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        journalAdapter = new JournalAdapter(options, this);
        recyclerView.setAdapter(journalAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        journalAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        journalAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        journalAdapter.notifyDataSetChanged();
    }
}