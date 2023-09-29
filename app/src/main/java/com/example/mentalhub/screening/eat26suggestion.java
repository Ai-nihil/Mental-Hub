package com.example.mentalhub.screening;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.mentalhub.MainActivity;
import com.example.mentalhub.R;

import java.util.ArrayList;
import java.util.Arrays;

public class eat26suggestion extends AppCompatActivity {

    Button continueButton;
    String possibleRisk;
    ArrayList<String> gamesList = new ArrayList<>(Arrays.asList("Breathing",
            "Journaling",
            "Mindfulness and Relaxation",
            "Cognitive Restructuring",
            "Problem Solving",
            "Psychoeducation"));

    ArrayList<Integer> gamesImages = new ArrayList<>(Arrays.asList(R.drawable.breath,
            R.drawable.journal,
            R.drawable.mindfulness,
            R.drawable.form,
            R.drawable.problemsolve,
            R.drawable.psychoeducation));

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat26suggestion);

        continueButton = findViewById(R.id.continueButton);

        possibleRisk = getIntent().getStringExtra("possibleRisk");

        continueButton.setOnClickListener((v) -> {
            Intent intent = new Intent(eat26suggestion.this, MainActivity.class);
            startActivity(intent);
        });

        // Depending on the value of possibleRisk, remove an element from gamesList
        if (possibleRisk != null) {
            if (possibleRisk.equals("Bulimia and food preoccupation (EAT-BUL)")) {

            }
            else if (possibleRisk.equals("Dietary (EAT-DIET)")) {

            } else {

            }
            // Add more conditions as needed for other possibleRisk values
        }

        listView = findViewById(R.id.customListView);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), gamesList, gamesImages);
        listView.setAdapter(customAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
    }
}