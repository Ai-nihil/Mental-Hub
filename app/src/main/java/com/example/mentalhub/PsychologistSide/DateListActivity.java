// DateListActivity.java
package com.example.mentalhub.PsychologistSide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhub.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DateListActivity extends AppCompatActivity implements DateListAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private DateListAdapter dateListAdapter;
    private List<String> dateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_list);

        recyclerView = findViewById(R.id.dateRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dateList = new ArrayList<>();
        dateListAdapter = new DateListAdapter(dateList, this); // Pass 'this' as the itemClickListener
        recyclerView.setAdapter(dateListAdapter);

        // Retrieve the list of dates from Firebase for the selected user
        String userId = getIntent().getStringExtra("userId");

        // Modify the database reference to directly point to the "progress" node
        DatabaseReference progressRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("progress");
        progressRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                    String date = dateSnapshot.getKey();
                    dateList.add(date);
                }
                dateListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
            }
        });
    }

    @Override
    public void onItemClick(String date) {
        Toast.makeText(this, "Clicked date: " + date, Toast.LENGTH_SHORT).show();

        // Start a new activity to show progress for the selected date
        Intent intent = new Intent(this, ProgressDetailActivity.class);
        intent.putExtra("userId", getIntent().getStringExtra("userId"));
        intent.putExtra("date", date);
        startActivity(intent);
    }
}
