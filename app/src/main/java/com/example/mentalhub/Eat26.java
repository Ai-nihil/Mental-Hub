package com.example.mentalhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Eat26 extends AppCompatActivity {

    int[] values;
    RadioGroup question1, question2, question3, question4, question5, question6, question7, question8,
            question9, question10, question11, question12, question13, question14, question15, question16,
            question17, question18, question19, question20, question21, question22, question23, question24,
            question25, question26;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat26);

        question1 = findViewById(R.id.question1);
        question2 = findViewById(R.id.question2);
        question3 = findViewById(R.id.question3);
        question4 = findViewById(R.id.question4);
        question5 = findViewById(R.id.question5);
        question6 = findViewById(R.id.question6);
        question7 = findViewById(R.id.question7);
        question8 = findViewById(R.id.question8);
        question9 = findViewById(R.id.question9);
        question10 = findViewById(R.id.question10);
        question11 = findViewById(R.id.question11);
        question12 = findViewById(R.id.question12);
        question13 = findViewById(R.id.question13);
        question14 = findViewById(R.id.question14);
        question15 = findViewById(R.id.question15);
        question16 = findViewById(R.id.question16);
        question17 = findViewById(R.id.question17);
        question18 = findViewById(R.id.question18);
        question19 = findViewById(R.id.question19);
        question20 = findViewById(R.id.question20);
        question21 = findViewById(R.id.question21);
        question22 = findViewById(R.id.question22);
        question23 = findViewById(R.id.question23);
        question24 = findViewById(R.id.question24);
        question25 = findViewById(R.id.question25);
        question26 = findViewById(R.id.question26);

        // Create an array of values to assign to the RadioButtons
        values = new int[]{3, 2, 1, 0, 0, 0};

        // Loop through the RadioGroup's child views and assign values to the RadioButtons
        for (int i = 0; i < question1.getChildCount(); i++) {
            View view = question1.getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) view;
                radioButton.setText("Option " + (i+1)); // Set the text of the RadioButton
                radioButton.setTag(values[i]); // Set the tag value to the corresponding value in the array
            }
        }

        //TODO: Create an algorithm that will add the weight of all answers and output the value
        // that will be used to determine what the user is at the potential risk of.

        // Get the selected RadioButton and its tag value
        question1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                int tagValue = (int) radioButton.getTag();
                // Use the tagValue as needed
            }
        });

    }

}