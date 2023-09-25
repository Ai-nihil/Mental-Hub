package com.example.mentalhub;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Register extends AppCompatActivity {

    EditText editTextName, editTextUsername, editTextEmail, editTextPassword;
    Button registerButton;
    RadioGroup radioGroup;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        mAuth = FirebaseAuth.getInstance();
        editTextName = findViewById(R.id.name);
        editTextUsername = findViewById(R.id.username);
        editTextEmail = findViewById(R.id.emailAdd);
        editTextPassword = findViewById(R.id.password);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);
        radioGroup = findViewById(R.id.radioGroup);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // Add an OnClickListener to the "Register" button
        textView.setOnClickListener((v) -> {
            // Create an Intent to navigate to the Register activity
            Intent intent = new Intent(getApplicationContext(), Register.class);
            startActivity(intent);
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, username, email, password;
                name = String.valueOf(editTextName.getText());
                username = String.valueOf(editTextUsername.getText());
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                int selectedId = radioGroup.getCheckedRadioButtonId();
                String userType;
                if (selectedId == R.id.radioButtonUser) {
                    userType = "patient";
                } else if (selectedId == R.id.radioButtonPsychologist) {
                    userType = "psychologist";
                } else {
                    // Handle case when no radio button is selected
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    editTextName.setError("Enter your name");
                } else if (TextUtils.isEmpty(username)) {
                    editTextUsername.setError("Enter your username");
                } else if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Enter your email");
                } else if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Enter your password");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        // If sign in succeeds, display a message to the user.
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("name", name);
                                        hashMap.put("email", email);
                                        hashMap.put("password", password);
                                        hashMap.put("userId", user.getUid());

                                        // Store the user type in the database
                                        hashMap.put("userType", userType);

                                        assert user != null;
                                        databaseReference.child("Users")
                                                .child(user.getUid())
                                                .setValue(hashMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(Register.this, "Authentication Successful.",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(Register.this, "" + e.getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Register.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    String currentDate = dateFormat.format(new Date());
                                    Map<String, Object> initialPoints = new HashMap<>();
                                    initialPoints.put("quizPoints", 0);
                                    initialPoints.put("breathingPoints", 0);
                                    initialPoints.put("mindPoints", 0);
                                    initialPoints.put("cognitivePoints", 0);
                                    initialPoints.put("journalPoints", 0);
                                    initialPoints.put("problemSolvingPoints", 0);
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    databaseReference.child("Users")
                                            .child(user.getUid()).child("progress").child(currentDate).setValue(initialPoints);

                                }
                            });
                }
            }
        });
    }
}
