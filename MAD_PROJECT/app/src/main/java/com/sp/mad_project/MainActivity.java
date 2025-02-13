package com.sp.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ImageAdapter adapter;
    private List<ImageModel> imageList;
    private boolean isLiked = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewPager = findViewById(R.id.viewPager);
        imageList = new ArrayList<>();

        imageList.add(new ImageModel(R.drawable.image1));
        imageList.add(new ImageModel(R.drawable.image2));
        imageList.add(new ImageModel(R.drawable.image3));
        imageList.add(new ImageModel(R.drawable.image4));
        imageList.add(new ImageModel(R.drawable.image5));

        adapter = new ImageAdapter(this, imageList);
        viewPager.setAdapter(adapter);

        // Reference the buttons
        ImageButton buttonToReport = findViewById(R.id.report_button);
        ImageButton post = findViewById(R.id.camera_icon);
        ImageButton search = findViewById(R.id.search_button);
        ImageButton chat = findViewById(R.id.chat_button);
        ImageButton profile = findViewById(R.id.profile_button);
        Button event = findViewById(R.id.events_button);
        ImageView likeButton = findViewById(R.id.like_button);

        // Set up click listener to toggle like state
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLiked = !isLiked;  // Toggle like state

                if (isLiked) {
                    // Change to filled heart (liked state)
                    likeButton.setImageResource(R.drawable.ic_heart_filled);
                } else {
                    // Change to bordered heart (unliked state)
                    likeButton.setImageResource(R.drawable.ic_heart_border);
                }
            }
        });

        // Set click listeners for the buttons
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to posts activity
                Intent intent = new Intent(MainActivity.this, Posts.class);
                startActivity(intent);
            }
        });
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to posts activity
                Intent intent = new Intent(MainActivity.this, Events.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Search activity
                Intent intent = new Intent(MainActivity.this, Search.class);
                startActivity(intent);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Search activity
                Intent intent = new Intent(MainActivity.this, UserSelectScreen.class);
                startActivity(intent);
            }
        });

        buttonToReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ReportActivity
                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
