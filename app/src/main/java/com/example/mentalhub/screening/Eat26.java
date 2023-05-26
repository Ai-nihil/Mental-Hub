package com.example.mentalhub.screening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mentalhub.R;
import com.example.mentalhub.Register;
import com.example.mentalhub.journal.JournalDetailsActivity;
import com.example.mentalhub.models.Journal;
import com.example.mentalhub.utils.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Eat26 extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    RadioGroup question1, question2, question3, question4, question5, question6, question7, question8,
            question9, question10, question11, question12, question13, question14, question15, question16,
            question17, question18, question19, question20, question21, question22, question23, question24,
            question25, question26;

    int result, dietingPoints, bulimiaPoints, oralControlPoints;
    int radioGroupListSnapShot;
    boolean allQuestionsAnswered = false;

    // Based on EAT26 ARTICLE
    int[] dietingNumbers = {1, 6, 7, 10, 11, 12, 14, 16, 17, 22, 23, 24, 26};
    int[] bulimiaNumbers = {3, 4, 9, 18, 21, 25};
    int[] oralControlNumbers = {2, 5, 8, 13, 15, 19, 20};

    Button submitButton;
    List<Integer> arrayList = new ArrayList<>();
    List<RadioGroup> radioGroupList = new ArrayList<>();
    RadioButton radioButtonBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat26);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

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
        submitButton = findViewById(R.id.submitButton);

        radioGroupList = Arrays.asList(question1, question2, question3, question4, question5,
                question6, question7, question8, question9, question10, question11, question12,
                question13, question14, question15, question16, question17, question18, question19,
                question20, question21, question22, question23, question24, question25, question26);

        submitButton.setOnClickListener((v) -> {
            arrayList.clear();
            allQuestionsAnswered = true;
            result = 0;
            dietingPoints = 0;
            bulimiaPoints = 0;
            oralControlPoints = 0;
            //An algorithm that will add the weight of all answers and output the value
            // that will be used to determine what the user is at the potential risk of.
            for (int i = 0; i < radioGroupList.size(); i++) {
                // Gets the radioButton checked in the radioGroup
                radioGroupListSnapShot = radioGroupList.get(i).getCheckedRadioButtonId();
                // adds the snapshotted value into the arrayList
                arrayList.add(radioGroupListSnapShot);
                // if there is radioGroup which has no radioButton that is checked
                if (radioGroupListSnapShot == -1) {
                    allQuestionsAnswered = false;
                    Toast.makeText(Eat26.this, "Please answer all the questions",
                            Toast.LENGTH_SHORT).show();
                } else if (!arrayList.contains(-1)){
                    // finds the id of the said radioButton
                    radioButtonBuffer = findViewById(radioGroupListSnapShot);
                    // gets the tag of the said radioButton
                    int tagValue = Integer.parseInt(String.valueOf(radioButtonBuffer.getTag()));

                    // adds points to the risk category based on number
                    if (contains(dietingNumbers, i + 1)) {
                        dietingPoints += tagValue;
                    } else if (contains(bulimiaNumbers, i + 1)) {
                        bulimiaPoints += tagValue;
                    } else if (contains(oralControlNumbers, i + 1)) {
                        oralControlPoints += tagValue;
                    }
                    // adds the value of the tag to the overall result
                    result += tagValue;
                }
            }

            if (allQuestionsAnswered) {
                Toast.makeText(Eat26.this, "Result: " + result,
                        Toast.LENGTH_SHORT).show();
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
                // inserts value of result into the database
                databaseReference.child("Users").child(user.getUid()).child("result").setValue(result);
                databaseReference.child("Users").child(user.getUid()).child("bulimiaPoints").setValue(bulimiaPoints);
                databaseReference.child("Users").child(user.getUid()).child("oralControlPoints").setValue(oralControlPoints);
                databaseReference.child("Users").child(user.getUid()).child("dietingPoints").setValue(dietingPoints);
                // Moves to another activity
                Intent intent = new Intent(Eat26.this, eat26results.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Function that is called to compare value
    public static boolean contains(int[] array, int key) {
        for (int i : array) {
            if (i == key) {
                return true;
            }
        }
        return false;
    }
}