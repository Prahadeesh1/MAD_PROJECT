package com.sp.mad_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Posts extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        ImageButton backButton = findViewById(R.id.backButton);
        Button doneButton = findViewById(R.id.doneButton);
        ImageView uploadIcon = findViewById(R.id.uploadIcon);

        uploadIcon.setOnClickListener(v -> openFileChooser());
        doneButton.setOnClickListener(v -> {
            if (imageUri != null) {
                Intent intent = new Intent(Posts.this, PreviewActivity.class);
                intent.putExtra("imageUri", imageUri.toString());
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(v -> finish());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
        }
    }
}
