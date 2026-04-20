package com.example.firenze3.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firenze3.DatabaseHelper;
import com.example.firenze3.Model.Monument;
import com.example.firenze3.Adapters.MonumentAdapter;
import com.example.firenze3.R;

import java.util.ArrayList;

// Activity της αρχικής σελίδας που δείχνει όλα τα μνημεία
public class HomeActivity extends AppCompatActivity {

    // Κάτω navigation menu
    private TextView navHome, navFav, navQuiz, navProfile;

    // Κατηγορίες-φίλτρα
    private TextView chipAll, chipChurches, chipMuseums, chipBridges, chipSquares;

    // Πεδίο αναζήτησης
    private EditText etSearch;

    // RecyclerView για τη λίστα μνημείων
    private RecyclerView recyclerMonuments;

    // Helper για βάση δεδομένων
    private DatabaseHelper dbHelper;

    // Adapter του RecyclerView
    private MonumentAdapter adapter;

    // Πλήρης λίστα μνημείων
    private ArrayList<Monument> fullMonumentList;

    // Email του τρέχοντος χρήστη
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Δημιουργία database helper
        dbHelper = new DatabaseHelper(this);

        // Σύνδεση views από το XML
        etSearch = findViewById(R.id.etSearch);
        recyclerMonuments = findViewById(R.id.recyclerMonuments);

        chipAll = findViewById(R.id.chipAll);
        chipChurches = findViewById(R.id.chipChurches);
        chipMuseums = findViewById(R.id.chipMuseums);
        chipBridges = findViewById(R.id.chipBridges);
        chipSquares = findViewById(R.id.chipSquares);

        // Παίρνουμε όλα τα μνημεία από τη βάση
        fullMonumentList = dbHelper.getAllMonuments();

        // Παίρνουμε το email του χρήστη από το Intent
        currentUserEmail = getIntent().getStringExtra("user_email");

        // Αν δεν έχει σταλεί email, βάζουμε κενό string
        if (currentUserEmail == null) {
            currentUserEmail = "";
        }

        // Δημιουργία adapter με όλη τη λίστα μνημείων
        adapter = new MonumentAdapter(this, new ArrayList<>(fullMonumentList), currentUserEmail);

        // Ορίζουμε layout manager και adapter στο RecyclerView
        recyclerMonuments.setLayoutManager(new LinearLayoutManager(this));
        recyclerMonuments.setAdapter(adapter);

        // Ρύθμιση bottom navigation
        setupBottomNav();

        // Ρύθμιση αναζήτησης
        setupSearch();

        // Ρύθμιση φίλτρων κατηγορίας
        setupCategoryFilters();
    }

    // Ρύθμιση search bar
    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Δεν κάνουμε κάτι πριν αλλάξει το κείμενο
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Κάθε φορά που αλλάζει το κείμενο, φιλτράρουμε τη λίστα
                filterByTextAndCategory(s.toString(), null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Δεν κάνουμε κάτι μετά την αλλαγή
            }
        });
    }

    // Ρύθμιση φίλτρων ανά κατηγορία
    private void setupCategoryFilters() {
        // Εμφάνιση όλων των μνημείων
        chipAll.setOnClickListener(v -> filterByTextAndCategory(etSearch.getText().toString(), "All"));

        // Εμφάνιση μόνο εκκλησιών
        chipChurches.setOnClickListener(v -> filterByTextAndCategory(etSearch.getText().toString(), "Churches"));

        // Εμφάνιση μόνο μουσείων
        chipMuseums.setOnClickListener(v -> filterByTextAndCategory(etSearch.getText().toString(), "Museums"));

        // Εμφάνιση μόνο γεφυριών
        chipBridges.setOnClickListener(v -> filterByTextAndCategory(etSearch.getText().toString(), "Bridges"));

        // Εμφάνιση μόνο πλατειών
        chipSquares.setOnClickListener(v -> filterByTextAndCategory(etSearch.getText().toString(), "Squares"));
    }

    // Φιλτράρει τη λίστα με βάση το κείμενο αναζήτησης και την κατηγορία
    private void filterByTextAndCategory(String text, String category) {
        ArrayList<Monument> filteredList = new ArrayList<>();

        // Ελέγχουμε κάθε μνημείο της πλήρους λίστας
        for (Monument monument : fullMonumentList) {

            // Ελέγχουμε αν το κείμενο υπάρχει στον τίτλο ή στην περιγραφή
            boolean matchesText =
                    monument.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                            monument.getDescription().toLowerCase().contains(text.toLowerCase());

            // Ελέγχουμε αν ταιριάζει η κατηγορία
            boolean matchesCategory =
                    category == null ||
                            category.equals("All") ||
                            monument.getCategory().equalsIgnoreCase(category);

            // Αν ταιριάζουν και τα δύο, το προσθέτουμε στη filtered λίστα
            if (matchesText && matchesCategory) {
                filteredList.add(monument);
            }
        }

        // Ενημερώνουμε τον adapter με τη νέα λίστα
        adapter.setFilteredList(filteredList);
    }

    // Ρύθμιση κάτω navigation menu
    private void setupBottomNav() {
        navHome = findViewById(R.id.navHome);
        navFav = findViewById(R.id.navFav);
        navQuiz = findViewById(R.id.navQuiz);
        navProfile = findViewById(R.id.navProfile);

        // Το Home φαίνεται ενεργό
        navHome.setAlpha(1f);
        navFav.setAlpha(0.7f);
        navQuiz.setAlpha(0.7f);
        navProfile.setAlpha(0.7f);

        // Είμαστε ήδη στο Home, άρα δεν κάνουμε κάτι
        navHome.setOnClickListener(v -> {
        });

        // Μετάβαση στα Favorites
        navFav.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, FavoritesActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            startActivity(intent);
        });

        // Μετάβαση στο Quiz
        navQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, QuizActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            startActivity(intent);
        });

        // Μετάβαση στο Profile
        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            startActivity(intent);
        });
    }
}