// ProgressDetailActivity.java
package com.example.mentalhub.progressChecker;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentalhub.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProgressDetailActivity extends AppCompatActivity {
    private TextView progressDetailTextView;

    CircularProgressBar circularProgressBar;
    TextView points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_2);
        circularProgressBar = findViewById(R.id.circularProgressBar);

        points = findViewById(R.id.points);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());

        // Retrieve the selected user's UID and date from the intent extras
        String userId = getIntent().getStringExtra("userId");
        String date = getIntent().getStringExtra("date");

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("progress").child(date);
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
               // Toast.makeText(progressActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void updateUI(int combinedPoints) {
        circularProgressBar.setProgressWithAnimation(combinedPoints);
        points.setText(String.valueOf(combinedPoints));
    }
}
