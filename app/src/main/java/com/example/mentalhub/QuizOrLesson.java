package com.example.mentalhub;

import com.example.mentalhub.quiz.*;
import com.example.mentalhub.psychoeducation.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class QuizOrLesson extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    ImageButton quizImg, lessonImg;
    TextView userDetails;

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
        } else {
            // Gets display name of the user and if not available gets their email
            if (user.getDisplayName() != null) {
                userDetails.setText(user.getDisplayName());
            } else {
                userDetails.setText(user.getEmail());
            }

            quizImg.setOnClickListener((View v) -> {
                {
                    Intent intent = new Intent(getApplicationContext(), QuizDetailsActivity.class);
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