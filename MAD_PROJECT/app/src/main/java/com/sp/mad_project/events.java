package com.sp.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class events extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ImageAdapter adapter;
    private List<ImageModel> imageList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);


        viewPager = findViewById(R.id.viewPager);
        imageList = new ArrayList<>();

        imageList.add(new ImageModel(R.drawable.image6));
        imageList.add(new ImageModel(R.drawable.image7));
        imageList.add(new ImageModel(R.drawable.image8));


        adapter = new ImageAdapter(this, imageList);
        viewPager.setAdapter(adapter);

        // Reference the buttons
        ImageButton buttonToReport = findViewById(R.id.report_button);
        ImageButton post = findViewById(R.id.camera_icon);
        ImageButton search = findViewById(R.id.search_button);
        ImageButton chat = findViewById(R.id.chat_button);
        Button main = findViewById(R.id.for_you_button);

        // Set click listeners for the buttons
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to posts activity
                Intent intent = new Intent(events.this, posts.class);
                startActivity(intent);
            }
        });
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to posts activity
                Intent intent = new Intent(events.this, MainActivity.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Search activity
                Intent intent = new Intent(events.this, Search.class);
                startActivity(intent);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Search activity
                Intent intent = new Intent(events.this, userSelectScreen.class);
                startActivity(intent);
            }
        });

        buttonToReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ReportActivity
                Intent intent = new Intent(events.this, ReportActivity.class);
                startActivity(intent);
            }
        });
    }
}
