package com.example.mentalhub.screening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mentalhub.R;
import com.example.mentalhub.quiz.Quiz;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class eat26results extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userId;
    TextView possibleRisk, youAreAtRiskOf, riskDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat26results);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        possibleRisk = findViewById(R.id.possibleRisk);
        youAreAtRiskOf = findViewById(R.id.youAreAtRiskOf);
        riskDescription = findViewById(R.id.riskDescription);

        // Gets the user's EAT26 results
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        userId = user.getUid();

        // Loads image into ImageView without storing locally.
        databaseReference.child("Users").child(userId).child("result").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    if (Integer.parseInt(String.valueOf(task.getResult().getValue())) < 20) {
                        possibleRisk.setText("Nothing");
                        riskDescription.setText("you are not at risk of maladaptive eating behavior");
                    } else {
                        databaseReference.child("Users").child(userId).child("bulimiaPoints").get().addOnCompleteListener(bulimiaTask -> {
                            if (bulimiaTask.isSuccessful()) {
                                int bulimiaScore = Integer.parseInt(String.valueOf(bulimiaTask.getResult().getValue()));

                                databaseReference.child("Users").child(userId).child("dietingPoints").get().addOnCompleteListener(dietingTask -> {
                                    if (dietingTask.isSuccessful()) {
                                        int dietingScore = Integer.parseInt(String.valueOf(dietingTask.getResult().getValue()));

                                        databaseReference.child("Users").child(userId).child("oralControlPoints").get().addOnCompleteListener(oralControlTask -> {
                                            if (oralControlTask.isSuccessful()) {
                                                int oralControlScore = Integer.parseInt(String.valueOf(oralControlTask.getResult().getValue()));

                                                if (bulimiaScore >= dietingScore && bulimiaScore >= oralControlScore) {
                                                    possibleRisk.setText("Bulimia and food preoccupation (EAT-BUL)");
                                                    riskDescription.setText("The preoccupation with thoughts about food and attempts to vomit food eaten during a binge");
                                                } else if (dietingScore >= oralControlScore) {
                                                    possibleRisk.setText("Dietary (EAT-DIET)");
                                                    riskDescription.setText("The preoccupation with being thinner and avoidance of fattening foods");
                                                } else {
                                                    possibleRisk.setText("Oral Control (EAT-ORAL)");
                                                    riskDescription.setText("Attempts to maintain self-control while eating and the perceived pressure from others to gain weight");
                                                }
                                            } else {
                                                // Handle the error for oralControlPoints task
                                            }
                                        });
                                    } else {
                                        // Handle the error for dietingPoints task
                                    }
                                });
                            } else {
                                // Handle the error for bulimiaPoints task
                            }
                        });
                    }
                }
            }
        });
    }
}