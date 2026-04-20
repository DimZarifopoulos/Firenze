package com.example.firenze3.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firenze3.DatabaseHelper;
import com.example.firenze3.R;

public class ProfileActivity extends AppCompatActivity {

    private Button btnEditProfile, btnStatistics, btnLogout;
    private TextView navHome, navFav, navQuiz, navProfile, tvAvatar;
    private TextView tvProfileName, tvFavoriteCount, tvViewsCount, tvQuizCount;
    private String currentUserEmail;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        currentUserEmail = prefs.getString("user_email", null);
        if (currentUserEmail == null) {
            currentUserEmail = getIntent().getStringExtra("user_email");
        }

        // Σύνδεση των TextViews
        tvProfileName = findViewById(R.id.tvProfileName);
        tvFavoriteCount = findViewById(R.id.tvFavoriteCount);
        tvViewsCount = findViewById(R.id.tvViewsCount);
        tvQuizCount = findViewById(R.id.tvQuizCount);
        tvAvatar = findViewById(R.id.tvAvatar);

        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnStatistics = findViewById(R.id.btnStatistics);
        btnLogout = findViewById(R.id.btnLogout);

        // Listeners
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            startActivity(intent);
        });

        btnStatistics.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, StatisticsActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> logoutUser());

        loadAndDisplayData();
        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ανανέωση δεδομένων κάθε φορά που επιστρέψουμε στη σελίδα
        loadAndDisplayData();
    }

    private void loadAndDisplayData() {
        if (currentUserEmail == null || currentUserEmail.isEmpty()) return;

        // Φόρτωση Ονόματος από τη βάση
        Cursor cursor = dbHelper.getUserByEmail(currentUserEmail);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String fullName = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_NAME));
                // Ενημέρωση ονόματος
                tvProfileName.setText(fullName);

                // Ενημέρωση Αρχικών (Avatar)
                String initials = getInitials(fullName);
                tvAvatar.setText(initials);
            }
            cursor.close();
        }

        // Favorites Count
        int favCount = dbHelper.getFavoriteCount(currentUserEmail);
        tvFavoriteCount.setText(String.valueOf(favCount));

        // Views (Total Opened Count)
        int viewsCount = dbHelper.getTotalOpenedCount(currentUserEmail);
        tvViewsCount.setText(String.valueOf(viewsCount));

        // Quiz Count
        int quizCount = dbHelper.getQuizCount(currentUserEmail);
        tvQuizCount.setText(String.valueOf(quizCount));
    }

    private String getInitials(String fullName) {
        if (fullName == null || fullName.isEmpty()) return "??";

        String[] parts = fullName.trim().split("\\s+");
        StringBuilder initials = new StringBuilder();

        // Πρώτο γράμμα από την πρώτη λέξη
        if (parts.length > 0 && !parts[0].isEmpty()) {
            initials.append(parts[0].charAt(0));
        }

        // Πρώτο γράμμα από την τελευταία λέξη
        if (parts.length > 1 && !parts[parts.length - 1].isEmpty()) {
            initials.append(parts[parts.length - 1].charAt(0));
        }

        return initials.toString().toUpperCase();
    }

    // Μέθοδος για το logout
    private void logoutUser() {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        prefs.edit().clear().apply();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Μέθοδος για το κάτω navigation
    private void setupBottomNav() {
        navHome = findViewById(R.id.navHome);
        navFav = findViewById(R.id.navFav);
        navQuiz = findViewById(R.id.navQuiz);
        navProfile = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            startActivity(intent);
        });

        navFav.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, FavoritesActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            startActivity(intent);
        });

        navQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, QuizActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            startActivity(intent);
        });

        navProfile.setOnClickListener(v -> {
        });
    }
}