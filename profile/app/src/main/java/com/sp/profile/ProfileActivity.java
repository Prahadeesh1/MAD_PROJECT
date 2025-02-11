package com.sp.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView username, userBio, postsCount, followersCount, followingCount, userPersonality;
    private ImageView btnFollowEdit;
    private ImageView settings;
    private RecyclerView recyclerViewPosts, recyclerViewEvents ;
    private PostAdapter postAdapter;
    private EventAdapter eventAdapter;
    private List<Post> postList;
    private List<Event> eventList;
    private TabHost host;
    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

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
        settings = findViewById(R.id.settings);
        btnFollowEdit = findViewById(R.id.btn_follow_edit);
        TAG = "ProfileActivity";


        loadProfileDetails();

        // Setup RecyclerView for posts
        recyclerViewPosts.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewPosts.setHasFixedSize(true);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList);
        recyclerViewPosts.setAdapter(postAdapter);


        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(this, eventList);
        recyclerViewEvents.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewEvents.setAdapter(eventAdapter);

        loadPosts();
        loadEvents();


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



        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, setting.class);
                startActivity(intent);
            }
        });
        btnFollowEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, edit.class);
                startActivity(intent);
            }
        });

        // Set dummy data

        followersCount.setText("5.3K");
        followingCount.setText("320");
    }

    private void loadProfileDetails(){
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = user.getUid();
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String UserName = document.getString("username");
                        String Personality = document.getString("personality");
                        String Bio = document.getString("Bio");
                        String ProfilePicture = document.getString("profilepictureUrl");
                        if (UserName != null && Personality != null
                                && Bio != null && ProfilePicture != null) {
                            // Load the profile picture into an ImageView using Glide or Picasso
                            Glide.with(ProfileActivity.this).load(ProfilePicture)
                                    .into(profileImage);
                            username.setText(UserName);
                            userPersonality.setText(Personality);
                            userBio.setText(Bio);
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileDetails(); // Refresh user details when returning to ProfileActivity
    }

    private void loadPosts() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        Log.d("Firestore", "Fetching posts for user: " + userId);

        db.collection("users").document(userId).collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        postList.clear(); // Clear previous data
                        for (DocumentSnapshot document : task.getResult()) {
                            Post post = document.toObject(Post.class);
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




    private void loadEvents() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        db.collection("users").document(userId).collection("events")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Error fetching events", error);
                        return;
                    }

                    if (value != null) {
                        eventList.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            Event event = doc.toObject(Event.class);
                            eventList.add(event);
                        }
                        eventAdapter.notifyDataSetChanged();
                        postsCount.setText(String.valueOf(postList.size() + eventList.size())); // Update count
                    }
                });
    }



}