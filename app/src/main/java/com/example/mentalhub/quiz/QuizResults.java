package com.example.mentalhub.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mentalhub.R;
import com.example.mentalhub.models.QuestionsLists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class QuizResults extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    int quizPoints;
    int quizBuffer = 0;

    ImageView congratulationsIcon, failureIcon;
    TextView successTextView, failureTextView, correctAnswersSuccess, correctAnswersFail;
    int userGrade;
    int numOfQuestions;
    int passingGrade;
    String congratulationsIconImage = "https://www.psdstamps.com/wp-content/uploads/2022/04/grunge-congratulations-label-png.png";
    String failureIconImage = "https://www.westfield.ma.edu/PersonalPages/draker/edcom/final/webprojects/sp18/sectiona/solarq/tryagain.png";
    Button startNewQuizBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        failureIcon = findViewById(R.id.failureIcon);
        failureTextView = findViewById(R.id.failureTextView);
        congratulationsIcon = findViewById(R.id.congratulationsIcon);
        successTextView = findViewById(R.id.successTextView);
        correctAnswersSuccess = findViewById(R.id.correctAnswersSuccess);
        correctAnswersFail = findViewById(R.id.correctAnswersFail);
        startNewQuizBtn = findViewById(R.id.startNewQuizButton);

        Glide.with(QuizResults.this).load(congratulationsIconImage).centerCrop().into(congratulationsIcon);
        Glide.with(QuizResults.this).load(failureIconImage).centerCrop().into(failureIcon);

        // Get Correct and Incorrect answers from MainActivity.class via intent
        final int getCorrectAnswers = getIntent().getIntExtra("correct",0);
        final int getNumberOfQuestions = getIntent().getIntExtra("numberOfQuestions", numOfQuestions);

        // Gets the value of the user's grade and the passing grade
        passingGrade = getNumberOfQuestions / 2;
        userGrade = getCorrectAnswers;

        //Gets score and assigns to quizPoints in Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // Gets value from quizPoints into quizBuffer
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;

        // Multiplies the value of userGrade by 5 to get the quizPoints
        quizPoints = userGrade * 5;

        databaseReference.child("Users").child(user.getUid()).child("quizPoints").get().addOnCompleteListener(recordedQuizScore -> {
            if (recordedQuizScore.isSuccessful()) {
                if (recordedQuizScore.getResult().getValue() == null) {
                    // Child "quizPoints" does not exist, create it with the initial value of quizBuffer
                    databaseReference.child("Users").child(user.getUid()).child("quizPoints").setValue(quizPoints);
                } else {
                    int existingQuizPoints = Integer.parseInt(String.valueOf(recordedQuizScore.getResult().getValue()));
                    quizPoints = existingQuizPoints + quizPoints;
                    // Update the child "quizPoints" with the new value
                    databaseReference.child("Users").child(user.getUid()).child("quizPoints").setValue(quizPoints);
                }
            }
        });

        // set correct and incorrect answers to TextViews
        correctAnswersSuccess.setText("Correct Answers : " + getCorrectAnswers + " / " + getNumberOfQuestions);
        correctAnswersFail.setText("Correct Answers : " + getCorrectAnswers + " / " + getNumberOfQuestions);

        // Added the and because if there is only one question then it will pass the if condition.
        if (userGrade >= passingGrade && userGrade != 0) {
            successTextView.setVisibility(View.VISIBLE);
            congratulationsIcon.setVisibility(View.VISIBLE);
            correctAnswersSuccess.setVisibility(View.VISIBLE);
        } else {
            failureTextView.setVisibility(View.VISIBLE);
            failureIcon.setVisibility(View.VISIBLE);
            correctAnswersFail.setVisibility(View.VISIBLE);
        }

        startNewQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuizResults.this, Quiz.class));
                finish();
            }
        });
    }

        @Override
        public void onBackPressed() {
            startActivity(new Intent(QuizResults.this, Quiz.class));
            finish();
        }

}