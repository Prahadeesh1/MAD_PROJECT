package com.sp.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private ImageAdapter adapter;
    private List<ImageModel> imageList;
    private Set<String> imageUrls;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String currentUserPersonality;

    private static final int REQUEST_NEW_POST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        imageList = new ArrayList<>();
        imageUrls = new HashSet<>();
        adapter = new ImageAdapter(this, imageList);
        viewPager.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fetchCurrentUserPersonality(); // ✅ Fetch logged-in user's personality type

        // Reference the buttons
        ImageButton buttonToReport = findViewById(R.id.report_button);
        ImageButton post = findViewById(R.id.camera_icon);
        ImageButton search = findViewById(R.id.search_button);
        ImageButton chat = findViewById(R.id.chat_button);
        ImageButton profile = findViewById(R.id.profile_button);
        Button event = findViewById(R.id.events_button);
        ImageView likeButton = findViewById(R.id.like_button);

        // Like button toggle
        likeButton.setOnClickListener(v -> {
            likeButton.setSelected(!likeButton.isSelected());
            likeButton.setImageResource(likeButton.isSelected() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_border);
        });

        // Set click listeners for navigation
        post.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Posts.class);
            startActivityForResult(intent, REQUEST_NEW_POST);
        });

        event.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Events.class)));
        search.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Search.class)));
        chat.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UserSelectScreen.class)));
        buttonToReport.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ReportActivity.class)));
        profile.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));
    }

    // ✅ Fetch the logged-in user's personality type
    private void fetchCurrentUserPersonality() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            firestore.collection("users").document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String rawPersonality = documentSnapshot.getString("personality");
                            if (rawPersonality != null) {
                                currentUserPersonality = cleanPersonalityString(rawPersonality);
                                fetchImagesFromFirestore(); // ✅ Fetch posts after getting the personality type
                            }
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to load user data: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        }
    }

    // ✅ Fetch images and filter them based on personality
    private void fetchImagesFromFirestore() {
        firestore.collection("users").get().addOnSuccessListener(userSnapshots -> {
            imageList.clear();
            imageUrls.clear();

            for (DocumentSnapshot userDoc : userSnapshots.getDocuments()) {
                String userId = userDoc.getId();
                String rawPersonality = userDoc.getString("personality");

                if (rawPersonality == null) continue;

                String userPersonality = cleanPersonalityString(rawPersonality);
                if (!shouldSeePost(userPersonality)) continue; // ✅ Skip posts outside compatibility range

                firestore.collection("users").document(userId)
                        .collection("posts")
                        .get()
                        .addOnSuccessListener(postSnapshots -> {
                            boolean dataChanged = false;

                            for (DocumentSnapshot postDoc : postSnapshots.getDocuments()) {
                                String imageUrl = postDoc.getString("imageUrl");

                                if (imageUrl != null && !imageUrl.isEmpty() && !imageUrls.contains(imageUrl)) {
                                    imageList.add(new ImageModel(imageUrl));
                                    imageUrls.add(imageUrl);
                                    dataChanged = true;
                                }
                            }

                            if (dataChanged) {
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Failed to load posts: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to load users: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    // ✅ Determine if a post should be shown based on personality compatibility
    private boolean shouldSeePost(String userPersonality) {
        if (currentUserPersonality == null) return false;

        switch (currentUserPersonality) {
            case "Introverted, intuitive, spontaneous":
                return userPersonality.equals("Introverted, intuitive, spontaneous") ||
                        userPersonality.equals("Balanced personality");

            case "Balanced personality":
                return true; // ✅ Sees all posts

            case "Extroverted, logical, organized":
                return userPersonality.equals("Extroverted, logical, organized") ||
                        userPersonality.equals("Balanced personality");

            case "Strongly extroverted, structured, leader":
                return userPersonality.equals("Strongly extroverted, structured, leader") ||
                        userPersonality.equals("Balanced personality");

            default:
                return false;
        }
    }

    // ✅ Convert personality string by cleaning unwanted characters
    private String cleanPersonalityString(String rawPersonality) {
        return rawPersonality.replace("\"", "").replace(";", "").trim();
    }

    // ✅ Refresh only when returning from a new post
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NEW_POST && resultCode == RESULT_OK) {
            fetchImagesFromFirestore();
        }
    }
}
