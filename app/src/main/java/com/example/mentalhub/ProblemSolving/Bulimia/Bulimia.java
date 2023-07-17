package com.example.mentalhub.ProblemSolving.Bulimia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mentalhub.ProblemSolving.MenuActivity;
import com.example.mentalhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Bulimia extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    int problemSolvingPoints = 10;

    private long mLastClickTime = 0;
    int currentResourceImg = 1;

    ImageView bulimiaBgImgView, eyeIcon;

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
        setContentView(R.layout.activity_bulimia);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        eyeIcon = findViewById(R.id.eye);
        bulimiaBgImgView = findViewById(R.id.bulimiaBg);
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
        bulimiaBgImgView.setOnClickListener((v) -> {
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
                makeEyeButtonVisible();
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
                Toast.makeText(Bulimia.this, "You have found an ending!",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Bulimia.this, MenuActivity.class));
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
                case "Don't seek professional help and hide the struggle":
                    currentResourceImg = 3 - 1;
                    break;
                case "Struggle with Academics":
                    currentResourceImg = 5 - 1;
                    break;
                case "Set realistic goals and realistic academic schedule":
                    currentResourceImg = 7 - 1;
                    break;
                case "Set realistic goals and manage her stress in a healthier manner":
                    currentResourceImg = 10 - 1;
                    break;
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
                case "Seek professional help":
                    currentResourceImg = 15 - 1;
                    break;
                case "Struggle with Love":
                    currentResourceImg = 17 - 1;
                    break;
                case "She will live up to their expectations. She can't let these people down":
                    currentResourceImg = 9 - 1;
                    break;
                case "Ignore academic responsibilities to relieve herself of stress":
                    currentResourceImg = 11 - 1;
                    break;
                case "Stay with her boyfriend":
                    currentResourceImg = 20 - 1;
                    break;
                case "She tried to help her parents get back together":
                    currentResourceImg = 25 - 1;
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
                case "She did not go out for Pizza with friends":
                    currentResourceImg = 3 - 1;
                    break;
                case "Struggle with Family":
                    currentResourceImg = 26 - 1;
                    break;
                case "Break up with her boyfriend":
                    currentResourceImg = 21 - 1;
                    break;
                case "Seek professional help":
                    currentResourceImg = 15 - 1;
                    break;
                case "Keep turning to food for comfort":
                    currentResourceImg = 27 - 1;
                    break;
                case "She gave up":
                    currentResourceImg = 33 - 1;
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
                case "Seek professional help again":
                    currentResourceImg = 30 - 1;
                    break;
                case "Continue on her life without seeking help":
                    currentResourceImg = 23 - 1;
                    break;
                case "Find other outlets":
                case "She kept trying":
                    currentResourceImg = 28 - 1;
                    break;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void changeCurrentScreen() {
        end = false;
        // Switch case changing the current screen
        switch (currentResourceImg) {
            case 2:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia2);
                choiceButton2.setText("Don't seek professional help and hide the struggle");
                choiceButton3.setText("Seek professional help");
                break;
            case 3:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia3);
                currentResourceImg = 4;
                break;
            case 4:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia4);
                currentResourceImg = 6;
                break;
            case 5:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia5);
                choiceButton1.setText("Set realistic goals and realistic academic schedule");
                choiceButton2.setText("She will live up to their expectations. She can't let these people down");
                break;
            case 6:
                //TODO: exit
                end = true;
                bulimiaBgImgView.setImageResource(R.drawable.bulimia6);
                break;
            case 7:
                currentResourceImg = 8;
                bulimiaBgImgView.setImageResource(R.drawable.bulimia7);
                break;
            case 8:
                //TODO: exit
                end = true;
                bulimiaBgImgView.setImageResource(R.drawable.bulimia8);
                break;
            case 9:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia9);
                choiceButton1.setText("Set realistic goals and realistic academic schedule");
                choiceButton2.setText("Ignore academic responsibilities to relieve herself of stress");
                break;
            case 10:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia10);
                currentResourceImg = 8;
                break;
            case 11:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia11);
                currentResourceImg = 12;
                break;
            case 12:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia12);
                currentResourceImg = 13;
                break;
            case 13:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia13);
                currentResourceImg = 14;
                break;
            case 14:
                //TODO: exit
                end = true;
                bulimiaBgImgView.setImageResource(R.drawable.bulimia14);
                break;
            case 15:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia15);
                choiceButton1.setText("Seek Professional help");
                choiceButton2.setText("Ignore her feelings");
                break;
            case 16:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia16);
                choiceButton1.setText("Struggle with Academics");
                choiceButton2.setText("Struggle with Love");
                choiceButton3.setText("Struggle with Family");
                break;
            case 17:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia17);
                currentResourceImg = 18;
                break;
            case 18:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia18);
                currentResourceImg = 19;
                break;
            case 19:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia19);
                choiceButton2.setText("Stay with her boyfriend");
                choiceButton3.setText("Break up with her boyfriend");
                choiceButton4.setText("Seek professional help again");
                break;
            case 20:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia20);
                currentResourceImg = 22;
                break;
            case 21:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia21);
                choiceButton3.setText("Seek professional help");
                choiceButton4.setText("Continue on her life without seeking help");
                break;
            case 22:
                //TODO: exit
                end = true;
                bulimiaBgImgView.setImageResource(R.drawable.bulimia22);
                break;
            case 23:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia23);
                currentResourceImg = 24;
                break;
            case 24:
                //TODO: exit
                end = true;
                bulimiaBgImgView.setImageResource(R.drawable.bulimia24);
                break;
            case 25:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia25);
                currentResourceImg = 27;
                break;
            case 26:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia26);
                choiceButton2.setText("She tried to help her parents get back together");
                choiceButton3.setText("Keep turning to food for comfort");
                choiceButton4.setText("Find other outlets");
                break;
            case 27:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia27);
                choiceButton3.setText("She gave up");
                choiceButton4.setText("She kept trying");
                break;
            case 28:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia28);
                currentResourceImg = 29;
                break;
            case 29:
                //TODO: exit
                end = true;
                bulimiaBgImgView.setImageResource(R.drawable.bulimia29);
                break;
            case 30:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia30);
                currentResourceImg = 31;
                break;
            case 31:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia31);
                currentResourceImg = 32;
                break;
            case 32:
                //TODO: Exit
                end = true;
                bulimiaBgImgView.setImageResource(R.drawable.bulimia32);
                break;
            case 33:
                bulimiaBgImgView.setImageResource(R.drawable.bulimia33);
                currentResourceImg = 34;
                break;
            case 34:
                //TODO: Exit
                end = true;
                bulimiaBgImgView.setImageResource(R.drawable.bulimia34);
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
        Glide.with(Bulimia.this).load(eyeOpenIconImage).centerCrop().into(eyeIcon);

        eyeIcon.setOnClickListener((View v) -> {
            if (eyeStateIsOpen) {
                //If the eye is open close it and make the choices invisible
                eyeStateIsOpen = false;
                Glide.with(Bulimia.this).load(eyeCloseIconImage).centerCrop().into(eyeIcon);
                makeChoiceButtonInvisible();
            } else {
                //If the eye is closed open it and make the choices visible
                eyeStateIsOpen = true;
                Glide.with(Bulimia.this).load(eyeOpenIconImage).centerCrop().into(eyeIcon);
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