package com.example.mentalhub.ProblemSolving;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mentalhub.R;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CustomMenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuAdapter = new CustomMenuAdapter(getMenuItems(), this);

        recyclerView.setAdapter(menuAdapter);
    }

    private List<MenuItem> getMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();

        menuItems.add(new MenuItem("Bulimia", "The preoccupation with thoughts about food and attempts to vomit food eaten during a binge"));
        menuItems.add(new MenuItem("Dieting", "The preoccupation with being thinner and avoidance of fattening foods"));
        menuItems.add(new MenuItem("Oral Control", "Attempts to maintain self-control while eating and the perceived pressure from others to gain weight"));

        return menuItems;
    }
}
