package com.sp.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference the buttons
        Button buttonToReport = findViewById(R.id.report_button);
        Button post = findViewById(R.id.camera_icon);
        Button search = findViewById(R.id.search_button);
        Button chat = findViewById(R.id.chat_button);

        // Set click listeners for the buttons
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to posts activity
                Intent intent = new Intent(MainActivity.this, posts.class);
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
                Intent intent = new Intent(MainActivity.this, userSelectScreen.class);
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
    }
}
