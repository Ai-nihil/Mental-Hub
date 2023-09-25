package com.example.mentalhub.PsychologistSide;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentalhub.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class PatientDataActivity extends AppCompatActivity {

    CircularProgressBar circularProgressBar;
    TextView points;
    TextView quizPoints;
    TextView breathingPoints;
    TextView mindPoints;
    TextView cognitivePoints;
    TextView journalPoints;
    TextView problemSolvingPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_data);

        circularProgressBar = findViewById(R.id.circularProgressBar);
        points = findViewById(R.id.points);
        quizPoints = findViewById(R.id.quizPoints);
        breathingPoints = findViewById(R.id.breathingPoints);
        mindPoints = findViewById(R.id.mindPoints);
        cognitivePoints = findViewById(R.id.cognitivePoints);
        journalPoints = findViewById(R.id.journalPoints);
        problemSolvingPoints = findViewById(R.id.problemSolvingPoints);

        Button openDateListButton = findViewById(R.id.openDateListButton);

        openDateListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add any logic you need to perform before opening DateListActivity here
                String userId = getIntent().getStringExtra("userId");
                // Create an Intent to open the DateListActivity
                Intent intent = new Intent(PatientDataActivity.this, DateListActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        String userId = getIntent().getStringExtra("userId");
        Log.d("PatientDataActivity", "Received userId: " + userId); // Log the received userId

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Integer quizPointsValue = dataSnapshot.child("quizPoints").getValue(Integer.class);
                    Integer breathingPointsValue = dataSnapshot.child("breathingPoints").getValue(Integer.class);
                    Integer mindPointsValue = dataSnapshot.child("mindPoints").getValue(Integer.class);
                    Integer cognitivePointsValue = dataSnapshot.child("cognitivePoints").getValue(Integer.class);
                    Integer journalPointsValue = dataSnapshot.child("journalPoints").getValue(Integer.class);
                    Integer problemSolvingPointsValue = dataSnapshot.child("problemSolvingPoints").getValue(Integer.class);

                    int combinedPoints = calculateCombinedPoints(quizPointsValue, breathingPointsValue, mindPointsValue, cognitivePointsValue, journalPointsValue, problemSolvingPointsValue);

                    // Update the UI
                    updateUI(combinedPoints, quizPointsValue, breathingPointsValue, mindPointsValue, cognitivePointsValue, journalPointsValue, problemSolvingPointsValue);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PatientDataActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int calculateCombinedPoints(Integer... points) {
        int combinedPoints = 0;
        for (Integer point : points) {
            if (point != null) {
                combinedPoints += point;
            }
        }
        return combinedPoints;
    }

    private void updateUI(int combinedPoints, Integer quizPointsValue, Integer breathingPointsValue, Integer mindPointsValue, Integer cognitivePointsValue, Integer journalPointsValue, Integer problemSolvingPointsValue) {
        circularProgressBar.setProgressWithAnimation(combinedPoints);
        points.setText(String.valueOf(combinedPoints));

        quizPoints.setText("Quiz Points: " + (quizPointsValue != null ? quizPointsValue : 0));
        breathingPoints.setText("Breathing Points: " + (breathingPointsValue != null ? breathingPointsValue : 0));
        mindPoints.setText("Mind Points: " + (mindPointsValue != null ? mindPointsValue : 0));
        cognitivePoints.setText("Cognitive Points: " + (cognitivePointsValue != null ? cognitivePointsValue : 0));
        journalPoints.setText("Journal Points: " + (journalPointsValue != null ? journalPointsValue : 0));
        problemSolvingPoints.setText("Problem Solving Points: " + (problemSolvingPointsValue != null ? problemSolvingPointsValue : 0));
    }
}