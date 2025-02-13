package com.sp.mad_project;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class PreviewActivity extends AppCompatActivity {

    private Uri imageUri;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        ImageButton backButton = findViewById(R.id.backButton);
        ImageView imagePreview = findViewById(R.id.imagePreview);
        EditText captionInput = findViewById(R.id.captionInput);
        Button postButton = findViewById(R.id.postButton);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString);
            imagePreview.setImageURI(imageUri);
        }

        backButton.setOnClickListener(v -> finish());

        postButton.setOnClickListener(v -> {
            if (imageUri != null && auth.getCurrentUser() != null) {
                uploadImageAndSavePost(imageUri, captionInput.getText().toString());
            } else {
                Toast.makeText(this, "Error: No image selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImageAndSavePost(Uri fileUri, String caption) {
        String userId = auth.getCurrentUser().getUid();
        String fileName = UUID.randomUUID().toString();
        StorageReference imageRef = storageRef.child("posts/" + userId + "/" + fileName);

        imageRef.putFile(fileUri).addOnSuccessListener(taskSnapshot ->
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    savePostToFirestore(userId, uri.toString(), caption);
                })
        ).addOnFailureListener(e ->
                Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    private void savePostToFirestore(String userId, String imageUrl, String caption) {
        HashMap<String, Object> post = new HashMap<>();
        post.put("imageUrl", imageUrl);
        post.put("timestamp", System.currentTimeMillis());

        db.collection("users").document(userId)
                .collection("posts").add(post)
                .addOnSuccessListener(documentReference -> {
                    // Show success message
                    Toast.makeText(this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show();

                    // Intent to go back to MainActivity
                    Intent intent = new Intent(PreviewActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Optional: Clear the current activity from stack
                    startActivity(intent);
                    finish(); // Optional: Finish the current activity to prevent going back to it
                })
                .addOnFailureListener(e -> {
                    // Show error message
                    Toast.makeText(this, "Error uploading post", Toast.LENGTH_SHORT).show();
                });

    }
}
