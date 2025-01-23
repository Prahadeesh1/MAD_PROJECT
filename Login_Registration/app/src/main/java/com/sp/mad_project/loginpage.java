package com.sp.mad_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class loginpage extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        register = findViewById(R.id.Register);
        register.setOnClickListener(onRegister);
        login = findViewById(R.id.login);
        login.setOnClickListener(onlogin);

    }

    private View.OnClickListener onlogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (username.getText().toString().equals("abc") && password.getText().toString().equals("123")){
                Intent intent;
                intent = new Intent(loginpage.this, mainpage.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "Incorrect Username or password", Toast.LENGTH_LONG).show();
            }

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