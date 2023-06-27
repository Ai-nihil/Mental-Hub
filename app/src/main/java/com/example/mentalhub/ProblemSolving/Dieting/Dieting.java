package com.example.mentalhub.ProblemSolving.Dieting;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mentalhub.R;

import java.util.ArrayList;
import java.util.List;

public class Dieting extends AppCompatActivity {

    private long mLastClickTime = 0;
    int currentResourceImg = 1;

    ImageView dietingBgImgView, eyeIcon;

    Button choiceButton1, choiceButton2, choiceButton3, choiceButton4;

    String choiceButtonText1, choiceButtonText2, choiceButtonText3, choiceButtonText4;
    String eyeOpenIconImage = "https://img.icons8.com/?size=512&id=986&format=png";
    String eyeCloseIconImage = "https://img.icons8.com/?size=512&id=121539&format=png";

    Boolean eyeStateIsOpen = true;
    Boolean choiceHasBeenMade = true;

    List<Integer> tagWithChoice = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieting);

        eyeIcon = findViewById(R.id.eye);
        dietingBgImgView = findViewById(R.id.dietingBg1);
        choiceButton1 = findViewById(R.id.choice1);
        choiceButton2 = findViewById(R.id.choice2);
        choiceButton3 = findViewById(R.id.choice3);
        choiceButton4 = findViewById(R.id.choice4);

        //Initially make the choices not visible
        choiceButton1.setVisibility(View.INVISIBLE);
        choiceButton2.setVisibility(View.INVISIBLE);
        choiceButton3.setVisibility(View.INVISIBLE);
        choiceButton4.setVisibility(View.INVISIBLE);

        //add value on tagWithChoice array
        tagWithChoice.add(2);
        tagWithChoice.add(3);
        tagWithChoice.add(4);
        tagWithChoice.add(5);
        tagWithChoice.add(14);
        tagWithChoice.add(15);
        tagWithChoice.add(16);
        tagWithChoice.add(17);

        //Set phone default to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Bring image to front to make it clickable.
        eyeIcon.bringToFront();

        // OnClickListener for the imageview
        dietingBgImgView.setOnClickListener((v) -> {
            //TODO: Add tag values for each image to switch between them
            //Algorithm to prevent double click
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            //Makes the eye and choice invisible
            if (choiceHasBeenMade) {
                currentResourceImg++;
                //Iterate through the tagWithChoice array
                for (int i = 0; i < tagWithChoice.size(); i++) {
                    //If there is a match execute code where choice and eye button becomes visible
                    if (currentResourceImg == tagWithChoice.get(i)) {
                        makeEyeButtonVisible();
                    }
                }
                // Returns all options into not selected
                deselectOptions();
                // Clears the texts
                clearButtonText();
                // Switch case for when no button is needed to be clicked
                changeCurrentScreen();
                // Makes the button invisible first then
                makeChoiceButtonInvisible();
                // makes Choice button visible
                makeChoiceButtonVisible();
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
                case "Go out for pizza with friends":
                    currentResourceImg = 13 - 1;
                    break;
                case "Seek professional help":
                    currentResourceImg = 8 - 1;
                    break;
                case "Invite her friends out for some pizza":
                    currentResourceImg = 3 - 1;
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
                case "Reflect upon one's actions and focus on changing the mindset":
                    currentResourceImg = 7 - 1;
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
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void changeCurrentScreen() {
        // Switch case for when no button is needed to be clicked
        switch (currentResourceImg) {
            case 2:
                dietingBgImgView.setImageResource(R.drawable.dieting2);
                choiceButton2.setText("Go out for pizza with friends");
                choiceButton3.setText("She did not go out for Pizza with friends");
                break;
            case 3:
                dietingBgImgView.setImageResource(R.drawable.dieting3);
                choiceButton1.setText("Pursue one's fixation towards becoming thinner");
                choiceButton2.setText("Invite her friends out for some pizza");
                choiceButton3.setText("Reflect upon one's actions and focus on changing the mindset");
                choiceButton4.setText("Reflect upon one's actions");
                break;
            case 4:
                dietingBgImgView.setImageResource(R.drawable.dieting4);
                choiceButton1.setText("Focus more attention on one's weight");
                choiceButton2.setText("Seek professional help");
                break;
            case 5:
                dietingBgImgView.setImageResource(R.drawable.dieting5);
                choiceButton1.setText("Focus on changing one's mindset");
                choiceButton2.setText("Seek professional help");
                break;
            case 6:
                dietingBgImgView.setImageResource(R.drawable.dieting6);
                break;
            case 7:
                dietingBgImgView.setImageResource(R.drawable.dieting7);
                break;
            case 8:
                dietingBgImgView.setImageResource(R.drawable.dieting8);
                break;
            case 9:
                dietingBgImgView.setImageResource(R.drawable.dieting9);
                break;
            case 10:
                dietingBgImgView.setImageResource(R.drawable.dieting10);
                break;
            case 11:
                dietingBgImgView.setImageResource(R.drawable.dieting11);
                break;
            case 12:
                dietingBgImgView.setImageResource(R.drawable.dieting12);
                break;
            case 13:
                dietingBgImgView.setImageResource(R.drawable.dieting13);
                break;
            case 14:
                dietingBgImgView.setImageResource(R.drawable.dieting14);
                break;
            case 15:
                dietingBgImgView.setImageResource(R.drawable.dieting15);
                break;
            case 16:
                dietingBgImgView.setImageResource(R.drawable.dieting16);
                break;
            case 17:
                dietingBgImgView.setImageResource(R.drawable.dieting17);
                break;
            case 18:
                dietingBgImgView.setImageResource(R.drawable.dieting18);
                break;
            case 19:
                dietingBgImgView.setImageResource(R.drawable.dieting19);
                break;
            case 20:
                dietingBgImgView.setImageResource(R.drawable.dieting20);
                break;
            case 21:
                dietingBgImgView.setImageResource(R.drawable.dieting21);
                break;
            case 22:
                dietingBgImgView.setImageResource(R.drawable.dieting22);
                break;
            case 23:
                dietingBgImgView.setImageResource(R.drawable.dieting23);
                break;
            case 24:
                dietingBgImgView.setImageResource(R.drawable.dieting24);
                break;
            case 25:
                dietingBgImgView.setImageResource(R.drawable.dieting25);
                break;
            case 26:
                dietingBgImgView.setImageResource(R.drawable.dieting26);
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
        Glide.with(Dieting.this).load(eyeOpenIconImage).centerCrop().into(eyeIcon);

        eyeIcon.setOnClickListener((View v) -> {
            if (eyeStateIsOpen) {
                //If the eye is open close it and make the choices invisible
                eyeStateIsOpen = false;
                Glide.with(Dieting.this).load(eyeCloseIconImage).centerCrop().into(eyeIcon);
                makeChoiceButtonInvisible();
            } else {
                //If the eye is closed open it and make the choices visible
                eyeStateIsOpen = true;
                Glide.with(Dieting.this).load(eyeOpenIconImage).centerCrop().into(eyeIcon);
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