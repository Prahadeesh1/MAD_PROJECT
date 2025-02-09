package com.sp.mad_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class submittingImage extends AppCompatActivity {

    private final int GALLERY_REQ_CODE = 1000;
    private Uri imageUri;

    Button btnUpload, btnSubmit;
    ImageView btnCamera, imgGallery, imgCamera;

    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private final int CAMERA_REQ_CODE = 2000;
    private String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitting_image);

        imgGallery = findViewById(R.id.imgGallery);
        imgCamera = findViewById(R.id.imgCamera);
        btnUpload = findViewById(R.id.btnUpload);
        btnCamera = findViewById(R.id.btnCamera);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Initialize Firebase
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        btnCamera.setOnClickListener(onCamera);
        btnSubmit.setOnClickListener(onSubmit);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    uploadProfilePicture();
                } else {
                    Toast.makeText(submittingImage.this, "Please select an image first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE && data != null && data.getData() != null) {
                // Image selected from gallery
                imageUri = data.getData();
                imgGallery.setImageURI(imageUri);
            } else if (requestCode == CAMERA_REQ_CODE && data != null) {
                // Image captured from camera
                String imagePath = data.getStringExtra("imageUri");
                if (imagePath != null) {
                    imageUri = Uri.parse(imagePath);
                    imgCamera.setImageURI(imageUri); // Show captured image
                }
            }
        }
    }



    private void uploadProfilePicture() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        StorageReference storageRef = storage.getReference("profile_pictures/" + userId);

        storageRef.putFile(imageUri) // `imageUri` is the selected image URI
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL of the uploaded image
                    storageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString(); // Convert the URL to a string
                                saveImageUrlToFirestore(userId, imageUrl); // Save the URL in Firestore
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(submittingImage.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(submittingImage.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveImageUrlToFirestore(String userId, String imageUrl) {
        Map<String, Object> user = new HashMap<>();
        user.put("imageUrl", imageUrl);
        db.collection("users").document(userId)
            .set(user, SetOptions.merge())
            .addOnSuccessListener(aVoid -> {
                Toast.makeText(submittingImage.this, "Profile picture updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(submittingImage.this, questionaire.class);
                startActivity(intent);
            })
            .addOnFailureListener(e -> {
                Toast.makeText(submittingImage.this, "Error saving profile picture", Toast.LENGTH_SHORT).show();
            });
    }


    private View.OnClickListener onCamera = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(submittingImage.this, Camera.class);
            startActivityForResult(intent, CAMERA_REQ_CODE); // Expect result from Camera.java
        }
    };

    private View.OnClickListener onSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(submittingImage.this, questionaire.class);
            startActivity(intent);
        }
    };

}
