package com.example.firenze3.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firenze3.DatabaseHelper;
import com.example.firenze3.R;

import java.util.Calendar;
import java.util.Locale;

// Activity για εγγραφή νέου χρήστη
public class RegisterActivity extends AppCompatActivity {

    // Πεδία εισαγωγής στοιχείων χρήστη
    private EditText etName, etBirthDate, etEmail, etPassword, etConfirmPassword;

    // Κουμπί ολοκλήρωσης εγγραφής
    private Button btnRegisterSubmit;

    // Link για μετάβαση στο login
    private TextView tvLogin;

    // Helper για σύνδεση με βάση δεδομένων
    private DatabaseHelper dbHelper;

    // Μεταβλητές για την αποθήκευση της επιλεγμένης ημερομηνίας γέννησης
    private int selectedYear = -1;
    private int selectedMonth = -1;
    private int selectedDay = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Δημιουργία database helper
        dbHelper = new DatabaseHelper(this);

        // Σύνδεση των views από το XML
        etName = findViewById(R.id.etName);
        etBirthDate = findViewById(R.id.etBirthDate);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegisterSubmit = findViewById(R.id.btnRegisterSubmit);
        tvLogin = findViewById(R.id.tvLogin);

        // Όταν πατηθεί το πεδίο ημερομηνίας, ανοίγει DatePicker
        etBirthDate.setOnClickListener(v -> showDatePicker());

        // Όταν πατηθεί το register, γίνεται έλεγχος και εγγραφή χρήστη
        btnRegisterSubmit.setOnClickListener(v -> registerUser());

        // Όταν πατηθεί το login text, πάει στη LoginActivity
        tvLogin.setOnClickListener(v ->
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    // Εμφανίζει ημερολόγιο για επιλογή ημερομηνίας γέννησης
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        // Παίρνουμε τη σημερινή ημερομηνία ως αρχική
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RegisterActivity.this,
                (view, yearSelected, monthSelected, daySelected) -> {

                    // Αποθηκεύουμε την ημερομηνία που διάλεξε ο χρήστης
                    selectedYear = yearSelected;
                    selectedMonth = monthSelected;
                    selectedDay = daySelected;

                    // Μορφοποίηση ημερομηνίας σε dd/MM/yyyy
                    String formattedDate = String.format(
                            Locale.getDefault(),
                            "%02d/%02d/%04d",
                            selectedDay,
                            selectedMonth + 1,
                            selectedYear
                    );

                    // Εμφάνιση στο πεδίο birth date
                    etBirthDate.setText(formattedDate);
                },
                year, month, day
        );

        // Εμφάνιση του DatePicker
        datePickerDialog.show();
    }

    // Κάνει όλους τους ελέγχους και αν είναι σωστά κάνει register τον χρήστη
    private void registerUser() {

        // Παίρνουμε τα δεδομένα που έγραψε ο χρήστης
        String name = etName.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Έλεγχος αν είναι κενό το όνομα
        if (TextUtils.isEmpty(name)) {
            etName.setError("Enter your full name");
            etName.requestFocus();
            return;
        }

        // Έλεγχος ελάχιστου συνολικού μήκους (3 χαρακτήρες)
        if (name.length() < 3) {
            etName.setError("Name is too short");
            etName.requestFocus();
            return;
        }

        // Έλεγχος αν περιέχει τουλάχιστον δύο λέξεις (Όνομα και Επίθετο)
        String[] nameParts = name.split("\\s+");

        if (nameParts.length < 2) {
            etName.setError("Please enter both your first and last name");
            etName.requestFocus();
            return;
        }

        // Έλεγχος να μην περιέχει αριθμούς
        if (!name.matches("^[a-zA-Zα-ωΑ-Ωά-ώΆ-Ώ\\s]+$")) {
            etName.setError("Name should only contain letters");
            etName.requestFocus();
            return;
        }

        // Έλεγχος αν είναι κενή η ημερομηνία γέννησης
        if (TextUtils.isEmpty(birthDate)) {
            etBirthDate.setError("Select your date of birth");
            return;
        }

        // Έλεγχος αν είναι κενό το email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter your email");
            etEmail.requestFocus();
            return;
        }

        // Έλεγχος αν το email είναι σωστής μορφής
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email");
            etEmail.requestFocus();
            return;
        }

        // Έλεγχος αν είναι κενό το password
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter password");
            etPassword.requestFocus();
            return;
        }

        // Έλεγχος ελάχιστου μήκους password
        if (password.length() < 4) {
            etPassword.setError("Password must be at least 4 characters");
            etPassword.requestFocus();
            return;
        }

        // Έλεγχος αν είναι κενό το confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Confirm your password");
            etConfirmPassword.requestFocus();
            return;
        }

        // Έλεγχος αν τα δύο password είναι ίδια
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        // Υπολογισμός ηλικίας από την ημερομηνία γέννησης
        int age = calculateAge(selectedYear, selectedMonth, selectedDay);

        // Αν η ηλικία είναι invalid
        if (age < 0) {
            Toast.makeText(this, "Invalid birth date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Έλεγχος αν υπάρχει ήδη το email στη βάση
        if (dbHelper.checkEmailExists(email)) {
            etEmail.setError("This email already exists");
            etEmail.requestFocus();
            return;
        }

        // Προσπάθεια εισαγωγής του χρήστη στη βάση
        boolean inserted = dbHelper.insertUser(name, birthDate, age, email, password);

        if (inserted) {
            // Αν έγινε σωστά η εγγραφή
            Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();

            // Πάμε στο login και στέλνουμε το email
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.putExtra("user_email", email);
            startActivity(intent);
            finish();
        } else {
            // Αν αποτύχει η εγγραφή
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
        }
    }

    // Υπολογίζει την ηλικία από ημερομηνία γέννησης
    private int calculateAge(int year, int month, int day) {

        // Αν δεν έχει επιλεγεί σωστά ημερομηνία
        if (year == -1 || month == -1 || day == -1) {
            return -1;
        }

        Calendar today = Calendar.getInstance();
        Calendar birth = Calendar.getInstance();

        // Βάζουμε την ημερομηνία γέννησης
        birth.set(year, month, day);

        // Αν η ημερομηνία γέννησης είναι στο μέλλον, είναι λάθος
        if (birth.after(today)) {
            return -1;
        }

        // Βασικός υπολογισμός ηλικίας
        int age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);

        // Αν δεν έχουν περάσει ακόμα γενέθλια φέτος, αφαιρούμε 1
        if (today.get(Calendar.MONTH) < birth.get(Calendar.MONTH) ||
                (today.get(Calendar.MONTH) == birth.get(Calendar.MONTH)
                        && today.get(Calendar.DAY_OF_MONTH) < birth.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age;
    }
}