package com.example.mentalhub.relaxation;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import android.graphics.text.LineBreaker;
import android.text.Layout;
import android.widget.TextView;

import com.example.mentalhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DirtGame extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    int mindPoints = 10;


    private RelativeLayout rootLayout;
    private TextView scoreTextView;
    private List<ImageView> dirtList;
    private int score;
    private ImageView dirtImageView;
    private int dirtWidth;
    private int dirtHeight;
    private ImageView windowImageView;
    private int level = 1;
    private int dirtCount;
    private TextView levelTextView;

    private boolean isGameActive = true;

    private boolean isFirstLevel = true;
    int totalScore = 0;
    private TextView quoteTextView;

    private int highScore;

    private String[] quotes = {
            "We've been taught to be ashamed or our basic human needs. Refuse to feel the shame. You are allowed to eat.",
            "I won't let a number on a scale own me",
            "I intend to accept my body today love my body tomorrow and appreciate my body always.",
            "Focus on how far you've come, not how far you have to go",
            "Binge on life. Purge negativity. Starve guilty feelings."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relax);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        rootLayout = findViewById(R.id.rootLayout);
        scoreTextView = findViewById(R.id.scoreTextView);
       // windowImageView = findViewById(R.id.windowImageView);
        levelTextView = findViewById(R.id.levelTextView);

        dirtList = new ArrayList<>();

        score = 0;
        highScore = loadHighScore();
        scoreTextView.setText("Score: " + totalScore + "     High Score: " + highScore);

        dirtImageView = new ImageView(this);
        dirtImageView.setImageResource(R.drawable.dirt);

        dirtWidth = 800;
        dirtHeight = 800;

        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            private float startX;
            private float startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float endX = event.getX();
                        float endY = event.getY();
                        handleSwipe(startX, startY, endX, endY);
                        startX = endX;
                        startY = endY;
                        break;
                    case MotionEvent.ACTION_UP:
                        float finalEndX = event.getX();
                        float finalEndY = event.getY();
                        handleSwipe(startX, startY, finalEndX, finalEndY);
                        break;
                }
                return true;
            }
        });

        levelTextView.setText("Level: " + level);
        displayDirt(5);
    }

    private void saveHighScore(int score) {
        SharedPreferences sharedPreferences = getSharedPreferences("HighScorePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("HighScore", score);
        editor.apply();
    }

    private int loadHighScore() {
        SharedPreferences sharedPreferences = getSharedPreferences("HighScorePrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("HighScore", 0);
    }

    private void startLevel() {
        checkDirtEmpty();
        switch (level) {
            case 1:
                dirtCount = 5;
                break;
            case 2:
                dirtCount = 6;
                break;
            case 3:
                dirtCount = 7;
                break;
            default:
                dirtCount = 5;
                break;
        }

        for (ImageView dirt : dirtList) {
            rootLayout.removeView(dirt);
        }
        dirtList.clear();

        levelTextView.setText("Level: " + level);

        int delayDuration = level > 1 ? 7000 : 0;

        rootLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                displayDirt(dirtCount);
            }
        }, delayDuration);
    }

    private void startNextLevel() {
        level++;
        startLevel();
    }

    private void displayDirt(int numberOfDirt) {
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();

        int maxX = screenWidth - dirtWidth;
        int maxY = screenHeight - dirtHeight;

        if (maxX <= 0 || maxY <= 0) {
            return;
        }

        Random random = new Random();
        int count = 0;

        while (count < numberOfDirt) {
            int randomX = random.nextInt(maxX);
            int randomY = random.nextInt(maxY);

            Rect newDirtRect = new Rect(randomX, randomY, randomX + dirtWidth, randomY + dirtHeight);
            boolean intersectsExistingDirt = false;

            for (ImageView dirtImageView : dirtList) {
                Rect existingDirtRect = new Rect();
                dirtImageView.getHitRect(existingDirtRect);

                if (Rect.intersects(newDirtRect, existingDirtRect)) {
                    intersectsExistingDirt = true;
                    break;
                }
            }

            if (!intersectsExistingDirt) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        dirtWidth, dirtHeight
                );
                layoutParams.leftMargin = randomX;
                layoutParams.topMargin = randomY;

                ImageView newDirtImageView = new ImageView(this);
                newDirtImageView.setImageResource(R.drawable.dirt);
                newDirtImageView.setTag(0);

                rootLayout.addView(newDirtImageView, layoutParams);
                dirtList.add(newDirtImageView);

                count++;
            }
        }
    }

    private void checkLevelCompletion() {
        if (dirtList.isEmpty()) {
            startNextLevel();
        }
    }

    private void displayInspirationalQuote() {
        mindPoints = 10;
        String quote = quotes[new Random().nextInt(quotes.length)];

        removeInspirationalQuote();

        quoteTextView = new TextView(this);
        quoteTextView.setText(quote);
        quoteTextView.setTextSize(30);
        quoteTextView.setTextColor(getResources().getColor(R.color.white));
        quoteTextView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        quoteTextView.setPadding(20, 20, 20, 20);
        quoteTextView.setTypeface(null, Typeface.BOLD);
        quoteTextView.setGravity(Gravity.CENTER);

        quoteTextView.setShadowLayer(10f, 0f, 0f, getResources().getColor(android.R.color.black));

        //Gets score and assigns to mindPoints in Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // Gets value from mindPoints
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());
        databaseReference.child("Users").child(user.getUid()).child("progress").child(currentDate).child("mindPoints").get().addOnCompleteListener(recordedMindScore -> {
            if (recordedMindScore.isSuccessful()) {
                if (recordedMindScore.getResult().getValue() == null) {
                    // Child "mindPoints" does not exist, create it with the initial value of quizPoints
                    databaseReference.child("Users").child(user.getUid()).child("progress").child(currentDate).child("mindPoints").setValue(mindPoints);
                } else {
                    int existingMindPoints = Integer.parseInt(String.valueOf(recordedMindScore.getResult().getValue()));
                    mindPoints = existingMindPoints + mindPoints;
                    // Update the child "mindPoints" with the new value
                    databaseReference.child("Users").child(user.getUid()).child("progress").child(currentDate).child("mindPoints").setValue(mindPoints);
                }
            }
        });

        databaseReference.child("Users").child(user.getUid()).child("mindPoints").get().addOnCompleteListener(recordedMindScore -> {
            mindPoints = 10;
            if (recordedMindScore.isSuccessful()) {
                if (recordedMindScore.getResult().getValue() == null) {
                    // Child "mindPoints" does not exist, create it with the initial value of quizPoints
                    databaseReference.child("Users").child(user.getUid()).child("mindPoints").setValue(mindPoints);
                } else {
                    int existingMindPoints = Integer.parseInt(String.valueOf(recordedMindScore.getResult().getValue()));
                    mindPoints = existingMindPoints + mindPoints;
                    // Update the child "mindPoints" with the new value
                    databaseReference.child("Users").child(user.getUid()).child("mindPoints").setValue(mindPoints);
                }
            }
        });
        RelativeLayout.LayoutParams quoteLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        quoteLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        rootLayout.addView(quoteTextView, quoteLayoutParams);

        quoteTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                removeInspirationalQuote();
                scoreTextView.setText("Score: " + totalScore + "    High Score: " + highScore);
            }
        }, 7000);
    }

    private void removeInspirationalQuote() {
        if (quoteTextView != null) {
            rootLayout.removeView(quoteTextView);
            quoteTextView = null;
        }
    }

    private void handleSwipe(float startX, float startY, float endX, float endY) {
        List<ImageView> dirtToRemove = new ArrayList<>();

        if (!isGameActive) {
            return;
        }
        boolean allDirtRemoved = true;
        for (ImageView dirt : dirtList) {
            int swipeCount = (int) dirt.getTag();
            Rect dirtRect = new Rect();
            dirt.getHitRect(dirtRect);
            if (dirtRect.contains((int) startX, (int) startY) && dirtRect.contains((int) endX, (int) endY)) {
                swipeCount++;
                dirt.setTag(swipeCount);
                if (swipeCount >= 30) {
                    dirtToRemove.add(dirt);
                }
            } else {
                allDirtRemoved = false;
            }
        }
        if (!dirtToRemove.isEmpty()) {
            for (ImageView dirt : dirtToRemove) {
                dirtList.remove(dirt);
                rootLayout.removeView(dirt);
            }

            checkLevelCompletion();
        }
        if (allDirtRemoved) {
        }

    }

    private void checkDirtEmpty() {
        if (dirtList.isEmpty()) {
            Log.d("DirtGame", "No more dirt left!");
            if (level != 1) {
                displayInspirationalQuote();
            }
            totalScore += 10;
            scoreTextView.setText("Score: " + totalScore + "    High Score: " + highScore);

            if (totalScore > highScore) {
                highScore = totalScore;
                saveHighScore(highScore);
                scoreTextView.setText("Score: " + totalScore + "    High Score: " + highScore);
            }
        }
    }
}
