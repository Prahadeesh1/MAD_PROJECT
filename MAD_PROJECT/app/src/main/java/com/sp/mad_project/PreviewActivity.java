package com.sp.mad_project;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class PreviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        ImageButton backButton = findViewById(R.id.backButton);
        ImageView imagePreview = findViewById(R.id.imagePreview);
        EditText captionInput = findViewById(R.id.captionInput);
        Button postButton = findViewById(R.id.postButton);

        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            imagePreview.setImageURI(imageUri);
        }

        backButton.setOnClickListener(v -> finish());
        postButton.setOnClickListener(v -> {

        });
    }
}
