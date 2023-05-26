package com.example.mentalhub.screening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mentalhub.R;
import com.example.mentalhub.quiz.Quiz;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class eat26results extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userId;
    TextView possibleRisk, youAreAtRiskOf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat26results);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        possibleRisk = findViewById(R.id.possibleRisk);
        youAreAtRiskOf = findViewById(R.id.youAreAtRiskOf);

        // Gets the user's EAT26 results
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        userId = user.getUid();

        // Loads image into ImageView without storing locally.
        databaseReference.child("Users").child(userId).child("result").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    if (Integer.parseInt(String.valueOf(task.getResult().getValue())) < 20) {
                        possibleRisk.setText("Nothing, you are not at risk of maladaptive eating behavior");
                    } else if (!task.getResult().exists()){
                        youAreAtRiskOf.setVisibility(View.INVISIBLE);
                        possibleRisk.setText("You need to take the EAT26 assessment first");
                    }
                }
            }
        });
    }

}