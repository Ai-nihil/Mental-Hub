package com.example.mentalhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mentalhub.cognitive.Cognitive;
import com.example.mentalhub.ProblemSolving.Bulimia.*;
import com.example.mentalhub.cognitive.Tutorial;
import com.example.mentalhub.journal.Journaling;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class GameActivity extends AppCompatActivity {

    // Global declaration of variables
    FirebaseAuth mAuth;
    FirebaseUser user;
    Button logoutBtn, journalingButton, cognitiveRestructuringButton,
            mindfulnessRelaxationButton, breathingExerciseButton, problemSolvingButton;
    TextView userDetails;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Instantiating variables
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userDetails = findViewById(R.id.user_details);
        logoutBtn = findViewById(R.id.logout);
        journalingButton = findViewById(R.id.journaling);
        cognitiveRestructuringButton = findViewById(R.id.cognitiveRestructuring);
        mindfulnessRelaxationButton = findViewById(R.id.mindfulnessRelaxation);
        breathingExerciseButton = findViewById(R.id.breathingExercise);
        problemSolvingButton = findViewById(R.id.problemSolving);

        // Sends user to login page if there is no session for user
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else {
            // Gets display name of the user
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            userId = user.getUid();
            if (user.getDisplayName() != null) {
                userDetails.setText(user.getDisplayName());
            } else {
                databaseReference.child("Users").child(user.getUid()).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            userDetails.setText(Objects.requireNonNull(task.getResult().getValue()).toString());
                        }
                    }
                });
            }

            journalingButton.setOnClickListener((View v) -> {
                Intent intent = new Intent(getApplicationContext(), Journaling.class);
                startActivity(intent);
            });

            cognitiveRestructuringButton.setOnClickListener((View v) -> {
                Intent intent = new Intent(getApplicationContext(), Tutorial.class);
                startActivity(intent);
            });

            problemSolvingButton.setOnClickListener((View v) -> {
                Intent intent = new Intent(getApplicationContext(), Bulimia.class);
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