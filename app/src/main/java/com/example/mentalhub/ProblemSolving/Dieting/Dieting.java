package com.example.mentalhub.ProblemSolving.Dieting;

import androidx.appcompat.app.AppCompatActivity;

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
    Boolean canClickNextToContinue = true;
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
        choiceButton1.setVisibility(View.GONE);
        choiceButton2.setVisibility(View.GONE);
        choiceButton3.setVisibility(View.GONE);
        choiceButton4.setVisibility(View.GONE);

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
                    //If there is a match execute code where choice button becomes visible
                    if (currentResourceImg == tagWithChoice.get(i)) {
                        makeEyeButtonVisible();
                        makeChoiceButtonVisible();
                    }
                }
                switch (currentResourceImg) {
                    case 2:
                        if (choiceHasBeenMade) {
                            makeEyeButtonVisible();
                            makeChoiceButtonVisible();
                            dietingBgImgView.setImageResource(R.drawable.dieting2);
                        }
                        break;
                    case 3:
                        if (choiceHasBeenMade) {
                            makeEyeButtonVisible();
                            makeChoiceButtonVisible();
                            dietingBgImgView.setImageResource(R.drawable.dieting3);
                        }
                        break;
                    case 4:
                        dietingBgImgView.setImageResource(R.drawable.dieting4);
                        break;
                    case 5:
                        dietingBgImgView.setImageResource(R.drawable.dieting5);
                        break;
                }
            }
            choiceHasBeenMade = false;
        });

        //Switch case for choiceButton1
        choiceButton1.setOnClickListener((View v) -> {
            // The text of the choices will be used as the basis as to what point the story is at.
            choiceButtonText1 = String.valueOf(choiceButton1.getText());
            choiceHasBeenMade = true;
            switch(choiceButtonText1) {
                case "Spend time with her friends":
                    makeChoiceButtonInvisible();
                    break;
                case "Pursue one's fixation towards becoming thinner":
                    break;
            }
        });

        //Switch case for choiceButton2
        choiceButton2.setOnClickListener((View v) -> {
            // The text of the choices will be used as the basis as to what point the story is at.
            choiceButtonText2 = String.valueOf(choiceButton2.getText());
            choiceHasBeenMade = true;
            switch(choiceButtonText2) {
                case "Go out for pizza with friends":
                    makeChoiceButtonInvisible();
                    break;
            }
        });

        //Switch case for choiceButton3
        choiceButton3.setOnClickListener((View v) -> {
            // The text of the choices will be used as the basis as to what point the story is at.
            choiceButtonText3 = String.valueOf(choiceButton3.getText());
            choiceHasBeenMade = true;
            switch(choiceButtonText3) {
                case "She did not go out for Pizza with friends":
                    break;
                case "Reflect upon one's actions and focus on changing the mindset":
            }
        });

        //Switch case for choiceButton4
        choiceButton4.setOnClickListener((View v) -> {
            // The text of the choices will be used as the basis as to what point the story is at.
            choiceButtonText4 = String.valueOf(choiceButton4.getText());
            choiceHasBeenMade = true;
            switch(choiceButtonText4) {
                case "Reflect upon one's actions":
                    makeChoiceButtonInvisible();
                    break;
            }
        });
    }

    void makeChoiceButtonInvisible() {
        choiceButton1.setVisibility(View.GONE);
        choiceButton2.setVisibility(View.GONE);
        choiceButton3.setVisibility(View.GONE);
        choiceButton4.setVisibility(View.GONE);
    }

    void makeChoiceButtonVisible() {
        choiceButton1.setVisibility(View.VISIBLE);
        choiceButton2.setVisibility(View.VISIBLE);
        choiceButton3.setVisibility(View.VISIBLE);
        choiceButton4.setVisibility(View.VISIBLE);
    }

    void makeEyeButtonVisible() {
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

    void makeEyeButtonInvisible() {
        eyeIcon.setVisibility(View.GONE);
    }

}