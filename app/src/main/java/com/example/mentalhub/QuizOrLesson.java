package com.example.mentalhub;

import com.example.mentalhub.quiz.*;
import com.example.mentalhub.psychoeducation.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class QuizOrLesson extends AppCompatActivity {

    ImageButton quizImg, lessonImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_or_lesson);

        quizImg = findViewById(R.id.quizButton);
        lessonImg = findViewById(R.id.lessonButton);

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