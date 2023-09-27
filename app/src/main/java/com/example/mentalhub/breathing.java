package com.example.mentalhub;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
    private VideoView videoView;
    private MediaController mediaController;
    private int cycleCount = 0; // Number of breathing cycles completed
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long intervalInMillis = 1000; // Interval in seconds
    private int secondsRemaining;
    private boolean isBreathingExerciseRunning = false;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String currentDate = dateFormat.format(new Date());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breathing);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        videoView = findViewById(R.id.videoView);
        timerTextView = findViewById(R.id.timerTextView);
        breathingImage = findViewById(R.id.breathingImage);
        startButton = findViewById(R.id.startButton);
        mediaController = new MediaController(this);

        // Hides breathing image and timer
        breathingImage.setVisibility(View.INVISIBLE);
        timerTextView.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.INVISIBLE);

        // Define the delay time in milliseconds
        int delayMillis = 1000; // 5000 milliseconds (5 seconds)

        // Create a Handler
        Handler handler = new Handler();

        // Use the handler to post a delayed message to start video playback
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Plays video
                String path = "android.resource://com.example.mentalhub/" + R.raw.breathingvideo;
                Uri uri = Uri.parse(path);
                videoView.setVideoURI(uri);
                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoView);
                videoView.start();
            }
        }, delayMillis);

        // Set an OnCompletionListener
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Video playback has completed
                Toast.makeText(getApplicationContext(), "Video finished playing", Toast.LENGTH_SHORT).show();
                startButton.setVisibility(View.VISIBLE);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBreathingExercise();
            }
        });
    }

    private void startBreathingExercise() {
        // Prevent starting a new exercise if one is already running
        if (isBreathingExerciseRunning) {
            return;
        }

        startButton.setEnabled(false); // Disable the button during the exercise
        videoView.stopPlayback(); // Stops video from playing
        videoView.setVisibility(View.GONE); // Hides the video
        breathingImage.setVisibility(View.VISIBLE);
        timerTextView.setVisibility(View.VISIBLE);

        countDownTimer = new CountDownTimer(16 * 1000, intervalInMillis) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;

                // Calculate seconds remaining based on millisUntilFinished
                secondsRemaining = (int) Math.ceil((double) millisUntilFinished / 1000);

                // Adjust image scale based on the breathing phase
                if (secondsRemaining >= 12) {
                    inflateImage();
                } else if (secondsRemaining >= 8) {
                    maintainImage();
                } else if (secondsRemaining >= 4) {
                    deflateImage();
                } else {
                    pauseImage();
                }
                // Update the timer text using secondsRemaining
                updateTimerText(secondsRemaining);
            }

            @Override
            public void onFinish() {
                // Breathing exercise finished
                cycleCount++;
                isBreathingExerciseRunning = false; // Set the flag to false
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
        isBreathingExerciseRunning = true; // Set the flag to true when exercise starts
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

    private void updateTimerText(int secondsRemaining) {
        String timerText = null;
        // Recounts
        if (secondsRemaining >= 12) {
            timerText = "Inhale through your nose: " + secondsRemaining;
        } else if (secondsRemaining >= 8) {
            timerText = "Hold your breathe: " + secondsRemaining;
        } else if (secondsRemaining >= 4) {
            timerText = "Exhale through your mouth: " + secondsRemaining;
        }  else {
            timerText = "Rest and wait: " + secondsRemaining;
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
