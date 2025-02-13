package com.sp.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserSelectScreen extends AppCompatActivity {
    RecyclerView recyclerView;
    UserAdapter userAdapter;
    ImageView back;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_select_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String userName = getIntent().getStringExtra("user");
        getSupportActionBar().setTitle(userName);

        userAdapter = new UserAdapter(this);
        recyclerView = findViewById(R.id.recycler_view_users);

        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Log.d("Firebase", "Database reference path: " + databaseReference.toString());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Firebase", "onDataChange triggered. Snapshot exists: " + snapshot.exists());

                userAdapter.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Log.d("Firebase", "DataSnapshot: " + dataSnapshot.toString());
                    String uId = dataSnapshot.getKey();
                    User user = dataSnapshot.getValue(User.class);
                    if(user!=null && user.getUserId()!=null && !user.getUserId().equals(FirebaseAuth.getInstance().getUid())){
                        Log.d("Firebase", "User ID from database: " + user.getUserId());
                        userAdapter.add(user);
                    }
                }
                List<User> userList = userAdapter.getUserModelList();
                Log.d("Adapter", "Total users added: " + userAdapter.getUserModelList().size());
                userAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.back ){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(UserSelectScreen.this, MainActivity.class));
            finish();
            return true;
        }
        return false;
    }
}