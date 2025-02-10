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

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView username, userBio, postsCount, followersCount, followingCount;
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
    private int[] postArray = {
            R.drawable.sample_post,
            R.drawable.box,
            R.drawable.cat,
            R.drawable.die,
            R.drawable.girl,
            R.drawable.pear,
            R.drawable.pixel,
            R.drawable.skull,
            R.drawable.spongebob,
    };
    private int[] eventArray = {
            R.drawable.art,
            R.drawable.party,
            R.drawable.market,
            R.drawable.music,
            R.drawable.open_mic,
            R.drawable.party,
    };

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
        postsCount = findViewById(R.id.posts_count);
        followersCount = findViewById(R.id.followers_count);
        followingCount = findViewById(R.id.following_count);
        recyclerViewPosts = findViewById(R.id.recycler_view_posts);
        recyclerViewEvents =findViewById(R.id.recycler_view_events);
        settings = findViewById(R.id.settings);
        btnFollowEdit = findViewById(R.id.btn_follow_edit);
        TAG = "ProfileActivity";


        loadProfileDetails();

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

        // Setup RecyclerView
        postList = new ArrayList<>();
        for(int j = 0; j < 2; j++){
            for (int i = 0; i < 9; i++) {
                int randomIndex = i % postArray.length; // Cycles through the images
                postList.add(new Post(postArray[randomIndex]));   // Add dummy posts
            }
        }

        recyclerViewPosts.setLayoutManager(new GridLayoutManager(this, 3));
        postAdapter = new PostAdapter(this, postList);
        recyclerViewPosts.setAdapter(postAdapter);

        eventList = new ArrayList<>();
        for(int j = 0; j < 2; j++){
            for (int i = 0; i < 6; i++) {
                int randomIndex = i % eventArray.length; // Cycles through the images
                eventList.add(new Event(eventArray[randomIndex]));   // Add dummy posts
            }
        }

        recyclerViewEvents.setLayoutManager(new GridLayoutManager(this, 2));
        eventAdapter = new EventAdapter(this, eventList);
        recyclerViewEvents.setAdapter(eventAdapter);

        postsCount.setText(String.valueOf(postList.size() + eventList.size()));
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
                        String profilePictureUrl = document.getString("imageUrl");
                        String Personality = document.getString("personality");
                        if (profilePictureUrl != null && UserName != null && Personality != null) {
                            // Load the profile picture into an ImageView using Glide or Picasso
                            Glide.with(ProfileActivity.this)
                                    .load(profilePictureUrl)
                                    .into(profileImage); // profileImageView should be your ImageView
                            username.setText(UserName);
                            userBio.setText(Personality);
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
}