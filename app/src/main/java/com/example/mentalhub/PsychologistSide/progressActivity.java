package com.example.mentalhub.PsychologistSide;

import android.os.Bundle;
import android.util.Log;
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
import android.util.Log;
import android.widget.Toast;

public class progressActivity extends AppCompatActivity {

    CircularProgressBar circularProgressBar;
    TextView points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        circularProgressBar = findViewById(R.id.circularProgressBar);
        points = findViewById(R.id.points);

        String userId = getIntent().getStringExtra("userId");
        Log.d("progressActivity", "Received userId: " + userId); // Log the received userId

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int quizPoints = dataSnapshot.child("quizPoints").getValue(Integer.class);
                    int breathingPoints = dataSnapshot.child("breathingPoints").getValue(Integer.class);
                    int mindPoints = dataSnapshot.child("mindPoints").getValue(Integer.class);
                    int cognitivePoints = dataSnapshot.child("cognitivePoints").getValue(Integer.class);
                    int journalPoints = dataSnapshot.child("journalPoints").getValue(Integer.class);
                    int problemSolvingPoints = dataSnapshot.child("problemSolvingPoints").getValue(Integer.class);

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