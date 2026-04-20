package com.example.firenze3.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firenze3.DatabaseHelper;
import com.example.firenze3.R;

// Activity για login χρήστη
public class LoginActivity extends AppCompatActivity {

    // Πεδία εισαγωγής email και password
    private EditText etEmail, etPassword;

    // Κουμπί login
    private Button btnLogin;

    // Link για μετάβαση στο register
    private TextView tvRegister;

    // Helper για τη βάση δεδομένων
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Δημιουργία σύνδεσης με βάση δεδομένων
        dbHelper = new DatabaseHelper(this);

        // Έλεγχος αν υπάρχει ήδη αποθηκευμένο login
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String savedEmail = prefs.getString("user_email", null);

        // Αν υπάρχει ήδη email αποθηκευμένο, πάει κατευθείαν στο Home
        if (savedEmail != null) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("user_email", savedEmail);
            startActivity(intent);
            finish();
            return;
        }

        // Σύνδεση των views από το XML
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Αν ήρθε email από το RegisterActivity, το βάζουμε αυτόματα στο πεδίο email
        String emailFromRegister = getIntent().getStringExtra("user_email");
        if (emailFromRegister != null && !emailFromRegister.isEmpty()) {
            etEmail.setText(emailFromRegister);
        }

        // Όταν πατηθεί το login → καλείται η loginUser()
        btnLogin.setOnClickListener(v -> loginUser());

        // Όταν πατηθεί το register → πάει στη RegisterActivity
        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    // Μέθοδος που κάνει τον έλεγχο login
    private void loginUser() {

        // Παίρνουμε τα δεδομένα που έγραψε ο χρήστης
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Αν το email είναι κενό → δείξε error
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter your email");
            etEmail.requestFocus();
            return;
        }

        // Αν το password είναι κενό → δείξε error
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter your password");
            etPassword.requestFocus();
            return;
        }

        // Έλεγχος αν ο χρήστης υπάρχει στη βάση
        boolean validUser = dbHelper.checkUserLogin(email, password);

        if (validUser) {
            // Αποθήκευση login session
            SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("user_email", email);
            editor.apply();

            // Μήνυμα επιτυχίας
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

            // Μετάβαση στο HomeActivity και αποστολή email χρήστη
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("user_email", email);
            startActivity(intent);

            // Κλείνουμε το login ώστε να μην επιστρέφει πίσω
            finish();

        } else {
            // Αν το login είναι λάθος
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
}