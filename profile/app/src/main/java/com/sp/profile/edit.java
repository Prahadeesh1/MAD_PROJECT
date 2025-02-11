package com.sp.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class edit extends AppCompatActivity {

    private final int GALLERY_REQ_CODE = 1000;
    private Uri imageUri;
    private EditText editUsername, editBio;
    private ImageView imgGallery;
    private Button btnSave, btnUpload;
    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Initialize Firebase
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        imgGallery = findViewById(R.id.imgGallery);
        editUsername = findViewById(R.id.edit_username);
        editBio = findViewById(R.id.edit_Bio);
        btnUpload = findViewById(R.id.btnUpload);
        btnSave = findViewById(R.id.btn_save);


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

        // Save data when button is clicked
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageUri != null || !editUsername.getText().toString().trim().isEmpty() || !editBio.getText().toString().trim().isEmpty()) {
                    updateProfileDetails();
                } else{
                    Toast.makeText(edit.this, "Please select an image first", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE && data != null && data.getData() != null) {
                // Image selected from gallery
                imageUri = data.getData();
                imgGallery.setImageURI(imageUri);
            }
        }
    }

    private void updateProfileDetails() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        if (imageUri != null) {
            StorageReference storageRef = storage.getReference("profile_pictures/" + userId);
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    saveImageUrlToFirestore(userId, uri.toString(), editUsername.getText().toString(), editBio.getText().toString());
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(edit.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(edit.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            saveImageUrlToFirestore(userId, null, editUsername.getText().toString(), editBio.getText().toString());
        }
    }

    private void saveImageUrlToFirestore(String userId, String imageUrl, String Username, String Bio) {
        Map<String, Object> user = new HashMap<>();
        user.put("profilepictureUrl", imageUrl);
        user.put("username", Username);
        user.put("Bio", Bio);

        db.collection("users").document(userId)
                .update(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(edit.this, "Profile updated!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(edit.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(edit.this, "Error updated profile", Toast.LENGTH_SHORT).show();
                });
    }

}
