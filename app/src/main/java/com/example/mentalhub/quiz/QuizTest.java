package com.example.mentalhub.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mentalhub.R;
import com.example.mentalhub.models.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QuizTest extends AppCompatActivity {

    private TextView remainingQuestions, question;

    ImageView backIcon, timeIcon;
    String backIconImage = "https://cdn-icons-png.flaticon.com/512/93/93634.png";
    String timeIconImage = "https://cdn-icons-png.flaticon.com/512/18/18304.png";

    TextView timer, selectedTopicname;
    private AppCompatButton option1, option2, option3, option4, nextBtn;

    private Timer quizTimer;

    private int totalTimeInMins, seconds;

    private final List<Question> questions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_test);

        final String getSelectedTopicName = getIntent().getStringExtra("selectedTopic");

        backIcon = findViewById(R.id.backButton);
        timeIcon = findViewById(R.id.timeIcon);
        timer = findViewById(R.id.timer);
        selectedTopicname = findViewById(R.id.topicName);
        question = findViewById(R.id.question);
        remainingQuestions = findViewById(R.id.questions);
        nextBtn = findViewById(R.id.nextBtn);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        // Loads image into ImageView without storing locally.
        Glide.with(QuizTest.this).load(backIconImage).centerCrop().into(backIcon);
        Glide.with(QuizTest.this).load(timeIconImage).centerCrop().into(timeIcon);

        // Persistence of data
        selectedTopicname.setText(getSelectedTopicName);

        startTimer(timer);

        backIcon.setOnClickListener((v) -> {
            onBackPressed();
        });

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

                    Toast.makeText(QuizTest.this,
                            "Time's Up!",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(QuizTest.this, QuizResults.class);
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

        for(int i = 0; i < questions.size(); i++) {
            final String getUserSelectedAnswer = questions.get(i).getUserSelectedAnswer();
            final String getAnswer = questions.get(i).getAnswer();

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

        startActivity(new Intent(QuizTest.this, Quiz.class));
    }

}