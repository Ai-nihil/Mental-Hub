package com.example.mentalhub;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class breathing extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    // This is the added points for each completed breathing
    int breathingPoints = 10;

    private static final long START_TIME_IN_MILLIS = 4000;

    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private ImageButton breathingImage;
    private Button startButton;
    private int cycleCount = 0; // Number of breathing cycles completed
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long intervalInMillis = 1000; // Interval in seconds
    int secondsRemaining;
    int phase;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String currentDate = dateFormat.format(new Date());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breathing);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        timerTextView = findViewById(R.id.timerTextView);
        breathingImage = findViewById(R.id.breathingImage);
        startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBreathingExercise();
            }
        });
    }

    private void startBreathingExercise() {
        phase = 17;
        startButton.setEnabled(false); // Disable the button during the exercise

        countDownTimer = new CountDownTimer(17 * 1000L, intervalInMillis) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                // Adjust image scale based on the breathing phase
                if (phase <= 16 && phase > 12) {
                    inflateImage();
                } else if (phase <= 12 && phase > 8) {
                    maintainImage();
                } else if (phase <= 8 && phase > 4) {
                    deflateImage();
                } else if (phase <= 4) {
                    pauseImage();
                }
                updateTimerText();
                phase--;
                }

            @Override
            public void onFinish() {
                // Breathing exercise finished
                cycleCount++;
                if (cycleCount < 6) {
                    Toast.makeText(breathing.this, "Cycle: " + cycleCount + " / " + "6",
                            Toast.LENGTH_SHORT).show();
                    startBreathingExercise();
                } else {
                    breathingPoints = 10;
                    Toast.makeText(breathing.this, "Congratulations, you've finished the breathing exercise!",
                            Toast.LENGTH_SHORT).show();
                    cycleCount = 0;
                    startButton.setEnabled(true); // Enable the button after all cycles are completed
                    //Gets score and assigns to breathingPoints in Firebase
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference();

                    // Gets value from breathingPoints into quizBuffer
                    FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;

                    databaseReference.child("Users").child(user.getUid()).child("progress").child(currentDate).child("breathingPoints").get().addOnCompleteListener(recordedBreathingScore -> {

                        if (recordedBreathingScore.isSuccessful()) {
                            if (recordedBreathingScore.getResult().getValue() == null) {
                                // Child "breathingPoints" does not exist, create it with the initial value of quizPoints
                                databaseReference.child("Users").child(user.getUid()).child("progress").child(currentDate).child("breathingPoints").setValue(breathingPoints);
                            } else {
                                int existingBreathingPoints = Integer.parseInt(String.valueOf(recordedBreathingScore.getResult().getValue()));
                                //Adds value of existingBreathingPoints with breathingPoints
                                breathingPoints += existingBreathingPoints;
                                // Update the child "breathingPoints" with the new value
                                databaseReference.child("Users").child(user.getUid()).child("progress").child(currentDate).child("breathingPoints").setValue(breathingPoints);
                            }
                        }
                    });

                    databaseReference.child("Users").child(user.getUid()).child("breathingPoints").get().addOnCompleteListener(recordedBreathingScore -> {
                        breathingPoints = 10;
                        if (recordedBreathingScore.isSuccessful()) {
                            if (recordedBreathingScore.getResult().getValue() == null) {
                                // Child "breathingPoints" does not exist, create it with the initial value of quizPoints
                                databaseReference.child("Users").child(user.getUid()).child("breathingPoints").setValue(breathingPoints);
                            } else {
                                int existingBreathingPoints = Integer.parseInt(String.valueOf(recordedBreathingScore.getResult().getValue()));
                                //Adds value of existingBreathingPoints with breathingPoints
                                breathingPoints += existingBreathingPoints;
                                // Update the child "breathingPoints" with the new value
                                databaseReference.child("Users").child(user.getUid()).child("breathingPoints").setValue(breathingPoints);
                            }
                        }
                    });
                }
            }
        };
        countDownTimer.start();
    }

    private void inflateImage() {
        breathingImage.animate().scaleX(1.2f).scaleY(1.2f).setDuration(500).start();
    }

    private void deflateImage() {
        breathingImage.animate().scaleX(1.0f).scaleY(1.0f).setDuration(500).start();
    }

    private void maintainImage() {
        // No scale change during the hold phase
        // You can customize this method if needed
    }

    private void pauseImage() {
        // No scale change during the pause phase
        // You can customize this method if needed
    }

    private void updateTimerText() {
        secondsRemaining = (int) (mTimeLeftInMillis / 1000);
        String timerText = null;
        // Recounts
        if (phase <= 16 && phase > 12) {
            timerText = "Breathe in: " + secondsRemaining;
        } else if (phase <= 12 && phase > 8) {
            timerText = "Hold: " + secondsRemaining;
        } else if (phase <= 8 && phase > 4) {
            timerText = "Breathe out: " + secondsRemaining;
        }  else if (phase <= 4) {
            timerText = "Pause: " + secondsRemaining;
        }
        timerTextView.setText(timerText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
