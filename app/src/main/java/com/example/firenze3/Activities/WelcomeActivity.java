package com.example.firenze3.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firenze3.R;

// Activity καλωσορίσματος (πρώτη οθόνη της εφαρμογής)
// Από εδώ ο χρήστης επιλέγει Login ή Register
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Σύνδεση των κουμπιών από το XML
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        // Όταν πατηθεί το Login → μετάβαση στη LoginActivity
        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class)));

        // Όταν πατηθεί το Register → μετάβαση στη RegisterActivity
        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class)));
    }
}