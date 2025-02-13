package com.sp.mad_project;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class UsersProfileActivity extends AppCompatActivity {
    private ImageView profileImage;
    private TextView username, userBio, postsCount, followersCount, followingCount, userPersonality;
    private RecyclerView recyclerViewPosts, recyclerViewEvents ;
    private PostAdapter postAdapter;
    private EventAdapter eventAdapter;
    private List<PostModel> postList;
    private List<EventModel> eventList;
    private TabHost host;
    private FirebaseFirestore db;
    String TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        profileImage = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        userBio = findViewById(R.id.user_bio);
        userPersonality = findViewById(R.id.user_personality);
        postsCount = findViewById(R.id.posts_count);
        followersCount = findViewById(R.id.followers_count);
        followingCount = findViewById(R.id.following_count);
        recyclerViewPosts = findViewById(R.id.recycler_view_posts);
        recyclerViewEvents =findViewById(R.id.recycler_view_events);

        recyclerViewPosts.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewPosts.setHasFixedSize(true);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList);
        recyclerViewPosts.setAdapter(postAdapter);


        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(this, eventList);
        recyclerViewEvents.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewEvents.setAdapter(eventAdapter);

        host = findViewById(R.id.tabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("Posts");
        spec.setContent(R.id.posts);
        spec.setIndicator("Posts");
        host.addTab(spec);

        spec = host.newTabSpec("Events");
        spec.setContent(R.id.events);
        spec.setIndicator("Events");
        host.addTab(spec);
        host.setCurrentTab(0);

        followersCount.setText("5.3K");
        followingCount.setText("320");

        // Get the user ID from the intent
        String userId = getIntent().getStringExtra("userId");
        if (userId != null) {
            loadUserProfile(userId);
            loadPosts(userId);
            loadEvents(userId);
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadUserProfile(String userId) {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(document -> {
            if (document.exists()) {
                String usernameText = document.getString("username");
                String Personality = document.getString("personality");
                String Bio = document.getString("Bio");
                String profileImageUrl = document.getString("profilepictureUrl");

                username.setText(usernameText);
                userPersonality.setText(Personality);
                userBio.setText(Bio);

                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                    Glide.with(this)
                            .load(profileImageUrl)
                            .placeholder(R.drawable.default_profile)
                            .error(R.drawable.default_profile)
                            .into(profileImage);
                } else {
                    profileImage.setImageResource(R.drawable.default_profile);
                }
            } else {
                Toast.makeText(this, "User profile not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
        );
    }

    private void loadPosts(String userId){
        db.collection("users").document(userId).collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        postList.clear(); // Clear previous data
                        for (DocumentSnapshot document : task.getResult()) {
                            PostModel post = document.toObject(PostModel.class);
                            if (post != null) {
                                postList.add(post);
                                Log.d("Firestore", "Loaded post: " + post.getImageUrl());
                            }
                        }
                        // Update UI on main thread
                        runOnUiThread(() -> {
                            postAdapter.notifyDataSetChanged(); // Refresh RecyclerView
                            postsCount.setText(String.valueOf(postList.size())); // Update post count
                        });

                        Log.d("Firestore", "Total posts loaded: " + postList.size());
                    } else {
                        Log.e("Firestore", "Error getting posts", task.getException());
                    }
                });
    }

    private void loadEvents(String userId){
        db.collection("users").document(userId).collection("events")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Error fetching events", error);
                        return;
                    }

                    if (value != null) {
                        eventList.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            EventModel event = doc.toObject(EventModel.class);
                            eventList.add(event);
                        }
                        eventAdapter.notifyDataSetChanged();
                        postsCount.setText(String.valueOf(postList.size() + eventList.size())); // Update count
                    }
                });
    }

}