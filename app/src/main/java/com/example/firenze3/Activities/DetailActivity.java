package com.example.firenze3.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firenze3.DatabaseHelper;
import com.example.firenze3.R;

import java.util.Locale;

// Activity που δείχνει αναλυτικά στοιχεία για ένα μνημείο
public class DetailActivity extends AppCompatActivity {

    // UI στοιχεία της οθόνης
    private ImageView imgMonument;
    private ImageButton btnHeart;
    private TextView tvSubtitle, tvTitle, tvDescription, tvCategoryChip;
    private Button btnListen, btnLearnMore, btnQuiz, btnOpenMap;

    // Κάτω navigation
    private TextView navHome, navFav, navQuiz, navProfile;

    // Μεταβλητή για το αν το μνημείο είναι favorite
    private boolean isFavorite = false;

    // Για να διαβάζει φωναχτά την περιγραφή
    private TextToSpeech textToSpeech;

    // Στοιχεία μνημείου
    private String monumentTitle;
    private String monumentSubtitle;
    private String monumentDescription;
    private String monumentCategory;
    private String monumentImageName;
    private String monumentMoreInfoUrl;
    private double monumentLatitude;
    private double monumentLongitude;

    // id μνημείου και email τρέχοντος χρήστη
    private int monumentId;
    private String currentUserEmail;

    // Helper για τη βάση δεδομένων
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Δημιουργία σύνδεσης με τη βάση
        dbHelper = new DatabaseHelper(this);

        // Σύνδεση των xml στοιχείων με τη Java
        imgMonument = findViewById(R.id.imgMonument);
        btnHeart = findViewById(R.id.btnHeart);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvCategoryChip = findViewById(R.id.tvCategoryChip);

        btnListen = findViewById(R.id.btnListen);
        btnLearnMore = findViewById(R.id.btnLearnMore);
        btnQuiz = findViewById(R.id.btnQuiz);
        btnOpenMap = findViewById(R.id.btnOpenMap);

        // Παίρνουμε τα δεδομένα που ήρθαν από το Intent
        readIntentData();

        // Εμφανίζουμε τα στοιχεία στην οθόνη
        displayMonumentData();

        // Ενεργοποίηση text to speech
        setupTextToSpeech();

        // Ρύθμιση bottom navigation
        setupBottomNav();

        // Αν υπάρχει σωστό monumentId και userEmail, αυξάνουμε το opened count
        if (monumentId != -1 && !currentUserEmail.isEmpty()) {
            dbHelper.incrementOpenedCount(currentUserEmail, monumentId);
        }
        // Ελέγχουμε αν το μνημείο είναι ήδη favorite
        if (monumentId != -1 && !currentUserEmail.isEmpty()) {
            isFavorite = dbHelper.isFavorite(currentUserEmail, monumentId);

            if (isFavorite) {
                btnHeart.setImageResource(R.drawable.ic_heart_filled);
            } else {
                btnHeart.setImageResource(R.drawable.ic_heart_outline);
            }
        }

        // Κουμπί favorite (καρδιά)
        btnHeart.setOnClickListener(v -> {

            // Αν είναι ήδη favorite → το αφαιρούμε
            if (isFavorite) {

                boolean removed = dbHelper.removeFavorite(currentUserEmail, monumentId);

                if (removed) {
                    btnHeart.setImageResource(R.drawable.ic_heart_outline);
                    isFavorite = false;
                    Toast.makeText(this, "Αφαιρέθηκε από αγαπημένα", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Αποτυχία αφαίρεσης", Toast.LENGTH_SHORT).show();
                }

            } else {

                // Αν δεν είναι favorite → το προσθέτουμε
                boolean added = dbHelper.addFavorite(currentUserEmail, monumentId);

                if (added) {
                    btnHeart.setImageResource(R.drawable.ic_heart_filled);
                    isFavorite = true;
                    Toast.makeText(this, "Προστέθηκε στα αγαπημένα", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Αποτυχία προσθήκης", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Κουμπί ακρόασης περιγραφής
        btnListen.setOnClickListener(v -> {
            if (textToSpeech != null) {
                // Διαβάζει την περιγραφή του μνημείου
                textToSpeech.speak(monumentDescription, TextToSpeech.QUEUE_FLUSH, null, null);

                // Αν υπάρχει σωστό monumentId και userEmail, αυξάνουμε το listened count
                if (monumentId != -1 && !currentUserEmail.isEmpty()) {
                    dbHelper.incrementListenedCount(currentUserEmail, monumentId);
                }

                Toast.makeText(this, "Ξεκίνησε η ακρόαση περιγραφής", Toast.LENGTH_SHORT).show();
            }
        });

        // Κουμπί για μετάβαση σε σελίδα με περισσότερες πληροφορίες
        btnLearnMore.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, LearnMoreActivity.class);
            intent.putExtra("url", monumentMoreInfoUrl);
            intent.putExtra("title", monumentTitle);
            startActivity(intent);
        });

        btnOpenMap.setOnClickListener(v -> {
            if (monumentLatitude != 0.0 && monumentLongitude != 0.0) {
                String uri = "google.navigation:q=" + monumentLatitude + "," + monumentLongitude;
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(uri));
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    String fallbackUri = "geo:" + monumentLatitude + "," + monumentLongitude + "?q=" +
                            monumentLatitude + "," + monumentLongitude + "(" + monumentTitle + ")";
                    Intent fallbackIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(fallbackUri));
                    startActivity(fallbackIntent);
                }
            } else {
                Toast.makeText(this, "Δεν υπάρχουν διαθέσιμες συντεταγμένες", Toast.LENGTH_SHORT).show();
            }
        });

        // Κουμπί για μετάβαση στο quiz του μνημείου
        btnQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, QuizActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            intent.putExtra("monument_title", monumentTitle);
            startActivity(intent);
        });
    }

    // Διαβάζει τα δεδομένα που ήρθαν από την προηγούμενη activity
    private void readIntentData() {
        Intent intent = getIntent();

        monumentId = intent.getIntExtra("monument_id", -1);
        currentUserEmail = intent.getStringExtra("user_email");

        monumentTitle = intent.getStringExtra("title");
        monumentSubtitle = intent.getStringExtra("subtitle");
        monumentDescription = intent.getStringExtra("description");
        monumentCategory = intent.getStringExtra("category");
        monumentImageName = intent.getStringExtra("image_name");
        monumentMoreInfoUrl = intent.getStringExtra("more_info_url");
        monumentLatitude = intent.getDoubleExtra("latitude", 0.0);
        monumentLongitude = intent.getDoubleExtra("longitude", 0.0);

        // Αν κάποιο πεδίο είναι null, βάζουμε default τιμές για ασφάλεια
        if (currentUserEmail == null) currentUserEmail = "";
        if (monumentTitle == null) monumentTitle = "Unknown Monument";
        if (monumentSubtitle == null) monumentSubtitle = "Monument";
        if (monumentDescription == null) monumentDescription = "No description available.";
        if (monumentCategory == null) monumentCategory = "Category";
        if (monumentImageName == null) monumentImageName = "firenze_poster";
        if (monumentMoreInfoUrl == null) monumentMoreInfoUrl = "";
    }

    // Εμφανίζει τα στοιχεία του μνημείου στα views
    private void displayMonumentData() {
        tvTitle.setText(monumentTitle);
        tvSubtitle.setText(monumentSubtitle);
        tvDescription.setText(monumentDescription);
        tvCategoryChip.setText(monumentCategory);

        // Βρίσκουμε την εικόνα από το όνομά της
        int imageResId = getResources().getIdentifier(
                monumentImageName,
                "drawable",
                getPackageName()
        );

        // Αν βρεθεί εικόνα, τη δείχνουμε, αλλιώς βάζουμε default
        if (imageResId != 0) {
            imgMonument.setImageResource(imageResId);
        } else {
            imgMonument.setImageResource(R.drawable.firenze_poster);
        }
    }

    // Ρυθμίζει το TextToSpeech
    private void setupTextToSpeech() {
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                // Προσπαθεί να βάλει ελληνική γλώσσα
                int result = textToSpeech.setLanguage(Locale.forLanguageTag("el"));

                // Αν δεν υποστηρίζονται τα ελληνικά, βάζει αγγλικά
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
    }

    // Ρυθμίσεις για το κάτω navigation menu
    private void setupBottomNav() {
        navHome = findViewById(R.id.navHome);
        navFav = findViewById(R.id.navFav);
        navQuiz = findViewById(R.id.navQuiz);
        navProfile = findViewById(R.id.navProfile);

        // Μετάβαση στο Home
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            startActivity(intent);
        });

        // Μετάβαση στα Favorites
        navFav.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, FavoritesActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            startActivity(intent);
        });

        // Μετάβαση στο Quiz
        navQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, QuizActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            startActivity(intent);
        });

        // Μετάβαση στο Profile
        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, ProfileActivity.class);
            intent.putExtra("user_email", currentUserEmail);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        // Κλείνουμε σωστά το TextToSpeech όταν κλείνει η activity
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}