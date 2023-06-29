package com.example.mentalhub.ProblemSolving.Oral;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.mentalhub.ProblemSolving.MenuActivity;
import com.example.mentalhub.R;
import com.example.mentalhub.breathing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Oral extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    int problemSolvingPoints = 10;

    private long mLastClickTime = 0;
    int currentResourceImg = 1;

    ImageView oralBgImgView, eyeIcon;

    Button choiceButton1, choiceButton2, choiceButton3, choiceButton4;

    String choiceButtonText1, choiceButtonText2, choiceButtonText3, choiceButtonText4;
    String eyeOpenIconImage = "https://img.icons8.com/?size=512&id=986&format=png";
    String eyeCloseIconImage = "https://img.icons8.com/?size=512&id=121539&format=png";

    Boolean eyeStateIsOpen = true;
    Boolean choiceHasBeenMade = true;

    Boolean end = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oral);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        eyeIcon = findViewById(R.id.eye);
        oralBgImgView = findViewById(R.id.dietingBg1);
        choiceButton1 = findViewById(R.id.choice1);
        choiceButton2 = findViewById(R.id.choice2);
        choiceButton3 = findViewById(R.id.choice3);
        choiceButton4 = findViewById(R.id.choice4);

        //Initially make the choices not visible
        choiceButton1.setVisibility(View.INVISIBLE);
        choiceButton2.setVisibility(View.INVISIBLE);
        choiceButton3.setVisibility(View.INVISIBLE);
        choiceButton4.setVisibility(View.INVISIBLE);

        //Gets score and assigns to breathingPoints in Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // Gets value from breathingPoints into quizBuffer
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;

        //Set phone default to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Bring image to front to make it clickable.
        eyeIcon.bringToFront();

        // OnClickListener for the imageview
        oralBgImgView.setOnClickListener((v) -> {
            //TODO: Add tag values for each image to switch between them
            //Algorithm to prevent double click
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            //Makes the eye and choice invisible
            if (choiceHasBeenMade) {
                currentResourceImg++;
                makeEyeButtonVisible();
            }
            // Returns all options into not selected
            deselectOptions();
            // Clears the texts
            clearButtonText();
            // Switch case for changing the screen
            changeCurrentScreen();
            // Makes the button invisible first then
            makeChoiceButtonInvisible();
            if (eyeStateIsOpen) {
                // makes Choice button visible
                makeChoiceButtonVisible();
            }
            if (end) {
                // Adds problemSolvingPoints to the user with the current UID
                databaseReference.child("Users").child(user.getUid()).child("problemSolvingPoints").get().addOnCompleteListener(recordedProblemSolvingScore -> {
                    if (recordedProblemSolvingScore.isSuccessful()) {
                        if (recordedProblemSolvingScore.getResult().getValue() == null) {
                            // Child "problemSolvingPoints" does not exist, create it with the initial value of quizPoints
                            databaseReference.child("Users").child(user.getUid()).child("problemSolvingPoints").setValue(problemSolvingPoints);
                        } else {
                            int existingProblemSolvingPoints = Integer.parseInt(String.valueOf(recordedProblemSolvingScore.getResult().getValue()));
                            //Adds value of problemSolvingPoints with problemSolvingPoints
                            problemSolvingPoints += existingProblemSolvingPoints;
                            // Update the child "problemSolvingPoints" with the new value
                            databaseReference.child("Users").child(user.getUid()).child("problemSolvingPoints").setValue(problemSolvingPoints);
                        }
                    }
                });
                Toast.makeText(Oral.this, "You have found an ending!",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Oral.this, MenuActivity.class));
                finish();
            }
            //Sets choice has been made back to false
            choiceHasBeenMade = false;
        });

        /*
        NOTE: The code implements an n - 1 imageResourceLocator to quickly identify which of the
        image resource must be used for that specific choice
        */
        //Switch case for choiceButton1
        choiceButton1.setOnClickListener((View v) -> {
            // The text of the choices will be used as the basis as to what point the story is at.
            choiceButtonText1 = String.valueOf(choiceButton1.getText());
            choiceHasBeenMade = true;
            deselectOptions();
            choiceButton1.setBackgroundResource(R.drawable.round_back_light_pink);
            switch(choiceButtonText1) {
                case "Spend time with her friends":
                    currentResourceImg = 13 - 1;
                    break;
                case "Pursue one's fixation towards becoming thinner":
                    currentResourceImg = 4 - 1;
                    break;
                case "Focus more attention on one's weight":
                    currentResourceImg = 7 - 1;
                    break;
                case "Focus on changing one's mindset":
                    currentResourceImg = 9 - 1;
                    break;
                case "Avoid seeking professional help":
                    currentResourceImg = 15 - 1;
                    break;
                case "Seek Professional help":
                    currentResourceImg = 21 - 1;
                    break;
                case "Apply what she learned to herself":
                    currentResourceImg = 21 - 1;
                    break;
                case "Continue changing the mindset":
                    currentResourceImg = 23 - 1;
            }
        });

        //Switch case for choiceButton2
        choiceButton2.setOnClickListener((View v) -> {
            // The text of the choices will be used as the basis as to what point the story is at.
            choiceButtonText2 = String.valueOf(choiceButton2.getText());
            choiceHasBeenMade = true;
            deselectOptions();
            choiceButton2.setBackgroundResource(R.drawable.round_back_light_pink);
            switch(choiceButtonText2) {
                case "Consult a Therapist":
                    currentResourceImg = 3 - 1;
                    break;
                case "Challenge: Temptation":
                    currentResourceImg = 4 - 1;
                    break;
                case "Ignore Therapist's advice":
                    currentResourceImg = 5 - 1;
                    break;
                case "Consequence of Temptation":
                    currentResourceImg = 6 - 1;

                    break;
                case "Try to change based on gut feeling":
                    currentResourceImg = 9 - 1;
                    break;
                case "Challenge: Temptation ":
                    currentResourceImg = 10 - 1;
                    break;
                case "Fast Improvement":
                    currentResourceImg = 14 - 1;
                    break;
                case "Challenge: Overeating":
                    currentResourceImg = 21 - 1;
                    break;


            }
        });

        //Switch case for choiceButton3
        choiceButton3.setOnClickListener((View v) -> {
            // The text of the choices will be used as the basis as to what point the story is at.
            choiceButtonText3 = String.valueOf(choiceButton3.getText());
            choiceHasBeenMade = true;
            deselectOptions();
            choiceButton3.setBackgroundResource(R.drawable.round_back_light_pink);
            switch(choiceButtonText3) {
                case "Work on it yourself":
                    currentResourceImg = 8 - 1;
                    break;
                case "Do extensive research on how to change":
                    currentResourceImg = 20 - 1;
                    break;
                case "Challenge: Self-esteem":
                    currentResourceImg = 11 - 1;
                    break;
                case "Challenge: Emotional Eating":
                    currentResourceImg = 13 - 1;
                    break;
                case "Implement Therapist's advice":
                    currentResourceImg = 18 - 1;
                    break;
                case "Patience and Discipline":
                    currentResourceImg = 16 - 1;
                    break;
                case "Challenge: Nutrition":
                    currentResourceImg = 22 - 1;
                    break;


            }
        });

        //Switch case for choiceButton4
        choiceButton4.setOnClickListener((View v) -> {
            // The text of the choices will be used as the basis as to what point the story is at.
            choiceButtonText4 = String.valueOf(choiceButton4.getText());
            choiceHasBeenMade = true;
            deselectOptions();
            choiceButton4.setBackgroundResource(R.drawable.round_back_light_pink);
            switch(choiceButtonText4) {
                case "Reflect upon one's actions":
                    currentResourceImg = 5 - 1;
                    break;
                case "Eat healthier":
                    currentResourceImg = 17 - 1;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void changeCurrentScreen() {
        // Switch case changing the current screen
        end = false;
        switch (currentResourceImg) {

            case 2:
                oralBgImgView.setImageResource(R.drawable.start);
                choiceButton2.setText("Consult a Therapist");
                choiceButton3.setText("Work on it yourself");
                break;
            case 3:
                oralBgImgView.setImageResource(R.drawable.oralconsult);
                choiceButton2.setText("Challenge: Temptation");
                choiceButton3.setText("Challenge: Emotional Eating");

                break;
            case 4:
                oralBgImgView.setImageResource(R.drawable.oral_temptations);
                choiceButton2.setText("Ignore Therapist's advice");
                choiceButton3.setText("Implement Therapist's advice");

                break;
            case 5:
                currentResourceImg = 6;
                oralBgImgView.setImageResource(R.drawable.oral_temptationb2);


                break;
            case 6:

                oralBgImgView.setImageResource(R.drawable.oral_temptationbending);
                end = true;
                break;
            case 7:


                break;
            case 8:

                oralBgImgView.setImageResource(R.drawable.oral_yourself);
                choiceButton2.setText("Try to change based on gut feeling");
                choiceButton3.setText("Do extensive research on how to change");
                break;

            case 9:

                oralBgImgView.setImageResource(R.drawable.oral_yourselfb1);
                choiceButton2.setText("Challenge: Temptation ");
                choiceButton3.setText("Challenge: Self-esteem");

                break;
            case 10:
                currentResourceImg = 12;
                oralBgImgView.setImageResource(R.drawable.oral_yourselfb21);
                break;
            case 11:
                currentResourceImg = 12;
                oralBgImgView.setImageResource(R.drawable.oral_yourselfb22);
                break;
            case 12://end

                oralBgImgView.setImageResource(R.drawable.oral_yourselfbnending);
                end = true;
                break;
            case 13:


                 oralBgImgView.setImageResource(R.drawable.oral_emotional);
                choiceButton2.setText("Fast Improvement");
                choiceButton3.setText("Patience and Discipline");
                break;
            case 14:
                currentResourceImg = 15;
                 oralBgImgView.setImageResource(R.drawable.oral_emotionalb2);

                break;
            case 15://end

                oralBgImgView.setImageResource(R.drawable.oral_emotionalbending);
                end = true;

                break;
            case 16:
                currentResourceImg = 17;
                 oralBgImgView.setImageResource(R.drawable.oral_emotionalg2);

                break;
            case 17://end

                  oralBgImgView.setImageResource(R.drawable.oral_emotionalgending);
                end = true;

                break;
            case 18:
                currentResourceImg = 19;
                  oralBgImgView.setImageResource(R.drawable.oral_temptationg2);
                break;
            case 19:

                 oralBgImgView.setImageResource(R.drawable.oral_temptationgending);
                end = true;

                break;
            case 20:
                oralBgImgView.setImageResource(R.drawable.oral_yourself);
                choiceButton2.setText("Challenge: Overeating");
                choiceButton3.setText("Challenge: Nutrition");
                break;
            case 21:
                currentResourceImg = 23;
                oralBgImgView.setImageResource(R.drawable.oral_yourselfg22);

                break;
            case 22:
                currentResourceImg = 23;
                oralBgImgView.setImageResource(R.drawable.oral_yourselfg21);

                break;
            case 23:
                currentResourceImg = 24;
                oralBgImgView.setImageResource(R.drawable.oral_yourselfg3);

                break;
            case 24:

               oralBgImgView.setImageResource(R.drawable.oral_yourselfgending);

                end = true;
                break;

        }
    }

    private void makeChoiceButtonInvisible() {
        choiceButton1.setVisibility(View.INVISIBLE);
        choiceButton2.setVisibility(View.INVISIBLE);
        choiceButton3.setVisibility(View.INVISIBLE);
        choiceButton4.setVisibility(View.INVISIBLE);
    }

    private void makeChoiceButtonVisible() {
        if (!choiceButton1.getText().equals("")) {
            choiceButton1.setVisibility(View.VISIBLE);
        }
        if (!choiceButton2.getText().equals("")) {
            choiceButton2.setVisibility(View.VISIBLE);
        }
        if (!choiceButton3.getText().equals("")) {
            choiceButton3.setVisibility(View.VISIBLE);
        }
        if (!choiceButton4.getText().equals("")) {
            choiceButton4.setVisibility(View.VISIBLE);
        }
    }

    private void makeEyeButtonVisible() {
        Glide.with(Oral.this).load(eyeOpenIconImage).centerCrop().into(eyeIcon);

        eyeIcon.setOnClickListener((View v) -> {
            if (eyeStateIsOpen) {
                //If the eye is open close it and make the choices invisible
                eyeStateIsOpen = false;
                Glide.with(Oral.this).load(eyeCloseIconImage).centerCrop().into(eyeIcon);
                makeChoiceButtonInvisible();
            } else {
                //If the eye is closed open it and make the choices visible
                eyeStateIsOpen = true;
                Glide.with(Oral.this).load(eyeOpenIconImage).centerCrop().into(eyeIcon);
                makeChoiceButtonVisible();
            }
        });
    }

    private void makeEyeButtonInvisible() {
        eyeIcon.setVisibility(View.INVISIBLE);
    }

    private void deselectOptions() {
        choiceButton1.setBackgroundResource(R.drawable.mybutton);
        choiceButton2.setBackgroundResource(R.drawable.mybutton);
        choiceButton3.setBackgroundResource(R.drawable.mybutton);
        choiceButton4.setBackgroundResource(R.drawable.mybutton);
    }

    private void clearButtonText() {
        choiceButton1.setText("");
        choiceButton2.setText("");
        choiceButton3.setText("");
        choiceButton4.setText("");
    }

}