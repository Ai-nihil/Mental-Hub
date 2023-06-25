package com.example.mentalhub.ProblemSolving.Bulimia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mentalhub.R;

public class Bulimia extends AppCompatActivity {

    int currentResourceImg;
    ImageView bulimiaBgImgView;
    Button choiceButton1, choiceButton2, choiceButton3;
    String choiceButtonText1, choiceButtonText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulimia);

        bulimiaBgImgView = findViewById(R.id.bulimiaBg);
        choiceButton1 = findViewById(R.id.choice1);
        choiceButton2 = findViewById(R.id.choice2);
        choiceButton3 = findViewById(R.id.choice3);

        //Initially make the choices not visible
        //choiceButton1.setVisibility(View.GONE);
        //choiceButton2.setVisibility(View.GONE);
        //choiceButton3.setVisibility(View.GONE);

        //currentResourceImg == tag value of the current image;

        //Set phone default to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Bring image to front to make it clickable.
        bulimiaBgImgView.bringToFront();

        // OnClickListener for the imageview
        bulimiaBgImgView.setOnClickListener((View v) -> {
            //TODO: Add tag values for each image to switch between them

            bulimiaBgImgView.setImageResource(R.drawable.background_splashscreen);
        });

        //Switch case for choiceButton1
        choiceButton1.setOnClickListener((View v) -> {
            // The text of the choices will be used as the basis as to what point the story is at.
            choiceButtonText1 = String.valueOf(choiceButton1.getText());
            switch(choiceButtonText1) {
                case "Don't Seek Professional Help and hide the struggles":
                    badEndingTemplate();
                    break;
                case "Struggle with Academics":
                    break;
            }
        });

        //Switch case for choiceButton2
        choiceButton2.setOnClickListener((View v) -> {
            // The text of the choices will be used as the basis as to what point the story is at.
            choiceButtonText2 = String.valueOf(choiceButton2.getText());
            switch(choiceButtonText2) {
                case "Seek Professional Help":
                    badEndingTemplate();
                    break;
            }
        });
    }

void badEndingTemplate() {
        //Removes all choices to give way to the text
        //Replace background of ImageView with a more appropriate image
        //Set tag values on the image to retrieve them later
        //TODO: Code for the above statement
        choiceButton1.setVisibility(View.GONE);
        choiceButton2.setVisibility(View.GONE);
        choiceButton3.setVisibility(View.GONE);
}


}