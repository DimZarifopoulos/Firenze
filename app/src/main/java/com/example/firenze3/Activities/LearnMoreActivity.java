package com.example.firenze3.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firenze3.R;

// Activity που εμφανίζει web σελίδα με περισσότερες πληροφορίες για το μνημείο
public class LearnMoreActivity extends AppCompatActivity {

    // WebView για εμφάνιση της σελίδας
    private WebView webViewMonument;

    // Τίτλος της σελίδας
    private TextView tvPageTitle;

    // Enable JavaScript
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_more);

        // Σύνδεση των views από το XML
        webViewMonument = findViewById(R.id.webViewMonument);
        tvPageTitle = findViewById(R.id.tvPageTitle);

        // Παίρνουμε τα δεδομένα από το Intent
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");

        // Αν υπάρχει τίτλος, τον εμφανίζουμε
        if (title != null && !title.isEmpty()) {
            tvPageTitle.setText(title);
        }

        // Ρυθμίσεις WebView
        WebSettings webSettings = webViewMonument.getSettings();

        // Ενεργοποίηση JavaScript (χρειάζεται για Wikipedia κτλ)
        webSettings.setJavaScriptEnabled(true);

        // Ενεργοποίηση local storage για web
        webSettings.setDomStorageEnabled(true);

        // Για να ανοίγουν τα links μέσα στο WebView και όχι στον browser
        webViewMonument.setWebViewClient(new WebViewClient());

        // Αν υπάρχει URL → φόρτωσε τη σελίδα
        if (url != null && !url.isEmpty()) {
            webViewMonument.loadUrl(url);
        } else {
            // Αν δεν υπάρχει URL → εμφάνιση fallback HTML
            webViewMonument.loadData(
                    "<h2>No page available</h2><p>Δεν υπάρχει διαθέσιμο link για αυτό το αξιοθέατο.</p>",
                    "text/html",
                    "UTF-8"
            );
        }
    }

    // Override back button ώστε να πηγαίνει πίσω μέσα στο WebView
    @Override
    public void onBackPressed() {
        if (webViewMonument != null && webViewMonument.canGoBack()) {
            webViewMonument.goBack(); // πάει στην προηγούμενη σελίδα του web
        } else {
            super.onBackPressed(); // αλλιώς κλείνει το activity
        }
    }
}