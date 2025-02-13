package com.sp.mad_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Setting extends AppCompatActivity {

    private Switch switchNotifications;
    private Button btnSave, btnlogout;
    private SharedPreferences sharedPreferences;
    private Switch switchDarkMode;
    private Switch switchPrivateAccount, switchLocationTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Initialize UI Elements
        switchNotifications = findViewById(R.id.switch_notifications);
        switchDarkMode = findViewById(R.id.switch_dark_mode); // ADD THIS LINE
        btnSave = findViewById(R.id.btn_save);
        btnlogout = findViewById(R.id.btn_logout);

        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        // Load saved preferences
        loadSettings();

        switchPrivateAccount = findViewById(R.id.switch_private_account);
        switchLocationTracking = findViewById(R.id.switch_location_tracking);

        // Initialize other switches correctly
        switchPrivateAccount.setChecked(sharedPreferences.getBoolean("private_account", false));
        switchLocationTracking.setChecked(sharedPreferences.getBoolean("location_tracking", true));

        switchPrivateAccount.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("private_account", isChecked).apply();
        });

        switchLocationTracking.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("location_tracking", isChecked).apply();
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                Toast.makeText(Setting.this, "Settings saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Setting.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Loginpage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (switchNotifications != null) {
            editor.putBoolean("notifications", switchNotifications.isChecked());
        } if (switchDarkMode != null) {
            editor.putBoolean("dark_mode", switchDarkMode.isChecked());
        }
        editor.apply();
    }


    private void loadSettings() {
        // Ensure UI elements are initialized before using them
        if (switchNotifications != null) {
            switchNotifications.setChecked(sharedPreferences.getBoolean("notifications", true));
        }
        if (switchDarkMode != null) {
            switchDarkMode.setChecked(sharedPreferences.getBoolean("dark_mode", false));
        }
        if (switchPrivateAccount != null) {
            switchPrivateAccount.setChecked(sharedPreferences.getBoolean("private_account", false));
        }
        if (switchLocationTracking != null) {
            switchLocationTracking.setChecked(sharedPreferences.getBoolean("location_tracking", true));
        }
    }
}