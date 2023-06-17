package com.example.mentalhub.progressChecker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mentalhub.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class progressActivity extends AppCompatActivity{

    int maxPoints = 100;
    int previousPoints = 0;
    CircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        circularProgressBar = findViewById(R.id.circularProgressBar);

        // Sets progress bar to specific amount of progress
        circularProgressBar.setProgressWithAnimation(maxPoints);
        //TODO: Get the sum of all the points gathered by the current user and place them here

    }
}