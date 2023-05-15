package com.example.mentalhub.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mentalhub.R;
import com.example.mentalhub.R.*;

public class Quiz extends AppCompatActivity {

    private String selectedTopicName = "";

    ImageView quizIcon, cbtIcon, mindfulnessIcon, positivepsychIcon, relaxationIcon;
    String quizIconImage = "https://www.pngmart.com/files/19/Vector-Quiz-Transparent-PNG.png";
    String cbtIconImage = "https://www.nicepng.com/png/full/396-3968386_mental-clarity-cognitive-behavioral-therapy.png";
    String mindfulnessIconImage = "https://www.pngall.com/wp-content/uploads/12/Mindfulness-Meditation-PNG-Picture.png";
    String positivepsychIconImage = "https://cdn2.iconfinder.com/data/icons/human-psychology-4/64/x-13-512.png";
    String relaxationIconImage = "https://static.vecteezy.com/system/resources/previews/013/261/186/original/girl-doing-yoga-and-meditating-yoga-love-freedom-happiness-health-free-png.png";

    LinearLayout cbtLayout, mindfulnessLayout, positivepsychLayout, relaxationLayout;
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // ImageView instantiate
        quizIcon = findViewById(R.id.quizIcon);
        cbtIcon = findViewById(R.id.cbtIcon);
        mindfulnessIcon = findViewById(R.id.mindfulnessIcon);
        positivepsychIcon = findViewById(R.id.positivepsychIcon);
        relaxationIcon = findViewById(R.id.relaxationIcon);

        // LinearLayout instantiate
        cbtLayout = findViewById(R.id.cbtLayout);
        mindfulnessLayout = findViewById(R.id.mindfulnessLayout);
        positivepsychLayout = findViewById(R.id.positivepsychLayout);
        relaxationLayout = findViewById(R.id.relaxationLayout);

        // Button instantiate
        startButton = findViewById(R.id.startButton);

        // Loads image into ImageView without storing locally.
        Glide.with(Quiz.this).load(quizIconImage).centerCrop().into(quizIcon);
        Glide.with(Quiz.this).load(cbtIconImage).centerCrop().into(cbtIcon);
        Glide.with(Quiz.this).load(mindfulnessIconImage).centerCrop().into(mindfulnessIcon);
        Glide.with(Quiz.this).load(positivepsychIconImage).centerCrop().into(positivepsychIcon);
        Glide.with(Quiz.this).load(relaxationIconImage).centerCrop().into(relaxationIcon);

        cbtLayout.setOnClickListener((v) -> {
            chooseTopic("cbtLayout", v);
        });

        mindfulnessLayout.setOnClickListener((v) -> {
            chooseTopic("mindfulnessLayout", v);
        });

        positivepsychLayout.setOnClickListener((v) -> {
            chooseTopic("positivepsychLayout", v);
        });

        relaxationLayout.setOnClickListener((v) -> {
                chooseTopic("relaxationLayout", v);
        });

        startButton.setOnClickListener((v)-> {
            if(selectedTopicName.isEmpty()) {
                Toast.makeText(Quiz.this, "Please choose a topic", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Quiz.this, QuizActivity.class);
                intent.putExtra("selectedTopic", selectedTopicName);
                startActivity(intent);
                finish();
            }
        });
    }
    private void chooseTopic(String topicName, View topicLayout) {

        // assign java to selectedTopicName
        selectedTopicName = topicName;

        // de-select other layouts
        deSelectAllLayouts();

        // select java layout
        topicLayout.setBackgroundResource(drawable.round_back_white_stroke);
    }

    private void deSelectAllLayouts() {

        cbtLayout.setBackgroundResource(drawable.gradient_background);
        relaxationLayout.setBackgroundResource(drawable.gradient_background);
        mindfulnessLayout.setBackgroundResource(drawable.gradient_background);
        positivepsychLayout.setBackgroundResource(drawable.gradient_background);
        // TODO topics must be added here
    }
}