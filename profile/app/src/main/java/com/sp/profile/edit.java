package com.sp.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class edit extends AppCompatActivity {

    private EditText editUsername, editBio;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editUsername = findViewById(R.id.edit_username);
        editBio = findViewById(R.id.edit_bio);
        btnSave = findViewById(R.id.btn_save);

        // Load saved data
        SharedPreferences sharedPreferences = getSharedPreferences("ProfileData", Context.MODE_PRIVATE);
        editUsername.setText(sharedPreferences.getString("username", ""));
        editBio.setText(sharedPreferences.getString("userBio", ""));

        // Save data when button is clicked
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", editUsername.getText().toString());
                editor.putString("userBio", editBio.getText().toString());
                editor.apply();

                // Return to ProfileActivity
                Intent intent = new Intent(edit.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
