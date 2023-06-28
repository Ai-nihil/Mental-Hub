package com.example.mentalhub;

import com.example.mentalhub.psychoeducation.*;
import com.example.mentalhub.quiz.Quiz;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Objects;

public class QuizOrLesson extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    ImageButton quizImg, lessonImg;
    TextView userDetails;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_or_lesson);

        quizImg = findViewById(R.id.quizButton);
        lessonImg = findViewById(R.id.lessonButton);
        userDetails = findViewById(R.id.user_details);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
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
                            userDetails.setText(Objects.requireNonNull(task.getResult().getValue()).toString());
                        }
                    }
                });

            quizImg.setOnClickListener((View v) -> {
                {
                    Intent intent = new Intent(getApplicationContext(), Quiz.class);
                    startActivity(intent);
                }
            });

            lessonImg.setOnClickListener((View v) -> {
                {
                    Intent intent = new Intent(getApplicationContext(), LessonActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}