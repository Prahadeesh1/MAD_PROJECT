package com.sp.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class FullScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        ImageView fullScreenImage = findViewById(R.id.full_screen_image);

        // Get image resource ID from intent
        int imageResId = getIntent().getIntExtra("image_res_id", 0);
        if (imageResId != 0) {
            fullScreenImage.setImageResource(imageResId);
        }

        // Close activity when clicked
        fullScreenImage.setOnClickListener(v -> finish());
    }
}
