package com.sp.mad_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginRegistrationPage extends AppCompatActivity {
    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration_page);

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        login.setOnClickListener(onLogin);
        register.setOnClickListener(onRegister);
    }

    private View.OnClickListener onLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(LoginRegistrationPage.this, Loginpage.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener onRegister = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(LoginRegistrationPage.this, Registerpage.class);
            startActivity(intent);
        }
    };
}
