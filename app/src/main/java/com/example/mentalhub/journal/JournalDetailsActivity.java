package com.example.mentalhub.journal;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentalhub.R;
import com.example.mentalhub.models.Journal;
import com.example.mentalhub.utils.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JournalDetailsActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    int journalPoints = 5;
    int bonusStreak = 0;
    int bonusPoints = 50;

    RadioGroup emotions;
    RadioButton emotion;
    RadioButton amazing, happy, neutral, sad, angry;

    // Creating global variables
    private EditText datePht;
    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;
    String title, content, docId, storedEmotion;
    boolean isEditMode = false;
    Button deleteJournalBtn;
    String tagText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_details);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // Initializing variables
        datePht = findViewById(R.id.note_date);
        pageTitleTextView = findViewById(R.id.page_title);
        titleEditText = findViewById(R.id.note_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        deleteJournalBtn = findViewById(R.id.delete_note_text_view_btn);

        // emotions radio group
        emotions = (RadioGroup) findViewById(R.id.note_emotion_group);
        amazing = findViewById(R.id.amazing);
        happy = findViewById(R.id.happy);
        neutral = findViewById(R.id.neutral);
        sad = findViewById(R.id.sad);
        angry = findViewById(R.id.angry);

        // receive data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");
        storedEmotion = getIntent().getStringExtra("storedEmotion");

        emotions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Get the checked radio button
                int radioButtonID = emotions.getCheckedRadioButtonId();
                emotion = emotions.findViewById(radioButtonID);
                tagText = emotion.getTag().toString();
                getRadioButtonTagEmotion(tagText);
            }
        });

        if (docId != null && !docId.isEmpty()) {
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);

        // If user is not in editmode they cannot see the delete button
        if (isEditMode) {
            pageTitleTextView.setText("Update Your Journal");
            deleteJournalBtn.setVisibility(View.VISIBLE);
            getRadioButtonTagEmotion(storedEmotion);
        }

        // Sets the date to current time when creating or updating journal
        datePht.setText(Utility.timeStampToString(Timestamp.now()));

        // OnClickListener for save button
        saveNoteBtn.setOnClickListener((v) -> saveNote());

        // OnClickListener for delete button, asks user first before deleting
        deleteJournalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Confirmation message for deleting the journal entry
                AlertDialog.Builder builder = new AlertDialog.Builder(JournalDetailsActivity.this);
                builder.setMessage("Do you want to delete your journal?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteNoteFromFirebase();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO Add something here
                    }
                });
                AlertDialog mDialog = builder.create();
                mDialog.show();
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
            }
        });

        // for our pick date button
        datePht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // passing context.
                        JournalDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // setting date to our edit text.
                                datePht.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        },
                        /*
                        passing year,
                        month and day for selected date in our date picker.
                        */
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });
    }

    void saveNote() {
        //Adds count to the bonus streak
        bonusStreak++;
        journalPoints = 5;

        String date = datePht.getText().toString();
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        String noteEmotion = tagText;

        if (noteTitle == null || noteTitle.isEmpty()) {
            Log.w(TAG, "saveNote:failure");
            titleEditText.setError("Title is required");
            return;
        } else if (noteEmotion == null) {
            Log.w(TAG, "saveNote:failure");
            Toast.makeText(JournalDetailsActivity.this, "Please select today's emotion", Toast.LENGTH_SHORT).show();
            return;
        } else if (date == null || date.isEmpty()) {
            Log.w(TAG, "saveNote:failure");
            datePht.setError("Correct Date is required");
            return;
        }

        Journal journal = new Journal();
        journal.setTitle(noteTitle);
        journal.setContent(noteContent);
        journal.setDate(date);
        journal.setEmotion(noteEmotion);
        journal.setTimestamp(Timestamp.now());

        saveJournalToFirebase(journal);

        //Gets score and assigns to quizPoints in Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // Gets value from quizPoints into quizBuffer
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        if (bonusStreak < 7) {
            // Adds points to journalPoints

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(new Date());
            databaseReference.child("Users").child(user.getUid()).child("progress").child(currentDate).child("journalPoints").get().addOnCompleteListener(recordedJournalScore -> {
                if (recordedJournalScore.isSuccessful()) {
                    if (recordedJournalScore.getResult().getValue() == null) {
                        // Child "journalPoints" does not exist, create it with the initial value of journalPoints
                        databaseReference.child("Users").child(user.getUid()).child("progress").child(currentDate).child("journalPoints").setValue(journalPoints);
                    } else {
                        int existingJournalPoints = Integer.parseInt(String.valueOf(recordedJournalScore.getResult().getValue()));
                        journalPoints = existingJournalPoints + journalPoints;
                        // Update the child "journalPoints" with the new value
                        databaseReference.child("Users").child(user.getUid()).child("progress").child(currentDate).child("journalPoints").setValue(journalPoints);
                    }
                }
            });

            databaseReference.child("Users").child(user.getUid()).child("journalPoints").get().addOnCompleteListener(recordedJournalScore -> {
                journalPoints = 5;
                if (recordedJournalScore.isSuccessful()) {
                    if (recordedJournalScore.getResult().getValue() == null) {
                        // Child "journalPoints" does not exist, create it with the initial value of journalPoints
                        databaseReference.child("Users").child(user.getUid()).child("journalPoints").setValue(journalPoints);
                    } else {
                        int existingJournalPoints = Integer.parseInt(String.valueOf(recordedJournalScore.getResult().getValue()));
                        journalPoints = existingJournalPoints + journalPoints + bonusPoints;
                        // Update the child "journalPoints" with the new value
                        databaseReference.child("Users").child(user.getUid()).child("journalPoints").setValue(journalPoints);
                    }
                }
            });
        } else {
            // Adds bonus points
            bonusStreak = 0;

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(new Date());
            // Adds points to journalPoints
            databaseReference.child("Users").child(user.getUid()).child("progress").child(currentDate).child("journalPoints").get().addOnCompleteListener(recordedJournalScore -> {
                if (recordedJournalScore.isSuccessful()) {
                    if (recordedJournalScore.getResult().getValue() == null) {
                        // Child "journalPoints" does not exist, create it with the initial value of journalPoints
                        databaseReference.child("Users").child(user.getUid()).child("progress").child(currentDate).child("journalPoints").setValue(journalPoints);
                    } else {
                        int existingJournalPoints = Integer.parseInt(String.valueOf(recordedJournalScore.getResult().getValue()));
                        journalPoints = existingJournalPoints + journalPoints + bonusPoints;
                        // Update the child "journalPoints" with the new value
                        databaseReference.child("Users").child(user.getUid()).child("progress").child(currentDate).child("journalPoints").setValue(journalPoints);
                    }
                }
            });

            databaseReference.child("Users").child(user.getUid()).child("journalPoints").get().addOnCompleteListener(recordedJournalScore -> {
             journalPoints = 5;
                if (recordedJournalScore.isSuccessful()) {
                    if (recordedJournalScore.getResult().getValue() == null) {
                        // Child "journalPoints" does not exist, create it with the initial value of journalPoints
                        databaseReference.child("Users").child(user.getUid()).child("journalPoints").setValue(journalPoints);
                    } else {
                        int existingJournalPoints = Integer.parseInt(String.valueOf(recordedJournalScore.getResult().getValue()));
                        journalPoints = existingJournalPoints + journalPoints + bonusPoints;
                        // Update the child "journalPoints" with the new value
                        databaseReference.child("Users").child(user.getUid()).child("journalPoints").setValue(journalPoints);
                    }
                }
            });
        }

    }

    void saveJournalToFirebase(Journal journal) {
        DocumentReference documentReference;
        if (isEditMode) {
            documentReference = Utility.getCollectionReferenceForJournal().document(docId);
        } else {
            documentReference = Utility.getCollectionReferenceForJournal().document();
        }

        documentReference.set(journal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //journal is added
                    Toast.makeText(JournalDetailsActivity.this,
                            "Journal Added Successfully!", Toast.LENGTH_SHORT).show();
                    if (isEditMode) {
                        Toast.makeText(JournalDetailsActivity.this,
                                "Journal Updated Successfully!", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                } else {
                    Toast.makeText(JournalDetailsActivity.this,
                            "Journal Not Added.", Toast.LENGTH_SHORT).show();
                    if (isEditMode) {
                        Toast.makeText(JournalDetailsActivity.this,
                                "Journal Update Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    void deleteNoteFromFirebase() {
        DocumentReference documentReference;

        documentReference = Utility.getCollectionReferenceForJournal().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //journal is added
                    Toast.makeText(JournalDetailsActivity.this,
                            "Journal Deleted Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(JournalDetailsActivity.this,
                            "Journal Not Deleted.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deselectAllRadioButton() {
        angry.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.angry_bw));
        sad.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.sad_bw));
        neutral.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.neutral_bw));
        happy.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.happy_bw));
        amazing.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.amazed_bw));
    }

    private void getRadioButtonTagEmotion(String textOfTag) {
        deselectAllRadioButton();

        switch (textOfTag) {
            case "angry":
                angry.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.angry));
                break;
            case "sad":
                sad.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.sad));
                break;
            case "neutral":
                neutral.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.neutral));
                break;
            case "happy":
                happy.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.happy));
                break;
            case "amazing":
                amazing.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.amazing));
                break;
        }
    }
}
