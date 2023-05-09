package com.example.mentalhub.psychoeducation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mentalhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class LessonActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    // creating variables for
    // our ui components.
    private RecyclerView recyclerView;
    // variable for our adapter
    // class and array list
    private MyListAdapter myListAdapter;
    private ArrayList<MyListData> myListDataArrayList;
    private SearchView searchView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lesson);

            searchView = findViewById(R.id.search_view);
            searchView.clearFocus();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterList(newText);
                    return true;
                }
            });

            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // No need to store this into firebase
            myListDataArrayList = new ArrayList<>();
            myListDataArrayList.add(new MyListData("Intro to CBT: Why Thoughts Matter",
                    R.drawable.cbt,
                    "https://www.therapistaid.com/therapy-article/cbt-why-thoughts-matter/cbt/adolescents"));
            myListDataArrayList.add(new MyListData("The Benefits of Mindfulness",
                    R.drawable.mindfullness,
                    "https://www.therapistaid.com/therapy-article/benefits-of-mindfulness/dbt/adolescents"));
            myListDataArrayList.add(new MyListData("How to do Deep Breathing",
                    R.drawable.breathrelax,
                    "https://www.therapistaid.com/therapy-video/deep-breathing-exercise/relaxation/none"));
            myListDataArrayList.add(new MyListData("Positive Psychology Techniques",
                    R.drawable.positivepsych,
                    "https://www.therapistaid.com/therapy-guide/positive-psychology-techniques/self-esteem/none"));
            myListDataArrayList.add(new MyListData("Relaxation Techniques",
                    R.drawable.relaxation,
                    "https://www.therapistaid.com/therapy-guide/relaxation-skills-guide/emotions/none#visualization"));

            myListAdapter = new MyListAdapter(myListDataArrayList);
            recyclerView.setAdapter(myListAdapter);

        }

    private void filterList(String text) {
        List<MyListData> filteredList = new ArrayList<>();
        for (MyListData myListData : myListDataArrayList) {
            if (myListData.getDescription().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(myListData);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
        } else {
            myListAdapter.setFilteredList(filteredList);
        }

    }
}