package com.example.mentalhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class journaling extends AppCompatActivity {

    FloatingActionButton addNoteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journaling);

        addNoteBtn = findViewById(R.id.add_note_btn);

        addNoteBtn.setOnClickListener((v)
                -> startActivity(new Intent(journaling.this,
                JournalDetailsActivity.class)) );
        {

        };
    }
}