package com.sp.mad_project;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {
    private EditText searchInput;
    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;
    private List<Users> usersList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchInput = findViewById(R.id.search_input);
        recyclerView = findViewById(R.id.recycler_view);

        db = FirebaseFirestore.getInstance();
        usersList = new ArrayList<>();
        usersAdapter = new UsersAdapter(this, usersList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(usersAdapter);

        // Handle "Enter" key to dismiss keyboard
        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                performSearch();
                return true;
            }
            return false;
        });

        // Listen for text changes in the search bar
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString().trim()); // Call search function as user types
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void performSearch() {
        String query = searchInput.getText().toString().trim();
        if (!query.isEmpty()) {
            searchUsers(query);
            hideKeyboard();
        } else {
            Toast.makeText(this, "Enter a username to search", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        }
    }

    private void searchUsers(String query) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CollectionReference usersRef = db.collection("users");

        usersRef.whereGreaterThanOrEqualTo("username", query)
                .whereLessThanOrEqualTo("username", query + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    usersList.clear();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String userId = document.getId();
                            if (!userId.equals(currentUserId)) {
                                String username = document.getString("username");
                                String email = document.getString("email");
                                String profileImage = document.getString("profilepictureUrl"); // Get profile picture

                                Users users = new Users(userId, username,email, profileImage);
                                usersList.add(users);
                            }

                        }
                        usersAdapter.notifyDataSetChanged(); // Update RecyclerView
                    } else {
                        Toast.makeText(Search.this, "Error fetching users", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
