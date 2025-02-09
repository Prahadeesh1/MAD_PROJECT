package com.sp.mad_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginpage extends AppCompatActivity {

    private EditText enterEmail;
    private EditText enterPassword;
    private Button login;
    private TextView register;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), mainpage.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        enterEmail = findViewById(R.id.email);
        enterPassword = findViewById(R.id.password);
        register = findViewById(R.id.Register);
        register.setOnClickListener(onRegister);
        login = findViewById(R.id.login);
        login.setOnClickListener(onlogin);
    }

    private View.OnClickListener onlogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String email, password;
            email = enterEmail.getText().toString();
            password = enterPassword.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(loginpage.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(loginpage.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Use signInWithEmailAndPassword() instead of createUserWithEmailAndPassword()
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(loginpage.this, "Login Successful",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), mainpage.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(loginpage.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };

    private View.OnClickListener onRegister = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(loginpage.this, registerpage.class);
            startActivity(intent);
        }
    };
}
