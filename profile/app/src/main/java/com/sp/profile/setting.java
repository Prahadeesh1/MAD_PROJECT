package com.sp.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class setting extends AppCompatActivity {

    private Switch switchNotifications;
    private Spinner spinnerTheme;
    private Button btnSave;
    private SharedPreferences sharedPreferences;
    private Switch switchDarkMode;
    private Spinner spinnerLanguage;
    private Switch switchPrivateAccount, switchLocationTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Initialize UI Elements
        switchNotifications = findViewById(R.id.switch_notifications);
        btnSave = findViewById(R.id.btn_save);

        // Setup SharedPreferences
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        // Load saved preferences
        loadSettings();

        switchPrivateAccount = findViewById(R.id.switch_private_account);
        switchLocationTracking = findViewById(R.id.switch_location_tracking);

        switchPrivateAccount.setChecked(sharedPreferences.getBoolean("private_account", false));
        switchLocationTracking.setChecked(sharedPreferences.getBoolean("location_tracking", true));

        switchPrivateAccount.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("private_account", isChecked).apply();
        });

        switchLocationTracking.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("location_tracking", isChecked).apply();
        });

        Button btnFeedback = findViewById(R.id.btn_feedback);
        btnFeedback.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@yourapp.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "App Feedback");
            try {
                startActivity(Intent.createChooser(emailIntent, "Send Feedback"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(setting.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnResetSettings = findViewById(R.id.btn_reset_settings);
        btnResetSettings.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Clear all saved settings
            editor.apply();

            // Reload activity to apply changes
            finish();
            startActivity(getIntent());

            Toast.makeText(setting.this, "Settings reset to default", Toast.LENGTH_SHORT).show();
        });

        // Save Button Click Listener
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                Toast.makeText(setting.this, "Settings saved!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(setting.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notifications", switchNotifications.isChecked());
        editor.putBoolean("dark_mode", switchDarkMode.isChecked());
        editor.putString("theme", spinnerTheme.getSelectedItem().toString());
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