package com.example.mentalhub.journal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mentalhub.R;
import com.example.mentalhub.cognitive.VPAdapter;
import com.example.mentalhub.cognitive.ViewPagerItem;

import java.util.ArrayList;

public class TutorialJournal extends AppCompatActivity implements View.OnClickListener {

    ViewPager2 viewPager2;
    ArrayList<ViewPagerItem> viewPagerItemArrayList;
    Button openActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial2);

        viewPager2 = findViewById(R.id.viewpager);
        openActivityButton = findViewById(R.id.openActivityButton);

        int[] images = {R.drawable.journalin_tut};
      //  String[] heading = {"How to Play", "How to Play", "How to Play", "How to Play", "How to Play"};
      //  String[] desc = {
      //          getString(R.string.a_desc),
       //         getString(R.string.b_desc),
      //          getString(R.string.c_desc),
   //           getString(R.string.d_desc),
      //          getString(R.string.e_desc)
      //  };

        viewPagerItemArrayList = new ArrayList<>();

        for (int i = 0; i < images.length; i++) {
            ViewPagerItem viewPagerItem = new ViewPagerItem(images[i]);
            viewPagerItemArrayList.add(viewPagerItem);
        }

        VPAdapter vpAdapter = new VPAdapter(viewPagerItemArrayList);

        viewPager2.setAdapter(vpAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(2);
        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        openActivityButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.openActivityButton) {
            openAnotherActivity();
        }
    }

    private void openAnotherActivity() {
        Intent intent = new Intent(this, Journaling.class);
        startActivity(intent);
    }
}
