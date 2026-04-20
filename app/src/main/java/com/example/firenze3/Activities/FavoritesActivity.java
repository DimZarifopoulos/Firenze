package com.example.firenze3.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firenze3.DatabaseHelper;
import com.example.firenze3.Adapters.FavoriteAdapter;
import com.example.firenze3.Model.Monument;
import com.example.firenze3.R;

import java.util.ArrayList;

// Activity που εμφανίζει τα αγαπημένα μνημεία του χρήστη
public class FavoritesActivity extends AppCompatActivity {

    // Κουμπί για μετάβαση στην αρχική ώστε να βρει μνημεία
    private Button btnExploreFromFavorites;

    // Layout που εμφανίζεται όταν δεν υπάρχουν αγαπημένα
    private LinearLayout layoutEmptyState;

    // RecyclerView που δείχνει τη λίστα αγαπημένων
    private RecyclerView recyclerFavorites;

    // Κάτω navigation menu
    private TextView navHome, navFav, navQuiz, navProfile;

    // Helper για τη βάση δεδομένων
    private DatabaseHelper dbHelper;

    // Email του τρέχοντος χρήστη
    private String currentUserEmail;

    // Λίστα με τα αγαπημένα μνημεία
    private ArrayList<Monument> favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Δημιουργία database helper
        dbHelper = new DatabaseHelper(this);

        // Σύνδεση των views από το XML
        btnExploreFromFavorites = findViewById(R.id.btnExploreFromFavorites);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        recyclerFavorites = findViewById(R.id.recyclerFavorites);

        // Παίρνουμε το email του χρήστη από το Intent
        currentUserEmail = getIntent().getStringExtra("user_email");

        // Αν δεν ήρθε email, βάζουμε κενό string για ασφάλεια
        if (currentUserEmail == null) {
            currentUserEmail = "";
        }

        // Αν πατήσει το κουμπί explore, πάει στην αρχική σελίδα
        btnExploreFromFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(FavoritesActivity.this, HomeActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            startActivity(intent);
        });

        // Ρύθμιση bottom navigation
        setupBottomNav();

        // Φόρτωση αγαπημένων από τη βάση
        loadFavorites();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites();
    }

    // Φέρνει τα αγαπημένα μνημεία του χρήστη από τη βάση
    private void loadFavorites() {
        favoriteList = dbHelper.getFavoriteMonuments(currentUserEmail);

        // Αν δεν υπάρχουν αγαπημένα, δείχνουμε το empty state
        if (favoriteList.isEmpty()) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            recyclerFavorites.setVisibility(View.GONE);
        } else {
            // Αν υπάρχουν αγαπημένα, δείχνουμε το RecyclerView
            layoutEmptyState.setVisibility(View.GONE);
            recyclerFavorites.setVisibility(View.VISIBLE);

            // Βάζουμε linear layout στο RecyclerView
            recyclerFavorites.setLayoutManager(new LinearLayoutManager(this));

            // Βάζουμε adapter με τα favorite monuments
            recyclerFavorites.setAdapter(new FavoriteAdapter(this, favoriteList, currentUserEmail));
        }
    }

    // Ρύθμιση του κάτω navigation menu
    private void setupBottomNav() {
        navHome = findViewById(R.id.navHome);
        navFav = findViewById(R.id.navFav);
        navQuiz = findViewById(R.id.navQuiz);
        navProfile = findViewById(R.id.navProfile);

        // Μετάβαση στο Home
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(FavoritesActivity.this, HomeActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            startActivity(intent);
        });

        // Είμαστε ήδη στη σελίδα Favorites, οπότε δεν κάνουμε κάτι
        navFav.setOnClickListener(v -> {
        });

        // Μετάβαση στο Quiz
        navQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(FavoritesActivity.this, QuizActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            startActivity(intent);
        });

        // Μετάβαση στο Profile
        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(FavoritesActivity.this, ProfileActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            startActivity(intent);
        });
    }
}