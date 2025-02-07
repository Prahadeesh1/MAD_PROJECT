package com.sp.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;import com.sp.mad_project.MainActivity;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Reference views
        EditText etReason = findViewById(R.id.et_reason);
        Button btnDone = findViewById(R.id.btn_done);

        // Done button click listener
        btnDone.setOnClickListener(v -> {
            String reason = etReason.getText().toString();
            if (reason.isEmpty()) {
                Toast.makeText(ReportActivity.this, "Please enter a reason!", Toast.LENGTH_SHORT).show();
            } else {
                // Redirect to MainActivity
                Intent intent = new Intent(ReportActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }
}
