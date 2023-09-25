package com.example.mentalhub.progressChecker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentalhub.PsychologistSide.DateListActivity;
import com.example.mentalhub.PsychologistSide.PatientDataActivity;
import com.example.mentalhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import android.util.Log;
import android.widget.Toast;

public class progressActivity extends AppCompatActivity {

    CircularProgressBar circularProgressBar;
    TextView points;

    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        circularProgressBar = findViewById(R.id.circularProgressBar);
        points = findViewById(R.id.points);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String userId = user.getUid();
        Button openProgressButton = findViewById(R.id.ShowProgress);

        openProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add any logic you need to perform before opening DateListActivity here
               // String userId = getIntent().getStringExtra("userId");
                // Create an Intent to open the DateListActivity
                Intent intent = new Intent(progressActivity.this, DateListActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
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

                    // Check if each value is not null before assigning to int
                    int quizPoints = (quizPointsValue != null) ? quizPointsValue : 0;
                    int breathingPoints = (breathingPointsValue != null) ? breathingPointsValue : 0;
                    int mindPoints = (mindPointsValue != null) ? mindPointsValue : 0;
                    int cognitivePoints = (cognitivePointsValue != null) ? cognitivePointsValue : 0;
                    int journalPoints = (journalPointsValue != null) ? journalPointsValue : 0;
                    int problemSolvingPoints = (problemSolvingPointsValue != null) ? problemSolvingPointsValue : 0;

                    int combinedPoints = quizPoints + breathingPoints + mindPoints + cognitivePoints + journalPoints + problemSolvingPoints;

                    updateUI(combinedPoints);


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(progressActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(int combinedPoints) {
        circularProgressBar.setProgressWithAnimation(combinedPoints);
        points.setText(String.valueOf(combinedPoints));
    }
}