package com.example.mentalhub.PsychologistSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mentalhub.GameActivity;
import com.example.mentalhub.Login;
import com.example.mentalhub.R;
import com.example.mentalhub.Tutorial;
import com.example.mentalhub.psychoeducation.TutorialEducation;
import com.example.mentalhub.screening.*;
import com.example.mentalhub.progressChecker.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class PsychologistActivity extends AppCompatActivity {

    // Global declaration of variables
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userId;
    Button logoutButton, progress;
    TextView textView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ImageView tutorial ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psychologist);

        //Authentication
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        logoutButton = findViewById(R.id.logout);
        progress = findViewById(R.id.patientProgress);


        textView = findViewById(R.id.user_details);






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
            databaseReference.child("Users").child(user.getUid()).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        textView.setText(Objects.requireNonNull(task.getResult().getValue()).toString());
                    }
                }
            });

            // When user opens the MainActivity they will be asked to subscribe to the notification
            FirebaseMessaging.getInstance().subscribeToTopic("MentalHubNotification")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "Done";
                            if (!task.isSuccessful()) {
                                msg = "Failed";
                            }

                        }
                    });

            progress.setOnClickListener((View v) -> {
                Intent intent = new Intent(getApplicationContext(), PatientView.class);
                startActivity(intent);
            });







        }

        // Button to log out the user
        logoutButton.setOnClickListener((View v) -> {
            {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

    }



}