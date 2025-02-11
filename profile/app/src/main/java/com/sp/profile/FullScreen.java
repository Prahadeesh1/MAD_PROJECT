package com.sp.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class FullScreen extends AppCompatActivity {

    private ImageView fullScreenImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        fullScreenImage = findViewById(R.id.full_screen_image);

        // Retrieve image URL from intent
        String imageUrl = getIntent().getStringExtra("image_url");

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(fullScreenImage);
        }

        // Close fullscreen on click
        fullScreenImage.setOnClickListener(v -> finish());
    }
}
