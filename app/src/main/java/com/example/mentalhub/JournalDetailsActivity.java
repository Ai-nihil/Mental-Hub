package com.example.mentalhub;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.lang.annotation.Documented;
import java.util.Calendar;

public class JournalDetailsActivity extends AppCompatActivity {

    // Creating global variables
    private EditText datePht;
    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_details);

        // Initializing variables
        datePht = findViewById(R.id.note_date);
        titleEditText = findViewById(R.id.note_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);

        saveNoteBtn.setOnClickListener((v) -> saveNote());

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
        String date = datePht.getText().toString();
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        if(noteTitle == null || noteTitle.isEmpty() ) {
            Log.w(TAG, "saveNote:failure");
            titleEditText.setError("Title is required");
            return;
        }  else if (date == null || date.isEmpty()) {
            Log.w(TAG, "saveNote:failure");
            datePht.setError("Correct Date is required");
            datePht.setText(Utility.timeStampToString(Timestamp.now()));
            return;
        }


        Journal journal = new Journal();
        journal.setTitle(noteTitle);
        journal.setContent(noteContent);
        journal.setDate(date);
        journal.setTimestamp(Timestamp.now());

        saveJournalToFirebase(journal);
    }

    void saveJournalToFirebase(Journal journal) {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForJournal().document();

        documentReference.set(journal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    //journal is added
                    Toast.makeText(JournalDetailsActivity.this,
                            "Journal Added Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(JournalDetailsActivity.this,
                            "Journal Not Added.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
