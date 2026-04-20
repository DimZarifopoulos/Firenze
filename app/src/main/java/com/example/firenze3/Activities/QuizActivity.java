package com.example.firenze3.Activities;

import android.widget.ImageView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firenze3.DatabaseHelper;
import com.example.firenze3.R;

public class QuizActivity extends AppCompatActivity {
    TextView answer1, answer2, answer3, answer4;
    TextView tvQuestion, tvFeedback, tvScore;
    ImageView imgMonument;
    TextView navHome, navFav, navQuiz, navProfile;

    Button btnNext;
    int score, questionIndex = 0;
    int totalQuestions = 5;
    boolean answered = false;
    int correctAnswer;
    private DatabaseHelper dbHelper;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Δημιουργία σύνδεσης με βάση δεδομένων
        dbHelper = new DatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        currentUserEmail = prefs.getString("user_email", null);
        if (currentUserEmail == null) {
            currentUserEmail = getIntent().getStringExtra("user_email");
        }
        imgMonument = findViewById(R.id.imgMonument);
        // Σύνδεση των views από το XML
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvFeedback = findViewById(R.id.tvFeedback);
        tvScore = findViewById(R.id.tvScore);
        btnNext = findViewById(R.id.btnNext);

        // Listeners
        answer1.setOnClickListener(v -> checkAnswer(1, answer1));
        answer2.setOnClickListener(v -> checkAnswer(2, answer2));
        answer3.setOnClickListener(v -> checkAnswer(3, answer3));
        answer4.setOnClickListener(v -> checkAnswer(4, answer4));

        btnNext.setOnClickListener(v -> {

            questionIndex++;

            if (questionIndex < totalQuestions) {
                answered = false;
                resetAnswersUI();
                loadNewQuestion();
            } else {
                saveQuizResult();
            }
        });

        loadNewQuestion();
        setupBottomNav();
    }


    private void loadNewQuestion() {
        Cursor cursor = dbHelper.getRandomQuestion();

        if (cursor != null && cursor.moveToFirst()) {
            tvQuestion.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUESTION_TEXT)));
            answer1.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ANSWER_1)));
            answer2.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ANSWER_2)));
            answer3.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ANSWER_3)));
            answer4.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ANSWER_4)));
            correctAnswer = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CORRECT_ANSWER));

            int imageColumnIndex = cursor.getColumnIndex("image");
            if (imageColumnIndex != -1) {
                String imageName = cursor.getString(imageColumnIndex);
                int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());

                if (imageResId != 0) {
                    imgMonument.setImageResource(imageResId);
                } else {
                    imgMonument.setImageResource(R.drawable.firenze_poster);
                }
            } else {
                imgMonument.setImageResource(R.drawable.firenze_poster);
            }

            cursor.close();
        }
    }

    private void saveQuizResult() {
        dbHelper.insertQuizResult(currentUserEmail, score);

        startActivity(new Intent(this, ProfileActivity.class));
        finish();
    }

    private void checkAnswer(int selectedAnswer, TextView selectedView) {
        if (answered) return;

        answered = true;

        if (selectedAnswer == correctAnswer) {
            selectedView.setBackgroundResource(R.drawable.bg_answer_correct);
            tvFeedback.setText("Σωστή απάντηση!");
            score++;
        } else {
            selectedView.setBackgroundResource(R.drawable.bg_answer_wrong);
            tvFeedback.setText("Λάθος απάντηση!");
            showCorrectAnswer();
        }

        tvScore.setText("Score: " + score);
    }

    private void showCorrectAnswer() {
        if (correctAnswer == 1) answer1.setBackgroundResource(R.drawable.bg_answer_correct);
        if (correctAnswer == 2) answer2.setBackgroundResource(R.drawable.bg_answer_correct);
        if (correctAnswer == 3) answer3.setBackgroundResource(R.drawable.bg_answer_correct);
        if (correctAnswer == 4) answer4.setBackgroundResource(R.drawable.bg_answer_correct);
    }

    private void resetAnswersUI() {
        // Επαναφορά των φόντων στο αρχικό drawable
        answer1.setBackgroundResource(R.drawable.bg_answer_default);
        answer2.setBackgroundResource(R.drawable.bg_answer_default);
        answer3.setBackgroundResource(R.drawable.bg_answer_default);
        answer4.setBackgroundResource(R.drawable.bg_answer_default);
        tvFeedback.setText("");

        answered = false;
    }

    private void setupBottomNav() {
        navHome = findViewById(R.id.navHome);
        navFav = findViewById(R.id.navFav);
        navQuiz = findViewById(R.id.navQuiz);
        navProfile = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v ->
                startActivity(new Intent(QuizActivity.this, HomeActivity.class)));

        navFav.setOnClickListener(v ->
                startActivity(new Intent(QuizActivity.this, FavoritesActivity.class)));

        navQuiz.setOnClickListener(v -> {
        });

        navProfile.setOnClickListener(v ->
                startActivity(new Intent(QuizActivity.this, ProfileActivity.class)));
    }
}