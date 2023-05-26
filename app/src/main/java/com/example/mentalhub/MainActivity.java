package com.example.mentalhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mentalhub.screening.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    // Global declaration of variables
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userId;
    Button logoutButton, playNowBtn, eat26, eat26ResultButton, psychoeducationBtn, checkProgressBtn;
    TextView textView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Authentication
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        logoutButton = findViewById(R.id.logout);
        eat26 = findViewById(R.id.eat26assessment);
        eat26ResultButton = findViewById(R.id.eat26Results);
        psychoeducationBtn = findViewById(R.id.psychoeducation);
        checkProgressBtn = findViewById(R.id.checkProgress);
        textView = findViewById(R.id.user_details);
        playNowBtn = findViewById(R.id.playNow);
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
                textView.setText(user.getDisplayName());
            } else {
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
            }

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

            eat26.setOnClickListener((View v) -> {
                {
                    Intent intent = new Intent(getApplicationContext(), Eat26.class);
                    startActivity(intent);
                }
            });

            playNowBtn.setOnClickListener((View v) -> {
                {
                    Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                    startActivity(intent);
                }
            });

            psychoeducationBtn.setOnClickListener((View v) -> {
                {
                    Intent intent = new Intent(getApplicationContext(), QuizOrLesson.class);
                    startActivity(intent);
                }
            });

            eat26ResultButton.setOnClickListener((View v) -> {
                {
                    Intent intent = new Intent(getApplicationContext(), eat26results.class);
                    startActivity(intent);
                }
            });

            // Hides the EAT26RESULTBUTTON if the user has yet to take the eat26 assessment
            databaseReference.child("Users").child(user.getUid()).child("result").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        eat26ResultButton.setVisibility(View.GONE);
                    }
                }
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