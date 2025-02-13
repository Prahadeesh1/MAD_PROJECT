package com.sp.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class Events extends AppCompatActivity {

    private ViewPager2 viewPager;
    private EventAdapter adapter;
    private List<EventModel> imageList;
    private Set<String> imageUrls;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private static final int REQUEST_NEW_POST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        viewPager = findViewById(R.id.viewPager);
        imageList = new ArrayList<>();
        imageUrls = new HashSet<>();
        adapter = new EventAdapter(this, imageList);
        viewPager.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fetchImagesFromFirestore(); // Fetch images from the "events" section in Firestore

        // Reference the buttons
        ImageButton buttonToReport = findViewById(R.id.report_button);
        ImageButton post = findViewById(R.id.camera_icon);
        ImageButton search = findViewById(R.id.search_button);
        ImageButton chat = findViewById(R.id.chat_button);
        ImageButton profile = findViewById(R.id.profile_button);
        Button foru = findViewById(R.id.for_you_button);
        ImageButton home = findViewById(R.id.home_button);

        // Set click listeners for navigation
        post.setOnClickListener(v -> {
            Intent intent = new Intent(Events.this, PEvents.class);
            startActivityForResult(intent, REQUEST_NEW_POST);
        });

        foru.setOnClickListener(v -> startActivity(new Intent(Events.this, MainActivity.class)));
        search.setOnClickListener(v -> startActivity(new Intent(Events.this, Search.class)));
        chat.setOnClickListener(v -> startActivity(new Intent(Events.this, UserSelectScreen.class)));
        buttonToReport.setOnClickListener(v -> startActivity(new Intent(Events.this, ReportActivity.class)));
        profile.setOnClickListener(v -> startActivity(new Intent(Events.this, ProfileActivity.class)));
        home.setOnClickListener(v -> startActivity(new Intent(Events.this, MainActivity.class)));
    }

    // Fetch all images from Firestore's "events" collection
    private void fetchImagesFromFirestore() {
        firestore.collection("users").get().addOnSuccessListener(userSnapshots -> {
            imageList.clear();
            imageUrls.clear();

            for (DocumentSnapshot userDoc : userSnapshots.getDocuments()) {
                String userId = userDoc.getId();

                // Fetch images from the "events" collection (not from posts)
                firestore.collection("users").document(userId)
                        .collection("events")
                        .get()
                        .addOnSuccessListener(eventSnapshots -> {
                            boolean dataChanged = false;

                            for (DocumentSnapshot eventDoc : eventSnapshots.getDocuments()) {
                                String imageUrl = eventDoc.getString("imageUrl");

                                if (imageUrl != null && !imageUrl.isEmpty() && !imageUrls.contains(imageUrl)) {
                                    imageList.add(new EventModel(imageUrl));
                                    imageUrls.add(imageUrl);
                                    dataChanged = true;
                                }
                            }

                            if (dataChanged) {
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Failed to load events: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to load users: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    // Refresh only when returning from a new event
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NEW_POST && resultCode == RESULT_OK) {
            fetchImagesFromFirestore();
        }
    }
}
