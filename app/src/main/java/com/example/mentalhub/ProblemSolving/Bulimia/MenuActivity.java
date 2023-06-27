package com.example.mentalhub.ProblemSolving.Bulimia;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mentalhub.ProblemSolving.Bulimia.CustomMenuAdapter;


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

        menuItems.add(new MenuItem("Bulimia", "Description for Bulimia"));
        menuItems.add(new MenuItem("Dieting", "Description for Dieting"));
        menuItems.add(new MenuItem("Oral Control", "Description for Oral Control"));

        return menuItems;
    }
}
