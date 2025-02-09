package com.sp.mad_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registerpage extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText password;
    private EditText cfm_password;
    private Button register;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpage);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        cfm_password = findViewById(R.id.cfm_password);
        register = findViewById(R.id.get_started);
        register.setOnClickListener(onRegister);
    }

    private View.OnClickListener onRegister = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String emailInput = email.getText().toString().trim();
            String passwordInput = password.getText().toString().trim();
            String confirmPasswordInput = cfm_password.getText().toString().trim();
            String usernameInput = username.getText().toString().trim();

            if (isInputInvalid(usernameInput, emailInput, passwordInput, confirmPasswordInput)) {
                return;
            }

            if (!passwordInput.equals(confirmPasswordInput)) {
                showToast("Password does not match the above password");
                return;
            }

            mAuth.createUserWithEmailAndPassword(emailInput, passwordInput)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                uploadUsername();
                                Intent intent = new Intent(registerpage.this, submittingImage.class);
                                startActivity(intent);
                                Toast.makeText(registerpage.this, "Account Created",
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(registerpage.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



            navigateToNextPage();
        }

        private boolean isInputInvalid(String username, String email, String password, String confirmPassword) {
            if (username.isEmpty()) {
                showToast("Please enter your Username");
                return true;
            }

            if(email.isEmpty()){
                showToast("Please enter your Email");
                return true;
            }

            if (!valEmail(email)) {
                showToast("Invalid email address");
                return true;
            }

            if (password.isEmpty()) {
                showToast("Please enter your password");
                return true;
            }

            if (confirmPassword.isEmpty()) {
                showToast("Please re-enter your password");
                return true;
            }

            return false;
        }

        private void showToast(String message) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

        private void navigateToNextPage() {
            Intent intent = new Intent(registerpage.this, submittingImage.class);
            startActivity(intent);
        }
    };

    private void uploadUsername() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        String userName = username.getText().toString();
        Map<String, Object> user = new HashMap<>();
        user.put("username", userName);
        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(registerpage.this, "Username Saved", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(registerpage.this, "Error saving username", Toast.LENGTH_SHORT).show();
                });
    }

    public static boolean valEmail(String input){
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailRegex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(input);
        return matcher.find();
    }

}