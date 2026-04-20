package com.example.firenze3.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firenze3.DatabaseHelper;
import com.example.firenze3.Model.MonumentStat;
import com.example.firenze3.R;

import java.util.ArrayList;

// Activity που εμφανίζει στατιστικά χρήσης του χρήστη
public class StatisticsActivity extends AppCompatActivity {

    // Κουμπί επιστροφής στο profile
    private Button btnBackToProfile;

    // TextViews για συνοπτικά στατιστικά
    private TextView tvTotalViews, tvTotalListens, tvSavedCount, tvUserName, tvUserAge;
    TextView navHome, navFav, navQuiz, navProfile;

    // RecyclerView (δεν χρησιμοποιείται εδώ γιατί φορτώνουμε δυναμικά layout)
    private RecyclerView recyclerStatistics;

    // Helper για τη βάση δεδομένων
    private DatabaseHelper dbHelper;

    // Email του τρέχοντος χρήστη
    private String currentUserEmail;

    // Container που θα γεμίσει με dynamic views στατιστικών
    private android.widget.LinearLayout layoutStatisticsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Δημιουργία database helper
        dbHelper = new DatabaseHelper(this);

        // Παίρνουμε email χρήστη από intent
        currentUserEmail = getIntent().getStringExtra("user_email");
        if (currentUserEmail == null) {
            currentUserEmail = "";
        }

        // Σύνδεση των views από XML
        tvTotalViews = findViewById(R.id.tvTotalViews);
        tvTotalListens = findViewById(R.id.tvTotalListens);
        tvSavedCount = findViewById(R.id.tvSavedCount);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserAge = findViewById(R.id.tvUserAge);
        layoutStatisticsContainer = findViewById(R.id.layoutStatisticsContainer);
        btnBackToProfile = findViewById(R.id.btnBackToProfile);

        // Φόρτωση δεδομένων
        loadUserInfo();
        loadSummaryStats();
        loadMonumentStats();

        // Κουμπί επιστροφής στο Profile
        btnBackToProfile.setOnClickListener(v -> {
            Intent intent = new Intent(StatisticsActivity.this, ProfileActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            startActivity(intent);
            finish();
        });

        setupBottomNav();
    }

    // Φορτώνει όνομα και ηλικία χρήστη από τη βάση
    private void loadUserInfo() {
        Cursor cursor = dbHelper.getUserByEmail(currentUserEmail);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_NAME));
            int age = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_AGE));

            tvUserName.setText("Όνομα: " + name);
            tvUserAge.setText("Ηλικία: " + age);
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    // Φορτώνει συνολικά stats (views, listens, favorites)
    private void loadSummaryStats() {
        int totalViews = dbHelper.getTotalOpenedCount(currentUserEmail);
        int totalListens = dbHelper.getTotalListenedCount(currentUserEmail);
        int savedCount = dbHelper.getFavoriteCount(currentUserEmail);

        tvTotalViews.setText(String.valueOf(totalViews));
        tvTotalListens.setText(String.valueOf(totalListens));
        tvSavedCount.setText(String.valueOf(savedCount));
    }

    // Φορτώνει stats για κάθε μνημείο
    private void loadMonumentStats() {

        // Παίρνουμε λίστα στατιστικών από DB
        ArrayList<MonumentStat> statList = dbHelper.getMonumentStatsForUser(currentUserEmail);

        // Καθαρίζουμε το container πριν γεμίσουμε
        layoutStatisticsContainer.removeAllViews();

        // Για κάθε monument δημιουργούμε ένα dynamic view
        for (MonumentStat stat : statList) {

            android.view.View statView =
                    getLayoutInflater().inflate(R.layout.item_statistic, layoutStatisticsContainer, false);

            TextView tvStatTitle = statView.findViewById(R.id.tvStatTitle);
            TextView tvOpenedCount = statView.findViewById(R.id.tvOpenedCount);
            TextView tvListenedCount = statView.findViewById(R.id.tvListenedCount);

            // Βάζουμε δεδομένα
            tvStatTitle.setText(stat.getMonumentTitle());
            tvOpenedCount.setText(String.valueOf(stat.getOpenedCount()));
            tvListenedCount.setText(String.valueOf(stat.getListenedCount()));

            // Προσθέτουμε το view στο container
            layoutStatisticsContainer.addView(statView);
        }
    }

    private void setupBottomNav() {
        navHome = findViewById(R.id.navHome);
        navFav = findViewById(R.id.navFav);
        navQuiz = findViewById(R.id.navQuiz);
        navProfile = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v ->
                startActivity(new Intent(StatisticsActivity.this, HomeActivity.class)));

        navFav.setOnClickListener(v ->
                startActivity(new Intent(StatisticsActivity.this, FavoritesActivity.class)));

        navQuiz.setOnClickListener(v -> {
        });

        navProfile.setOnClickListener(v ->
                startActivity(new Intent(StatisticsActivity.this, ProfileActivity.class)));
    }
}