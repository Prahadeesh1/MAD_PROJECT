package com.sp.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView username, userBio, postsCount, followersCount, followingCount;
    private ImageView btnFollowEdit;
    private ImageView settings;
    private RecyclerView recyclerViewPosts, recyclerViewEvents ;
    private PostAdapter postAdapter, eventAdapter;
    private List<Post> postList, eventList;
    private TabHost host;
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
        username.setText("john_doe");
        userBio.setText("Photographer | Travel | Adventure");
        postsCount.setText("120");
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
                eventList.add(new Post(eventArray[randomIndex]));   // Add dummy posts
            }
        }

        recyclerViewEvents.setLayoutManager(new GridLayoutManager(this, 2));
        eventAdapter = new PostAdapter(this, eventList);
        recyclerViewEvents.setAdapter(eventAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("ProfileData", Context.MODE_PRIVATE);
        username.setText(sharedPreferences.getString("username", "john_doe"));
        userBio.setText(sharedPreferences.getString("userBio", "Photographer | Travel | Adventure"));
    }
}