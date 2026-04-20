package com.example.firenze3.Model;

// Class model που αναπαριστά ένα μνημείο
// Χρησιμοποιείται για να αποθηκεύουμε και να μεταφέρουμε δεδομένα μνημείων μέσα στο app
public class Monument {

    // Μεταβλητές που περιγράφουν το μνημείο
    private int id;                // μοναδικό id από τη βάση δεδομένων
    private String title;          // τίτλος μνημείου
    private String subtitle;       // υπότιτλος / μικρή περιγραφή
    private String description;    // πλήρης περιγραφή
    private String category;       // κατηγορία (Churches, Museums κτλ)
    private String imageName;      // όνομα εικόνας από drawable
    private double latitude;       // γεωγραφικό πλάτος
    private double longitude;      // γεωγραφικό μήκος
    private String moreInfoUrl;    // link για περισσότερες πληροφορίες (web)

    // Constructor → δημιουργεί ένα object Monument με όλα τα δεδομένα
    public Monument(int id, String title, String subtitle, String description,
                    String category, String imageName, double latitude,
                    double longitude, String moreInfoUrl) {

        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.category = category;
        this.imageName = imageName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.moreInfoUrl = moreInfoUrl;
    }

    // Getter για id
    public int getId() {
        return id;
    }

    // Getter για title
    public String getTitle() {
        return title;
    }

    // Getter για subtitle
    public String getSubtitle() {
        return subtitle;
    }

    // Getter για description
    public String getDescription() {
        return description;
    }

    // Getter για category
    public String getCategory() {
        return category;
    }

    // Getter για image name
    public String getImageName() {
        return imageName;
    }

    // Getter για latitude
    public double getLatitude() {
        return latitude;
    }

    // Getter για longitude
    public double getLongitude() {
        return longitude;
    }

    // Getter για more info URL
    public String getMoreInfoUrl() {
        return moreInfoUrl;
    }
}