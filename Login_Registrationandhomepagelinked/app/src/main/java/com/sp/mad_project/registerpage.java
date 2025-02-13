package com.sp.mad_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registerpage extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText password;
    private EditText cfm_password;
    private Button register;
    private String Email;
    private String Password;
    private String Cfm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpage);
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

    public static boolean valEmail(String input){
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailRegex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(input);
        return matcher.find();
    }

}