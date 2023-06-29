package com.example.mentalhub.progressChecker;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mentalhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class progressActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView points;

    int quizPoints = 0, breathingPoints = 0, mindPoints = 0, cognitivePoints = 0, journalPoints = 0, problemSolvingPoints = 0;

    int maxPoints = 10000;
    int previousPoints = 0;
    int combinedPoints = 0;
    CircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        circularProgressBar = findViewById(R.id.circularProgressBar);
        points = findViewById(R.id.points);

        combinedPoints = 0;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Users").child(user.getUid()).child("quizPoints").get().addOnCompleteListener(recordedQuizScore -> {
            if (recordedQuizScore.isSuccessful()) {
                if (recordedQuizScore.getResult().getValue() == null) {
                    // Child "quizPoints" does not exist, create it with the initial value of quizPoints
                    databaseReference.child("Users").child(user.getUid()).child("quizPoints").setValue(quizPoints);
                } else {
                    quizPoints = Integer.parseInt(String.valueOf(recordedQuizScore.getResult().getValue()));
                }
                databaseReference.child("Users").child(user.getUid()).child("breathingPoints").get().addOnCompleteListener(recordedBreathingScore -> {
                    if (recordedBreathingScore.isSuccessful()) {
                        if (recordedBreathingScore.getResult().getValue() == null) {
                            // Child "breathingPoints" does not exist, create it with the initial value of quizPoints
                            databaseReference.child("Users").child(user.getUid()).child("breathingPoints").setValue(breathingPoints);
                        } else {
                            breathingPoints = Integer.parseInt(String.valueOf(recordedBreathingScore.getResult().getValue()));
                        }
                        databaseReference.child("Users").child(user.getUid()).child("mindPoints").get().addOnCompleteListener(recordedMindScore -> {
                            if (recordedMindScore.isSuccessful()) {
                                if (recordedMindScore.getResult().getValue() == null) {
                                    // Child "mindPoints" does not exist, create it with the initial value of quizPoints
                                    databaseReference.child("Users").child(user.getUid()).child("mindPoints").setValue(mindPoints);
                                } else {
                                    mindPoints = Integer.parseInt(String.valueOf(recordedMindScore.getResult().getValue()));
                                }
                                databaseReference.child("Users").child(user.getUid()).child("problemSolvingPoints").get().addOnCompleteListener(recordedProblemSolvingScore -> {
                                    if (recordedProblemSolvingScore.isSuccessful()) {
                                        if (recordedProblemSolvingScore.getResult().getValue() == null) {
                                            // Child "problemSolvingPoints" does not exist, create it with the initial value of quizPoints
                                            databaseReference.child("Users").child(user.getUid()).child("problemSolvingPoints").setValue(problemSolvingPoints);
                                        } else {
                                            problemSolvingPoints = Integer.parseInt(String.valueOf(recordedProblemSolvingScore.getResult().getValue()));
                                        }
                                        databaseReference.child("Users").child(user.getUid()).child("journalPoints").get().addOnCompleteListener(recordedJournalScore -> {
                                            if (recordedJournalScore.isSuccessful()) {
                                                if (recordedJournalScore.getResult().getValue() == null) {
                                                    // Child "problemSolvingPoints" does not exist, create it with the initial value of quizPoints
                                                    databaseReference.child("Users").child(user.getUid()).child("journalPoints").setValue(journalPoints);
                                                } else {
                                                    journalPoints = Integer.parseInt(String.valueOf(recordedJournalScore.getResult().getValue()));
                                                }
                                                databaseReference.child("Users").child(user.getUid()).child("cognitivePoints").get().addOnCompleteListener(recordedCognitiveScore -> {
                                                    if (recordedCognitiveScore.isSuccessful()) {
                                                        if (recordedCognitiveScore.getResult().getValue() == null) {
                                                            // Child "cognitivePoints" does not exist, create it with the initial value of quizPoints
                                                            databaseReference.child("Users").child(user.getUid()).child("cognitivePoints").setValue(cognitivePoints);
                                                        } else {
                                                            cognitivePoints = Integer.parseInt(String.valueOf(recordedCognitiveScore.getResult().getValue()));
                                                        }
                                                        // Calculate the combinedPoints inside this last callback
                                                        combinedPoints = quizPoints + mindPoints + breathingPoints + cognitivePoints + problemSolvingPoints + journalPoints;
                                                        // Sets progress bar to specific amount of progress
                                                        circularProgressBar.setProgressWithAnimation(combinedPoints);
                                                        // Sets text of the progress checker points
                                                        String convertedCombinedPoints = String.valueOf(combinedPoints);
                                                        points.setText(convertedCombinedPoints);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
                //TODO: Get the sum of all the points gathered by the current user and place them here

            }
        });
    }
}
