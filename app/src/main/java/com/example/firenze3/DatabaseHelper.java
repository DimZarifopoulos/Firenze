package com.example.firenze3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.firenze3.Model.Monument;
import com.example.firenze3.Model.MonumentStat;

import java.util.ArrayList;

// Κλάση που διαχειρίζεται όλη τη βάση δεδομένων της εφαρμογής
public class DatabaseHelper extends SQLiteOpenHelper {

    // Όνομα βάσης και version (αν αλλάξει version -> γίνεται recreate)
    private static final String DATABASE_NAME = "cityguide.db";
    private static final int DATABASE_VERSION = 7;

    // Πίνακες
    public static final String TABLE_USERS = "users";
    public static final String TABLE_MONUMENTS = "monuments";

    // ----------- USERS COLUMNS -----------
    public static final String COL_USER_ID = "id";
    public static final String COL_USER_NAME = "name";
    public static final String COL_USER_BIRTH_DATE = "birth_date";
    public static final String COL_USER_AGE = "age";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_PASSWORD = "password";

    // ----------- MONUMENTS COLUMNS -----------
    public static final String COL_MONUMENT_ID = "id";
    public static final String COL_MONUMENT_TITLE = "title";
    public static final String COL_MONUMENT_SUBTITLE = "subtitle";
    public static final String COL_MONUMENT_DESCRIPTION = "description";
    public static final String COL_MONUMENT_CATEGORY = "category";
    public static final String COL_MONUMENT_IMAGE_NAME = "image_name";
    public static final String COL_MONUMENT_LATITUDE = "latitude";
    public static final String COL_MONUMENT_LONGITUDE = "longitude";
    public static final String COL_MONUMENT_MORE_INFO_URL = "more_info_url";

    // ----------- FAVORITES TABLE -----------
    public static final String TABLE_FAVORITES = "favorites";
    public static final String COL_FAVORITE_ID = "id";
    public static final String COL_FAVORITE_USER_EMAIL = "user_email";
    public static final String COL_FAVORITE_MONUMENT_ID = "monument_id";

    // ----------- USAGE STATS TABLE -----------
    public static final String TABLE_USAGE_STATS = "usage_stats";
    public static final String COL_USAGE_ID = "id";
    public static final String COL_USAGE_USER_EMAIL = "user_email";
    public static final String COL_USAGE_MONUMENT_ID = "monument_id";
    public static final String COL_USAGE_OPENED_COUNT = "opened_count";
    public static final String COL_USAGE_LISTENED_COUNT = "listened_count";

    // ----------- QUIZ QUESTIONS TABLE -----------
    public static final String TABLE_QUIZ_QUESTIONS = "questions";
    public static final String COL_QUESTION_ID = "id";
    public static final String COL_QUESTION_TEXT = "question_text";
    public static final String COL_ANSWER_1 = "answer1";
    public static final String COL_ANSWER_2 = "answer2";
    public static final String COL_ANSWER_3 = "answer3";
    public static final String COL_ANSWER_4 = "answer4";
    public static final String COL_CORRECT_ANSWER = "correct_answer"; // 1, 2, 3 ή 4
    public static final String COL_QUESTION_MONUMENT_ID = "monument_id";

    // ----------- QUIZ RESULTS TABLE -----------
    public static final String TABLE_QUIZ_RESULTS = "quiz_results";
    public static final String COL_QUIZ_ID = "id";
    public static final String COL_QUIZ_USER_EMAIL = "user_email";
    public static final String COL_QUIZ_SCORE = "score";

    // Constructor
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Δημιουργία όλων των πινάκων
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Πίνακας χρηστών
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " ("
                + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USER_NAME + " TEXT NOT NULL, "
                + COL_USER_BIRTH_DATE + " TEXT NOT NULL, "
                + COL_USER_AGE + " INTEGER NOT NULL, "
                + COL_USER_EMAIL + " TEXT NOT NULL UNIQUE, "
                + COL_USER_PASSWORD + " TEXT NOT NULL"
                + ")";

        // Πίνακας μνημείων
        String createMonumentsTable = "CREATE TABLE " + TABLE_MONUMENTS + " ("
                + COL_MONUMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MONUMENT_TITLE + " TEXT NOT NULL, "
                + COL_MONUMENT_SUBTITLE + " TEXT NOT NULL, "
                + COL_MONUMENT_DESCRIPTION + " TEXT NOT NULL, "
                + COL_MONUMENT_CATEGORY + " TEXT NOT NULL, "
                + COL_MONUMENT_IMAGE_NAME + " TEXT NOT NULL, "
                + COL_MONUMENT_LATITUDE + " REAL NOT NULL, "
                + COL_MONUMENT_LONGITUDE + " REAL NOT NULL, "
                + COL_MONUMENT_MORE_INFO_URL + " TEXT NOT NULL"
                + ")";

        // Πίνακας favorites (ποια μνημεία έχει κάνει favorite ο χρήστης)
        String createFavoritesTable = "CREATE TABLE " + TABLE_FAVORITES + " ("
                + COL_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_FAVORITE_USER_EMAIL + " TEXT NOT NULL, "
                + COL_FAVORITE_MONUMENT_ID + " INTEGER NOT NULL"
                + ")";

        // Πίνακας στατιστικών χρήσης (πόσες φορές άνοιξε/άκουσε μνημείο)
        String createUsageStatsTable = "CREATE TABLE " + TABLE_USAGE_STATS + " ("
                + COL_USAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USAGE_USER_EMAIL + " TEXT NOT NULL, "
                + COL_USAGE_MONUMENT_ID + " INTEGER NOT NULL, "
                + COL_USAGE_OPENED_COUNT + " INTEGER DEFAULT 0, "
                + COL_USAGE_LISTENED_COUNT + " INTEGER DEFAULT 0"
                + ")";

        // Πίνακας quiz results
        String createQuizResultsTable = "CREATE TABLE " + TABLE_QUIZ_RESULTS + " ("
                + COL_QUIZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_QUIZ_USER_EMAIL + " TEXT NOT NULL, "
                + COL_QUIZ_SCORE + " INTEGER "
                + ")";

        // Πίνακας quiz questions
        String createQuestionsTable = "CREATE TABLE " + TABLE_QUIZ_QUESTIONS + " ("
                + COL_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_QUESTION_TEXT + " TEXT NOT NULL, "
                + COL_ANSWER_1 + " TEXT NOT NULL, "
                + COL_ANSWER_2 + " TEXT NOT NULL, "
                + COL_ANSWER_3 + " TEXT NOT NULL, "
                + COL_ANSWER_4 + " TEXT NOT NULL, "
                + COL_CORRECT_ANSWER + " INTEGER NOT NULL, "
                + COL_QUESTION_MONUMENT_ID + " INTEGER NOT NULL, "
                + "FOREIGN KEY (" + COL_QUESTION_MONUMENT_ID + ") REFERENCES "
                + TABLE_MONUMENTS + "(" + COL_MONUMENT_ID + ")"
                + ")";

        // Εκτέλεση SQL για δημιουργία πινάκων
        db.execSQL(createUsageStatsTable);
        db.execSQL(createFavoritesTable);
        db.execSQL(createUsersTable);
        db.execSQL(createMonumentsTable);
        db.execSQL(createQuizResultsTable);
        db.execSQL(createQuestionsTable);

        // Βάζουμε default μνημεία
        insertDefaultMonuments(db);
        insertDefaultQuizQuestions(db);
    }


    // Αν αλλάξει version -> σβήνουμε και ξαναφτιάχνουμε τη βάση
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_RESULTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USAGE_STATS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONUMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ---------------- USERS ----------------

    public boolean insertUser(String name, String birthDate, int age, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String formattedName = formatName(name);

        values.put(COL_USER_NAME, formattedName);
        values.put(COL_USER_BIRTH_DATE, birthDate);
        values.put(COL_USER_AGE, age);
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USER_EMAIL + " = ?",
                new String[]{email}
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkUserLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USER_EMAIL + " = ? AND " + COL_USER_PASSWORD + " = ?",
                new String[]{email, password}
        );

        boolean valid = cursor.getCount() > 0;
        cursor.close();
        return valid;
    }

    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USER_EMAIL + " = ?",
                new String[]{email}
        );
    }

    public boolean updateUser(String oldEmail, String newName, String newBirthDate, int newAge, String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String formattedName = formatName(newName);

        values.put(COL_USER_NAME, formattedName);
        values.put(COL_USER_BIRTH_DATE, newBirthDate);
        values.put(COL_USER_AGE, newAge);
        values.put(COL_USER_EMAIL, newEmail);

        int result = db.update(TABLE_USERS, values, COL_USER_EMAIL + " = ?", new String[]{oldEmail});
        return result > 0;
    }

    private String formatName(String name) {
        if (name == null || name.isEmpty()) return name;

        String[] words = name.toLowerCase().trim().split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return sb.toString().trim();
    }

    // ---------------- MONUMENTS ----------------

    private void insertDefaultMonuments(SQLiteDatabase db) {
        insertMonument(db,
                "Florence Cathedral",
                "Cathedral",
                "Ο Καθεδρικός Ναός Santa Maria del Fiore είναι το πιο διάσημο αξιοθέατο της Φλωρεντίας με τον εντυπωσιακό τρούλο του Brunelleschi.",
                "Churches",
                "duomo",
                43.7731,
                11.2560,
                "https://en.wikipedia.org/wiki/Florence_Cathedral"
        );

        insertMonument(db,
                "Ponte Vecchio",
                "Medieval Bridge",
                "Η Ponte Vecchio είναι η πιο γνωστή γέφυρα της Φλωρεντίας, διάσημη για τα κοσμηματοπωλεία της.",
                "Bridges",
                "ponte_vecchio",
                43.7679,
                11.2531,
                "https://en.wikipedia.org/wiki/Ponte_Vecchio"
        );

        insertMonument(db,
                "Uffizi Gallery",
                "Art Museum",
                "Η Πινακοθήκη Uffizi είναι ένα από τα σημαντικότερα μουσεία τέχνης στον κόσμο με έργα της Αναγέννησης.",
                "Museums",
                "uffizi",
                43.7687,
                11.2550,
                "https://en.wikipedia.org/wiki/Uffizi"
        );

        insertMonument(db,
                "Palazzo Vecchio",
                "Historic Palace",
                "Το Palazzo Vecchio είναι το ιστορικό δημαρχείο της Φλωρεντίας και σημαντικό πολιτικό κέντρο από τον Μεσαίωνα.",
                "Palaces",
                "palazzo_vecchio",
                43.7696,
                11.2558,
                "https://en.wikipedia.org/wiki/Palazzo_Vecchio"
        );

        insertMonument(db,
                "Piazza della Signoria",
                "Famous Square",
                "Η Piazza della Signoria είναι μία από τις πιο γνωστές πλατείες της Φλωρεντίας, γεμάτη ιστορία και γλυπτά.",
                "Squares",
                "piazza_signoria",
                43.7695,
                11.2558,
                "https://en.wikipedia.org/wiki/Piazza_della_Signoria"
        );

        insertMonument(db,
                "Porcellino",
                "Bronze Statue",
                "Το Porcellino είναι ένα χάλκινο άγαλμα αγριόχοιρου στη Φλωρεντία, που σύμφωνα με την παράδοση φέρνει καλή τύχη.",
                "Statues",
                "porcellino",
                43.7701,
                11.2552,
                "https://en.wikipedia.org/wiki/Il_Porcellino"
        );

        insertMonument(db,
                "Palazzo Pitti",
                "Renaissance Palace",
                "Το Palazzo Pitti είναι ένα μεγάλο αναγεννησιακό παλάτι που υπήρξε κατοικία της οικογένειας Medici και σήμερα στεγάζει μουσεία.",
                "Palaces",
                "palazzo_pitti",
                43.7652,
                11.2502,
                "https://en.wikipedia.org/wiki/Palazzo_Pitti"
        );

        insertMonument(db,
                "Boboli Gardens",
                "Historic Gardens",
                "Οι Κήποι Boboli είναι ένα από τα πιο σημαντικά παραδείγματα ιταλικών κήπων της Αναγέννησης και βρίσκονται πίσω από το Palazzo Pitti.",
                "Gardens",
                "boboli_gardens",
                43.7636,
                11.2486,
                "https://en.wikipedia.org/wiki/Boboli_Gardens"
        );
    }

    private void insertMonument(SQLiteDatabase db, String title, String subtitle,
                                String description, String category, String imageName,
                                double latitude, double longitude, String moreInfoUrl) {
        ContentValues values = new ContentValues();
        values.put(COL_MONUMENT_TITLE, title);
        values.put(COL_MONUMENT_SUBTITLE, subtitle);
        values.put(COL_MONUMENT_DESCRIPTION, description);
        values.put(COL_MONUMENT_CATEGORY, category);
        values.put(COL_MONUMENT_IMAGE_NAME, imageName);
        values.put(COL_MONUMENT_LATITUDE, latitude);
        values.put(COL_MONUMENT_LONGITUDE, longitude);
        values.put(COL_MONUMENT_MORE_INFO_URL, moreInfoUrl);

        db.insert(TABLE_MONUMENTS, null, values);
    }

    public ArrayList<Monument> getAllMonuments() {
        ArrayList<Monument> monumentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MONUMENTS, null);

        if (cursor.moveToFirst()) {
            do {
                Monument monument = new Monument(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_MONUMENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MONUMENT_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MONUMENT_SUBTITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MONUMENT_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MONUMENT_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MONUMENT_IMAGE_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_MONUMENT_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_MONUMENT_LONGITUDE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MONUMENT_MORE_INFO_URL))
                );

                monumentList.add(monument);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return monumentList;
    }
    // ---------------- FAVORITES ----------------
    public boolean isFavorite(String userEmail, int monumentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_FAVORITES + " WHERE "
                        + COL_FAVORITE_USER_EMAIL + " = ? AND "
                        + COL_FAVORITE_MONUMENT_ID + " = ?",
                new String[]{userEmail, String.valueOf(monumentId)}
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean addFavorite(String userEmail, int monumentId) {
        if (isFavorite(userEmail, monumentId)) {
            return true;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FAVORITE_USER_EMAIL, userEmail);
        values.put(COL_FAVORITE_MONUMENT_ID, monumentId);

        long result = db.insert(TABLE_FAVORITES, null, values);
        return result != -1;
    }

    public boolean removeFavorite(String userEmail, int monumentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(
                TABLE_FAVORITES,
                COL_FAVORITE_USER_EMAIL + " = ? AND " + COL_FAVORITE_MONUMENT_ID + " = ?",
                new String[]{userEmail, String.valueOf(monumentId)}
        );

        return result > 0;
    }

    // Παίρνει όλα τα favorite μνημεία χρήστη
    public ArrayList<Monument> getFavoriteMonuments(String userEmail) {
        ArrayList<Monument> favoriteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT m.* FROM " + TABLE_MONUMENTS + " m INNER JOIN " + TABLE_FAVORITES + " f "
                + "ON m." + COL_MONUMENT_ID + " = f." + COL_FAVORITE_MONUMENT_ID + " "
                + "WHERE f." + COL_FAVORITE_USER_EMAIL + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{userEmail});

        if (cursor.moveToFirst()) {
            do {
                Monument monument = new Monument(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_MONUMENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MONUMENT_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MONUMENT_SUBTITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MONUMENT_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MONUMENT_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MONUMENT_IMAGE_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_MONUMENT_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_MONUMENT_LONGITUDE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MONUMENT_MORE_INFO_URL))
                );
                favoriteList.add(monument);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return favoriteList;
    }
    // ---------------- USAGE STATS ----------------

    // Αυτή η μέθοδος αυξάνει κατά 1 το πόσες φορές ο χρήστης άνοιξε ένα μνημείο
    public void incrementOpenedCount(String userEmail, int monumentId) {
        // Παίρνουμε τη βάση σε write mode γιατί θα κάνουμε insert/update
        SQLiteDatabase db = this.getWritableDatabase();

        // Ψάχνουμε αν υπάρχει ήδη εγγραφή για τον συγκεκριμένο χρήστη και το συγκεκριμένο μνημείο
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USAGE_STATS + " WHERE "
                        + COL_USAGE_USER_EMAIL + " = ? AND "
                        + COL_USAGE_MONUMENT_ID + " = ?",
                new String[]{userEmail, String.valueOf(monumentId)}
        );

        // Αν υπάρχει ήδη εγγραφή
        if (cursor.moveToFirst()) {
            // Παίρνουμε το τωρινό opened_count
            int currentOpened = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USAGE_OPENED_COUNT));

            // Δημιουργούμε object με τη νέα τιμή
            ContentValues values = new ContentValues();
            values.put(COL_USAGE_OPENED_COUNT, currentOpened + 1);

            // Κάνουμε update το record αυξάνοντας το opened_count κατά 1
            db.update(
                    TABLE_USAGE_STATS,
                    values,
                    COL_USAGE_USER_EMAIL + " = ? AND " + COL_USAGE_MONUMENT_ID + " = ?",
                    new String[]{userEmail, String.valueOf(monumentId)}
            );
        } else {
            // Αν δεν υπάρχει εγγραφή, δημιουργούμε καινούρια
            ContentValues values = new ContentValues();
            values.put(COL_USAGE_USER_EMAIL, userEmail);
            values.put(COL_USAGE_MONUMENT_ID, monumentId);
            values.put(COL_USAGE_OPENED_COUNT, 1);   // πρώτη φορά που ανοίχτηκε
            values.put(COL_USAGE_LISTENED_COUNT, 0); // δεν έχει ακουστεί ακόμα

            // Εισαγωγή νέας εγγραφής
            db.insert(TABLE_USAGE_STATS, null, values);
        }

        // Κλείνουμε τον cursor
        cursor.close();
    }

    // Αυτή η μέθοδος αυξάνει κατά 1 το πόσες φορές ο χρήστης άκουσε το audio ενός μνημείου
    public void incrementListenedCount(String userEmail, int monumentId) {
        // Παίρνουμε τη βάση σε write mode γιατί θα κάνουμε insert/update
        SQLiteDatabase db = this.getWritableDatabase();

        // Ψάχνουμε αν υπάρχει ήδη εγγραφή για τον συγκεκριμένο χρήστη και μνημείο
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USAGE_STATS + " WHERE "
                        + COL_USAGE_USER_EMAIL + " = ? AND "
                        + COL_USAGE_MONUMENT_ID + " = ?",
                new String[]{userEmail, String.valueOf(monumentId)}
        );

        // Αν υπάρχει ήδη εγγραφή
        if (cursor.moveToFirst()) {
            // Παίρνουμε το τωρινό listened_count
            int currentListened = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USAGE_LISTENED_COUNT));

            // Βάζουμε τη νέα τιμή
            ContentValues values = new ContentValues();
            values.put(COL_USAGE_LISTENED_COUNT, currentListened + 1);

            // Κάνουμε update το record αυξάνοντας το listened_count κατά 1
            db.update(
                    TABLE_USAGE_STATS,
                    values,
                    COL_USAGE_USER_EMAIL + " = ? AND " + COL_USAGE_MONUMENT_ID + " = ?",
                    new String[]{userEmail, String.valueOf(monumentId)}
            );
        } else {
            // Αν δεν υπάρχει εγγραφή, δημιουργούμε νέα
            ContentValues values = new ContentValues();
            values.put(COL_USAGE_USER_EMAIL, userEmail);
            values.put(COL_USAGE_MONUMENT_ID, monumentId);
            values.put(COL_USAGE_OPENED_COUNT, 0);    // δεν έχει ανοιχτεί ακόμα
            values.put(COL_USAGE_LISTENED_COUNT, 1);  // πρώτη φορά που ακούστηκε

            // Εισαγωγή νέας εγγραφής
            db.insert(TABLE_USAGE_STATS, null, values);
        }

        // Κλείνουμε τον cursor
        cursor.close();
    }

    // Επιστρέφει στατιστικά για όλα τα μνημεία ενός χρήστη
    public ArrayList<MonumentStat> getMonumentStatsForUser(String userEmail) {
        // Λίστα που θα αποθηκεύσουμε τα στατιστικά
        ArrayList<MonumentStat> statList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Κάνουμε LEFT JOIN ώστε να εμφανίζονται όλα τα μνημεία
        // ακόμα και αν ο χρήστης δεν έχει αλληλεπιδράσει με κάποια από αυτά
        String query = "SELECT m." + COL_MONUMENT_TITLE + ", "
                + "COALESCE(u." + COL_USAGE_OPENED_COUNT + ", 0) AS opened_count, "
                + "COALESCE(u." + COL_USAGE_LISTENED_COUNT + ", 0) AS listened_count "
                + "FROM " + TABLE_MONUMENTS + " m "
                + "LEFT JOIN " + TABLE_USAGE_STATS + " u "
                + "ON m." + COL_MONUMENT_ID + " = u." + COL_USAGE_MONUMENT_ID
                + " AND u." + COL_USAGE_USER_EMAIL + " = ?";

        // Εκτέλεση query
        Cursor cursor = db.rawQuery(query, new String[]{userEmail});

        // Αν υπάρχουν αποτελέσματα
        if (cursor.moveToFirst()) {
            do {
                // Δημιουργούμε object MonumentStat για κάθε μνημείο
                MonumentStat stat = new MonumentStat(
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MONUMENT_TITLE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow("opened_count")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("listened_count"))
                );
                // Το βάζουμε στη λίστα
                statList.add(stat);
            } while (cursor.moveToNext());
        }

        // Κλείνουμε τον cursor
        cursor.close();
        return statList;
    }

    // Επιστρέφει το συνολικό πλήθος ανοιγμάτων όλων των μνημείων από έναν χρήστη
    public int getTotalOpenedCount(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Υπολογίζουμε το SUM του opened_count για τον συγκεκριμένο χρήστη
        Cursor cursor = db.rawQuery(
                "SELECT SUM(" + COL_USAGE_OPENED_COUNT + ") FROM " + TABLE_USAGE_STATS + " WHERE " + COL_USAGE_USER_EMAIL + " = ?",
                new String[]{userEmail}
        );

        int total = 0;

        // Αν υπάρχει αποτέλεσμα, το αποθηκεύουμε
        if (cursor.moveToFirst()) {
            total = cursor.isNull(0) ? 0 : cursor.getInt(0);
        }

        cursor.close();
        return total;
    }

    // Επιστρέφει το συνολικό πλήθος ακροάσεων όλων των μνημείων από έναν χρήστη
    public int getTotalListenedCount(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Υπολογίζουμε το SUM του listened_count για τον συγκεκριμένο χρήστη
        Cursor cursor = db.rawQuery(
                "SELECT SUM(" + COL_USAGE_LISTENED_COUNT + ") FROM " + TABLE_USAGE_STATS + " WHERE " + COL_USAGE_USER_EMAIL + " = ?",
                new String[]{userEmail}
        );

        int total = 0;

        // Αν υπάρχει αποτέλεσμα, το αποθηκεύουμε
        if (cursor.moveToFirst()) {
            total = cursor.isNull(0) ? 0 : cursor.getInt(0);
        }

        cursor.close();
        return total;
    }

    // Επιστρέφει πόσα favorites έχει ο χρήστης
    public int getFavoriteCount(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Μετράμε πόσες εγγραφές υπάρχουν στον πίνακα favorites για αυτό το email
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_FAVORITES + " WHERE " + COL_FAVORITE_USER_EMAIL + " = ?",
                new String[]{userEmail}
        );

        int total = 0;

        // Παίρνουμε το αποτέλεσμα του COUNT
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }

        cursor.close();
        return total;
    }
    // ---------------- QUIZ RESULTS ----------------

    // Προσθήκη νέου αποτελέσματος Quiz
    public boolean insertQuizResult(String email, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_QUIZ_USER_EMAIL, email);
        values.put(COL_QUIZ_SCORE, score);

        long result = db.insert(TABLE_QUIZ_RESULTS, null, values);
        return result != -1;
    }

    // Επιστροφή πλήθους ολοκληρωμένων Quiz για το ProfileActivity
    public int getQuizCount(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_QUIZ_RESULTS + " WHERE " + COL_QUIZ_USER_EMAIL + " = ?",
                new String[]{email}
        );

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // ---------------- QUIZ QUESTIONS ----------------

    private void insertDefaultQuizQuestions(SQLiteDatabase db) {

        // 1 - DUOMO
        insertQuestion(db, "Ποιος σχεδίασε τον τρούλο του Santa Maria del Fiore;",
                "Michelangelo", "Brunelleschi", "Leonardo da Vinci", "Donatello", 2, 1);

        insertQuestion(db, "Σε ποια πόλη βρίσκεται ο καθεδρικός Santa Maria del Fiore;",
                "Ρώμη", "Μιλάνο", "Φλωρεντία", "Βενετία", 3, 1);

        // 2 - PONTE VECCHIO
        insertQuestion(db, "Για τι είναι κυρίως γνωστό το Ponte Vecchio;",
                "Για τα αγάλματα", "Για τον τρούλο", "Για τα μικρά καταστήματα πάνω στο γεφύρι", "Για το μουσείο", 3, 2);

        insertQuestion(db, "Πάνω από ποιο ποτάμι περνά η Ponte Vecchio;",
                "Τίβερη", "Άρνο", "Δούναβη", "Σηκουάνα", 2, 2);

        // 3 - UFFIZI
        insertQuestion(db, "Τι είδους μνημείο είναι η Galleria degli Uffizi;",
                "Εκκλησία", "Μουσείο τέχνης", "Γέφυρα", "Πλατεία", 2, 3);

        insertQuestion(db, "Ποιος ζωγράφισε τη Γέννηση της Αφροδίτης που εκτίθεται στην Uffizi;",
                "Michelangelo", "Leonardo", "Botticelli", "Raphael", 3, 3);

        // 4 - PALAZZO VECCHIO
        insertQuestion(db, "Ποιο κτίριο χρησιμεύει ως ιστορικό δημαρχείο της Φλωρεντίας;",
                "Uffizi", "Palazzo Vecchio", "Duomo", "Palazzo Pitti", 2, 4);

        insertQuestion(db, "Σε ποια πλατεία βρίσκεται το Palazzo Vecchio;",
                "Piazza del Duomo", "Piazza della Signoria", "Piazza Santa Croce", "Piazza della Repubblica", 2, 4);

        // 5 - PIAZZA DELLA SIGNORIA
        insertQuestion(db, "Ποια πλατεία της Φλωρεντίας είναι διάσημη για τα γλυπτά της;",
                "Piazza del Duomo", "Piazza Santa Croce", "Piazza della Signoria", "Piazza della Repubblica", 3, 5);

        insertQuestion(db, "Ποιο σημαντικό κτίριο βρίσκεται στην Piazza della Signoria;",
                "Duomo", "Palazzo Vecchio", "Uffizi", "Santa Croce", 2, 5);

        // 6 - PORCELLINO
        insertQuestion(db, "Τι είναι το Porcellino;",
                "Άγαλμα λιονταριού", "Άγαλμα αγριόχοιρου", "Σιντριβάνι", "Εκκλησία", 2, 6);

        insertQuestion(db, "Τι κάνουν οι επισκέπτες στο Porcellino για καλή τύχη;",
                "Αφήνουν λουλούδια", "Τρίβουν τη μύτη", "Ρίχνουν νερό", "Ανάβουν κερί", 2, 6);

        // 7 - PALAZZO PITTI
        insertQuestion(db, "Ποια οικογένεια έζησε στο Palazzo Pitti;",
                "Borgia", "Medici", "Sforza", "Colonna", 2, 7);

        insertQuestion(db, "Τι βρίσκεται πίσω από το Palazzo Pitti;",
                "Duomo", "Ponte Vecchio", "Boboli Gardens", "Uffizi", 3, 7);

        // 8 - BOBOLI GARDENS
        insertQuestion(db, "Τι είναι οι Boboli Gardens;",
                "Μουσείο", "Κήποι", "Εκκλησία", "Γέφυρα", 2, 8);

        insertQuestion(db, "Οι Boboli Gardens βρίσκονται πίσω από ποιο παλάτι;",
                "Palazzo Vecchio", "Palazzo Pitti", "Duomo", "Uffizi", 2, 8);
    }
    // Helper μέθοδος για την εισαγωγή
    private void insertQuestion(SQLiteDatabase db, String text, String a1, String a2, String a3, String a4, int correct, int monumentId) {
        ContentValues values = new ContentValues();
        values.put(COL_QUESTION_TEXT, text);
        values.put(COL_ANSWER_1, a1);
        values.put(COL_ANSWER_2, a2);
        values.put(COL_ANSWER_3, a3);
        values.put(COL_ANSWER_4, a4);
        values.put(COL_CORRECT_ANSWER, correct);
        values.put(COL_QUESTION_MONUMENT_ID, monumentId);
        db.insert(TABLE_QUIZ_QUESTIONS, null, values);
    }

    // Helper μέθοδος που μας δίνει μια τυχαία ερώτηση
    public Cursor getRandomQuestion() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query =
                "SELECT q.*, m." + COL_MONUMENT_IMAGE_NAME + " AS image " +
                        "FROM " + TABLE_QUIZ_QUESTIONS + " q " +
                        "INNER JOIN " + TABLE_MONUMENTS + " m " +
                        "ON q." + COL_QUESTION_MONUMENT_ID + " = m." + COL_MONUMENT_ID + " " +
                        "ORDER BY RANDOM() LIMIT 1";

        return db.rawQuery(query, null);
    }
}