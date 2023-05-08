package com.example.mentalhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mentalhub.journal.journaling;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GameActivity extends AppCompatActivity {

    // Global declaration of variables
    FirebaseAuth mAuth;
    FirebaseUser user;
    Button logoutBtn, journalingButton, cognitiveRestructuringButton,
            mindfulnessRelaxationButton, breathingExerciseButton;
    TextView userDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Instantiating variables
        mAuth = FirebaseAuth.getInstance();
        userDetails = findViewById(R.id.user_details);
        logoutBtn = findViewById(R.id.logout);
        journalingButton = findViewById(R.id.journaling);
        cognitiveRestructuringButton = findViewById(R.id.cognitiveRestructuring);
        mindfulnessRelaxationButton = findViewById(R.id.mindfulnessRelaxation);
        breathingExerciseButton = findViewById(R.id.breathingExercise);
        user = mAuth.getCurrentUser();
        // Sends user to login page if there is no session for user
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            userDetails.setText(user.getDisplayName());

            journalingButton.setOnClickListener((View v) -> {
                Intent intent = new Intent(getApplicationContext(), journaling.class);
                startActivity(intent);
            });

            // Button to log out the user
            logoutBtn.setOnClickListener((View v) -> {
                {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}