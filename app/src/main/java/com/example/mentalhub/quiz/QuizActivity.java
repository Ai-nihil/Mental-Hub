package com.example.mentalhub.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mentalhub.R;
import com.example.mentalhub.models.QuestionsLists;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QuizActivity extends AppCompatActivity {

    private TextView remainingQuestions, questionTextView;

    ImageView backIcon, timeIcon;
    String backIconImage = "https://cdn-icons-png.flaticon.com/512/93/93634.png";
    String timeIconImage = "https://cdn-icons-png.flaticon.com/512/18/18304.png";

    TextView timer, selectedTopicName;
    private AppCompatButton option1, option2, option3, option4, nextBtn;

    private Timer quizTimer;

    private int totalTimeInMins, seconds;

    private final List<QuestionsLists> questionsLists = new ArrayList<>();

    private int getCurrentQuestionPosition = 0;

    private String selectedOptionByUser = "";

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_test);

        backIcon = findViewById(R.id.backButton);
        timeIcon = findViewById(R.id.timeIcon);
        timer = findViewById(R.id.timer);
        selectedTopicName = findViewById(R.id.topicName);
        questionTextView = findViewById(R.id.questionsLists);
        remainingQuestions = findViewById(R.id.questions);
        nextBtn = findViewById(R.id.nextBtn);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        // Loads image into ImageView without storing locally.
        Glide.with(QuizActivity.this).load(backIconImage).centerCrop().into(backIcon);
        Glide.with(QuizActivity.this).load(timeIconImage).centerCrop().into(timeIcon);

        extras = getIntent().getExtras();

        // get Topic Name and User Name from StartActivity via Intent
        final String getTopicName = extras.getString("selectedTopic");

        // Persistence of data, sets topic name to textview
        selectedTopicName.setText(getTopicName);

        // Start quiz countdown timer
        startTimer(timer);

        remainingQuestions.setText((getCurrentQuestionPosition + 1) + " / " + questionsLists.size());
        questionTextView.setText(questionsLists.get(0).getQuestion());
        option1.setText(questionsLists.get(0).getOption1());
        option2.setText(questionsLists.get(0).getOption2());
        option3.setText(questionsLists.get(0).getOption3());
        option4.setText(questionsLists.get(0).getOption4());

        // get question from Firebase Data according to selectedTopicName and assign to
        // questionsLists arraylist
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://mentalhub-4fd78-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // show dialog while questionsLists are being fetched
        ProgressDialog progressDialog = new ProgressDialog(QuizActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // getting all questionsLists from firebase realtime database for a specific topic
                for (DataSnapshot dataSnapshot : snapshot.child(getTopicName).getChildren()) {

                    // getting data from firebase database
                    final String getQuestion = dataSnapshot.child("question").getValue(String.class);
                    final String getOption1 = dataSnapshot.child("option1").getValue(String.class);
                    final String getOption2 = dataSnapshot.child("option2").getValue(String.class);
                    final String getOption3 = dataSnapshot.child("option3").getValue(String.class);
                    final String getOption4 = dataSnapshot.child("option4").getValue(String.class);
                    final String getAnswer = dataSnapshot.child("answer").getValue(String.class);

                    // adding data to the questionsList
                    QuestionsLists questionsList = new QuestionsLists(getQuestion, getOption1, getOption2, getOption3, getOption4, getAnswer);
                    questionsLists.add(questionsList);
                }

                // hide dialog
                progressDialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        option1.setOnClickListener((v) -> {
            if (selectedOptionByUser.isEmpty()) {
                selectedOptionByUser = option1.getText().toString();

                option1.setBackgroundResource(R.drawable.round_back_red10);
                option1.setTextColor(Color.WHITE);

                revealAnswer();

                questionsLists.get(getCurrentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);
            }
        });

        option2.setOnClickListener((v) -> {
            if (selectedOptionByUser.isEmpty()) {
                selectedOptionByUser = option2.getText().toString();

                option2.setBackgroundResource(R.drawable.round_back_red10);
                option2.setTextColor(Color.WHITE);

                revealAnswer();

                questionsLists.get(getCurrentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);
            }
        });

        option3.setOnClickListener((v) -> {
            if (selectedOptionByUser.isEmpty()) {
                selectedOptionByUser = option3.getText().toString();

                option3.setBackgroundResource(R.drawable.round_back_red10);
                option3.setTextColor(Color.WHITE);

                revealAnswer();

                questionsLists.get(getCurrentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);
            }
        });

        option4.setOnClickListener((v) -> {
            if (selectedOptionByUser.isEmpty()) {
                selectedOptionByUser = option4.getText().toString();

                option4.setBackgroundResource(R.drawable.round_back_red10);
                option4.setTextColor(Color.WHITE);

                revealAnswer();

                questionsLists.get(getCurrentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);
            }
        });

        backIcon.setOnClickListener((v) -> {
            onBackPressed();
        });

        nextBtn.setOnClickListener((v) -> {
            if(selectedOptionByUser.isEmpty()) {
                Toast.makeText(
                        QuizActivity.this,
                        "Choose an option",
                        Toast.LENGTH_SHORT).show();
            } else {
                changeNextQuestion();
            }
        });

    }

    private void changeNextQuestion() {
        getCurrentQuestionPosition++;

        if((getCurrentQuestionPosition+ 1) == questionsLists.size()) {
            nextBtn.setText("Submit Quiz");
        }
        if(getCurrentQuestionPosition < questionsLists.size()) {
            selectedOptionByUser = "";

            option1.setBackgroundResource(R.drawable.round_back_white_stroke2);
            option1.setTextColor(Color.parseColor("#1F6BB8"));

            option2.setBackgroundResource(R.drawable.round_back_white_stroke2);
            option2.setTextColor(Color.parseColor("#1F6BB8"));

            option3.setBackgroundResource(R.drawable.round_back_white_stroke2);
            option3.setTextColor(Color.parseColor("#1F6BB8"));

            option4.setBackgroundResource(R.drawable.round_back_white_stroke2);
            option4.setTextColor(Color.parseColor("#1F6BB8"));

            remainingQuestions.setText((getCurrentQuestionPosition + 1) + " / " + questionsLists.size());
            questionTextView.setText(questionsLists.get(getCurrentQuestionPosition).getQuestion());
            option1.setText(questionsLists.get(getCurrentQuestionPosition).getOption1());
            option2.setText(questionsLists.get(getCurrentQuestionPosition).getOption2());
            option3.setText(questionsLists.get(getCurrentQuestionPosition).getOption3());
            option4.setText(questionsLists.get(getCurrentQuestionPosition).getOption4());
        } else {
            Intent intent = new Intent(QuizActivity.this, QuizResults.class);
            intent.putExtra("correct", getCorrectAnswers());
            startActivity(intent);

            finish();
        }
    }

    private void startTimer(TextView timerTextView) {
        quizTimer = new Timer();

        // Algorithm for the timer
        quizTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(seconds == 0 && totalTimeInMins == 0) {
                    quizTimer.purge();
                    quizTimer.cancel();

                    Toast.makeText(QuizActivity.this,
                            "Time's Up!",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(QuizActivity.this, QuizResults.class);
                    intent.putExtra("correct", getCorrectAnswers());
                    startActivity(intent);

                    finish();
                } else if (seconds == 0) {
                    totalTimeInMins--;
                    seconds = 59;
                } else {
                    seconds--;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String finalMinutes = String.valueOf(totalTimeInMins);
                        String finalSeconds = String.valueOf(seconds);

                        if (finalMinutes.length() == 1) {
                            finalMinutes = "0" + finalMinutes;
                        }

                        if (finalSeconds.length() == 1) {
                            finalSeconds = "0" + finalSeconds;
                        }

                        timerTextView.setText(finalMinutes + " : " + finalSeconds);
                    }
                });
            }
        }, 1000, 1000);
    }

    private int getCorrectAnswers() {
        int correctAnswers = 0;

        for(int i = 0; i < questionsLists.size(); i++) {
            final String getUserSelectedAnswer = questionsLists.get(i).getUserSelectedAnswer();
            final String getAnswer = questionsLists.get(i).getAnswer();

            if(getUserSelectedAnswer.equals(getAnswer)) {
                correctAnswers++;
            }
        }
        return correctAnswers;
    }

    @Override
    public void onBackPressed() {
        quizTimer.purge();
        quizTimer.cancel();

        startActivity(new Intent(QuizActivity.this, Quiz.class));
        finish();
    }

    private void revealAnswer() {
        final String getAnswer = questionsLists.get(getCurrentQuestionPosition).getAnswer();

        if(option1.getText().toString().equals(getAnswer)) {
            option1.setBackgroundResource(R.drawable.round_back_green10);
            option1.setTextColor(Color.WHITE);
        } else if(option2.getText().toString().equals(getAnswer)) {
            option2.setBackgroundResource(R.drawable.round_back_green10);
            option2.setTextColor(Color.WHITE);
        } else if(option3.getText().toString().equals(getAnswer)) {
            option3.setBackgroundResource(R.drawable.round_back_green10);
            option3.setTextColor(Color.WHITE);
        } else if(option4.getText().toString().equals(getAnswer)) {
            option4.setBackgroundResource(R.drawable.round_back_green10);
            option4.setTextColor(Color.WHITE);
        }
    }

}