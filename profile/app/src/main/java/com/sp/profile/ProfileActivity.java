package com.sp.profile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
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
    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;

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
        btnFollowEdit = findViewById(R.id.btn_follow_edit);
        recyclerViewPosts = findViewById(R.id.recycler_view_posts);

        // Set dummy data
        username.setText("john_doe");
        userBio.setText("Photographer | Travel | Adventure");
        postsCount.setText("120");
        followersCount.setText("5.3K");
        followingCount.setText("320");

        // Setup RecyclerView
        postList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            postList.add(new Post(R.drawable.sample_post));  // Add dummy posts
        }

        recyclerViewPosts.setLayoutManager(new GridLayoutManager(this, 3));
        postAdapter = new PostAdapter(this, postList);
        recyclerViewPosts.setAdapter(postAdapter);
    }
}