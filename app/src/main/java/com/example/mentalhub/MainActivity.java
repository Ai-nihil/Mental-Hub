package com.example.mentalhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // Global declaration of variables
    FirebaseAuth mAuth;
    Button logoutButton, playNowBtn, eat26, psychoeducationBtn, checkProgressBtn;
    TextView textView;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logout);
        eat26 = findViewById(R.id.eat26assessment);
        psychoeducationBtn = findViewById(R.id.psychoeducation);
        checkProgressBtn = findViewById(R.id.checkProgress);
        textView = findViewById(R.id.user_details);
        playNowBtn = findViewById(R.id.playNow);
        user = mAuth.getCurrentUser();
        // Sends user to login page if there is no session for user
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else {
            // TODO: Implement google OAuth to easily get the user's name
            textView.setText(user.getEmail());

            playNowBtn.setOnClickListener((View v) -> {
                {
                    Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                    startActivity(intent);
                }
            });

            eat26.setOnClickListener((View v) -> {
                {
                    Intent intent = new Intent(getApplicationContext(), Eat26.class);
                    startActivity(intent);
                }
            });
        }
        // Button to log out the user
        logoutButton.setOnClickListener((View v) -> {
            {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

    }
}