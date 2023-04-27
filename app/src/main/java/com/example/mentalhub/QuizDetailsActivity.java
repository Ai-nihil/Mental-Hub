package com.example.mentalhub;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.UUID;

public class QuizDetailsActivity extends AppCompatActivity {
    EditText nameEditText;
    String id, name, docId;
    Button add;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        id = UUID.randomUUID().toString();
        nameEditText = findViewById(R.id.quizName);
        add = findViewById(R.id.quizAdd);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // receive data
        name = getIntent().getStringExtra("name");
        docId = getIntent().getStringExtra("docId");

        add.setOnClickListener((v) -> saveQuiz());
    }

    void saveQuiz () {
        String quizName = nameEditText.getText().toString();

        if (quizName == null || quizName.isEmpty()) {
            Log.w(TAG, "saveQuiz:failure");
            nameEditText.setError("Title is required");
            return;
        }

        Quiz quiz = new Quiz();
        quiz.setName(quizName);
        quiz.setTimestamp(Timestamp.now());

        saveQuizToFirebase(quiz);
    }

    void saveQuizToFirebase(Quiz quiz) {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForQuiz().document();

        documentReference.set(quiz).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Quiz is added
                    Toast.makeText(QuizDetailsActivity.this,
                            "Quiz Added Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(QuizDetailsActivity.this,
                            "Quiz Not Added.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void deleteQuizFromFirebase() {
        DocumentReference documentReference;

        //TODO: ADD DOCUMENT ID
        documentReference = Utility.getCollectionReferenceForQuiz().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //journal is added
                    Toast.makeText(QuizDetailsActivity.this,
                            "Journal Deleted Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(QuizDetailsActivity.this,
                            "Journal Not Deleted.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}