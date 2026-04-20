package com.example.firenze3;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firenze3.Activities.HomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Redirect immediately to HomeActivity
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        finish();
    }
}