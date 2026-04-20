package com.example.firenze3.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firenze3.DatabaseHelper;
import com.example.firenze3.R;

// Activity για επεξεργασία στοιχείων προφίλ
public class EditProfileActivity extends AppCompatActivity {

    // Πεδία εισαγωγής για όνομα και ηλικία
    EditText etName, etAge;

    // Κουμπιά αποθήκευσης και επιστροφής
    Button btnSaveProfile, btnBackProfile;

    // Κάτω navigation menu
    TextView navHome, navFav, navQuiz, navProfile;

    // Helper για τη βάση δεδομένων
    private DatabaseHelper dbHelper;

    // Μεταβλητές για να κρατήσουμε τα στοιχεία που δεν αλλάζουν
    private String currentEmail;
    private String fixedBirthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Δημιουργία σύνδεσης με βάση δεδομένων
        dbHelper = new DatabaseHelper(this);

        // Παίρνουμε το email από το session (SharedPreferences)
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        currentEmail = prefs.getString("user_email", null);
        if (currentEmail == null) {
            currentEmail = getIntent().getStringExtra("user_email");
        }

        // Σύνδεση των στοιχείων του XML με τη Java
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnBackProfile = findViewById(R.id.btnBackProfile);

        // Φορτώνουμε στοιχεία χρήστη
        loadUserData();

        // Save profile
        btnSaveProfile.setOnClickListener(v -> saveChanges());

        // Όταν πατηθεί το back, επιστρέφει στη σελίδα προφίλ
        btnBackProfile.setOnClickListener(v -> {
            startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
            finish(); // κλείνει την τωρινή activity
        });

        // Ρύθμιση του κάτω menu πλοήγησης
        setupBottomNav();
    }

    // Μέθοδος για την φόρτωση των στοιχείων του χρήστη από τη βάση
    private void loadUserData() {

        Cursor cursor = dbHelper.getUserByEmail(currentEmail);

        if (cursor != null && cursor.moveToFirst()) {

            // Παίρνουμε τα στοιχεία από την βάση
            // Η μέθοδος updateUser() απαιτεί (oldEmail, newName, newBirthDate, newAge, newEmail)
            // οπότε αποθηκεύουμε σε μεταβλητές fixedBirthDate, currentEmail και τα στοιχεία που δεν θα αλλάξουν
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_NAME));
            int age = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_AGE));
            fixedBirthDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_BIRTH_DATE));

            // Γεμίζουμε τα πεδία με τα τρέχοντα στοιχεία
            etName.setText(name);
            etAge.setText(String.valueOf(age));
        }

        if (cursor != null)
            cursor.close();
    }

    // Μέθοδος για να αποθηκεύσουμε τις αλλαγές
    private void saveChanges() {

        // Παίρνουμε τα νέα στοιχεία από τα EditText
        String newName = etName.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();

        // Έλεγχος για κενά
        if (TextUtils.isEmpty(newName)) {
            etName.setError("Enter name");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(ageStr)) {
            etAge.setError("Enter age");
            etAge.requestFocus();
            return;
        }

        // Έλεγχος ονόματος να έχει ελάχιστο συνολικό μήκος (3 χαρακτήρες)
        if (newName.length() < 3) {
            etName.setError("Name is too short");
            etName.requestFocus();
            return;
        }

        // Έλεγχος αν περιέχει τουλάχιστον δύο λέξεις (Όνομα και Επίθετο)
        String[] nameParts = newName.split("\\s+");

        if (nameParts.length < 2) {
            etName.setError("Please enter both your first and last name");
            etName.requestFocus();
            return;
        }

        // Έλεγχος να μην περιέχει αριθμούς
        if (!newName.matches("^[a-zA-Zα-ωΑ-Ωά-ώΆ-Ώ\\s]+$")) {
            etName.setError("Name should only contain letters");
            etName.requestFocus();
            return;
        }

        // Έλεγχος ηλικίας για αριθμό
        int newAge;

        try {
            newAge = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            etAge.setError("Invalid age");
            etAge.requestFocus();
            return;
        }

        // Μεταξύ 12 και 110 ετών
        if (newAge < 12 || newAge > 110) {
            etAge.setError("Age must be between 12 and 110");
            etAge.requestFocus();
            return;
        }

        // Ενημέρωση στη βάση δεδομένων χρησιμοποιώντας:
        // - Τις νέες τιμές από τα EditText (newName, newAge)
        // - Τις παλιές τιμές που δεν αλλάζουν (fixedBirthDate, currentEmail)
        boolean isUpdated = dbHelper.updateUser(currentEmail, newName, fixedBirthDate, newAge, currentEmail);

        if (isUpdated) {
            Toast.makeText(this, "Το προφίλ ενημερώθηκε!", Toast.LENGTH_SHORT).show();

            // Επιστροφή στο ProfileActivity για να δει τις αλλαγές
            finish();
        } else {
            Toast.makeText(this, "Σφάλμα κατά την αποθήκευση", Toast.LENGTH_SHORT).show();
        }
    }

    // Μέθοδος για το κάτω navigation
    private void setupBottomNav() {
        navHome = findViewById(R.id.navHome);
        navFav = findViewById(R.id.navFav);
        navQuiz = findViewById(R.id.navQuiz);
        navProfile = findViewById(R.id.navProfile);

        // Μετάβαση στο Home
        navHome.setOnClickListener(v ->
                startActivity(new Intent(EditProfileActivity.this, HomeActivity.class)));

        // Μετάβαση στα Favorites
        navFav.setOnClickListener(v ->
                startActivity(new Intent(EditProfileActivity.this, FavoritesActivity.class)));

        // Μετάβαση στο Quiz
        navQuiz.setOnClickListener(v ->
                startActivity(new Intent(EditProfileActivity.this, QuizActivity.class)));

        // Μετάβαση στο Profile
        navProfile.setOnClickListener(v ->
                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class)));
    }
}