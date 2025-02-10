package com.sp.mad_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class profileCreation extends AppCompatActivity {

    private final int GALLERY_REQ_CODE = 1000;
    Uri imageUri;
    Button btnUpload, btnSubmit;
    EditText Biography;
    FirebaseStorage storage;
    FirebaseFirestore db;
    FirebaseAuth auth;
    ImageView imgGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        imgGallery = findViewById(R.id.imgGallery);
        Biography = findViewById(R.id.biography);
        btnUpload = findViewById(R.id.btnUpload);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    uploadBio(Biography.getText().toString());
                    Intent intent = new Intent(profileCreation.this, questionaire.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(profileCreation.this, "Please select an image first", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(profileCreation.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(profileCreation.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveImageUrlToFirestore(String userId, String imageUrl) {
        Map<String, Object> user = new HashMap<>();
        user.put("profilepictureUrl", imageUrl);
        db.collection("users").document(userId)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(profileCreation.this, "Profile picture updated!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(profileCreation.this, "Error saving profile picture", Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadBio(String Bio){
        FirebaseUser currentUser = auth.getCurrentUser();
        String userId = currentUser.getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("Bio", Bio);
        db.collection("users").document(userId)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(profileCreation.this, "BIO set", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(profileCreation.this, "Error saving BIO", Toast.LENGTH_SHORT).show();
                });
    }

}