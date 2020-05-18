package com.example.tfg_urjc_appfirstrun.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tfg_urjc_appfirstrun.Activities.LoginActivity.ui.login.LoginActivity;
import com.example.tfg_urjc_appfirstrun.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginRegister = findViewById(R.id.register);

        loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent= new Intent (RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            }
        });
    }
}
