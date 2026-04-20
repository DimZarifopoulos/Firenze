package com.example.firenze3.Model;

// Class model που κρατάει στατιστικά χρήσης για κάθε μνημείο
// Χρησιμοποιείται στη σελίδα Statistics για να δείχνει πόσες φορές
// άνοιξε ή άκουσε ο χρήστης ένα μνημείο
public class MonumentStat {

    // Τίτλος μνημείου
    private final String monumentTitle;

    // Πόσες φορές άνοιξε το μνημείο
    private final int openedCount;

    // Πόσες φορές άκουσε την περιγραφή
    private final int listenedCount;

    // Constructor → δημιουργεί object MonumentStat με όλα τα δεδομένα
    public MonumentStat(String monumentTitle, int openedCount, int listenedCount) {
        this.monumentTitle = monumentTitle;
        this.openedCount = openedCount;
        this.listenedCount = listenedCount;
    }

    // Getter για τίτλο μνημείου
    public String getMonumentTitle() {
        return monumentTitle;
    }

    // Getter για opened count
    public int getOpenedCount() {
        return openedCount;
    }

    // Getter για listened count
    public int getListenedCount() {
        return listenedCount;
    }
}