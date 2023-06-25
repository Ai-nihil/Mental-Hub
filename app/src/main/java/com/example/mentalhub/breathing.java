package com.example.mentalhub;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class breathing extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breathing);

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
                    startBreathingExercise();
                } else {
                    cycleCount = 0;
                    startButton.setEnabled(true); // Enable the button after all cycles are completed
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
