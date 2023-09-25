// PatientView.java
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

import java.util.ArrayList;
import java.util.List;
import com.google.firebase.database.ValueEventListener;

// PatientView.java
public class PatientView extends AppCompatActivity implements UserAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientview);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this); // Pass 'this' as the itemClickListener
        recyclerView.setAdapter(userAdapter);

        // Retrieve the user data from Firebase and add it to the userList
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        usersRef.orderByChild("userType").equalTo("patient").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        user.setUserId(userSnapshot.getKey()); // Set the userId from the snapshot key

                        userList.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
            }
        });
    }

    @Override
    public void onItemClick(User user) {
        Toast.makeText(this, "Clicked user: " + user.getName(), Toast.LENGTH_SHORT).show();


        // Start a new activity to show the selected user's details (PatientDataActivity)
        Intent intent = new Intent(this, PatientDataActivity.class);
        intent.putExtra("userId", user.getUserId());
        startActivity(intent);


    }
}

