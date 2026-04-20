package com.example.firenze3.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firenze3.Activities.DetailActivity;
import com.example.firenze3.DatabaseHelper;
import com.example.firenze3.Model.Monument;
import com.example.firenze3.R;

import java.util.ArrayList;

// Adapter για το RecyclerView που εμφανίζει όλα τα μνημεία στην αρχική σελίδα
public class MonumentAdapter extends RecyclerView.Adapter<MonumentAdapter.MonumentViewHolder> {

    // Context της activity
    private final Context context;

    // Λίστα με τα μνημεία που θα εμφανιστούν
    private ArrayList<Monument> monumentList;

    // Helper για επικοινωνία με τη βάση
    private final DatabaseHelper dbHelper;

    // Email του τρέχοντος χρήστη
    private final String currentUserEmail;

    // Constructor
    public MonumentAdapter(Context context, ArrayList<Monument> monumentList, String currentUserEmail) {
        this.context = context;
        this.monumentList = monumentList;
        this.currentUserEmail = currentUserEmail;
        this.dbHelper = new DatabaseHelper(context);
    }

    // Ενημερώνει τη λίστα μετά από search / filter
    public void setFilteredList(ArrayList<Monument> filteredList) {
        this.monumentList = filteredList;
        notifyDataSetChanged(); // ανανεώνει το RecyclerView
    }

    // Δημιουργεί το layout για κάθε item της λίστας
    @NonNull
    @Override
    public MonumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_monument, parent, false);
        return new MonumentViewHolder(view);
    }

    // Βάζει τα δεδομένα κάθε μνημείου στο αντίστοιχο item
    @Override
    public void onBindViewHolder(@NonNull MonumentViewHolder holder, int position) {
        Monument monument = monumentList.get(position);

        // Εμφάνιση title, subtitle και description
        holder.tvTitle.setText(monument.getTitle());
        holder.tvSubtitle.setText(monument.getSubtitle());
        holder.tvDescription.setText(monument.getDescription());

        // Βρίσκουμε την εικόνα από το όνομα που έχει το monument
        int imageResId = context.getResources().getIdentifier(
                monument.getImageName(),
                "drawable",
                context.getPackageName()
        );

        // Αν υπάρχει εικόνα, τη δείχνουμε
        // αλλιώς βάζουμε default εικόνα
        if (imageResId != 0) {
            holder.imgMonument.setImageResource(imageResId);
        } else {
            holder.imgMonument.setImageResource(R.drawable.firenze_poster);
        }

        // Όταν πατηθεί το Explore, ανοίγει η DetailActivity
        holder.btnExplore.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);

            // Στέλνουμε όλα τα στοιχεία του monument στη DetailActivity
            intent.putExtra("monument_id", monument.getId());
            intent.putExtra("title", monument.getTitle());
            intent.putExtra("subtitle", monument.getSubtitle());
            intent.putExtra("description", monument.getDescription());
            intent.putExtra("category", monument.getCategory());
            intent.putExtra("image_name", monument.getImageName());
            intent.putExtra("latitude", monument.getLatitude());
            intent.putExtra("longitude", monument.getLongitude());
            intent.putExtra("more_info_url", monument.getMoreInfoUrl());
            intent.putExtra("user_email", currentUserEmail);

            context.startActivity(intent);
        });

        // Ελέγχουμε αν το monument είναι favorite για τον συγκεκριμένο χρήστη
        boolean favorite = dbHelper.isFavorite(currentUserEmail, monument.getId());

        // Αν είναι favorite βάζουμε γεμάτη καρδιά, αλλιώς άδεια
        holder.btnHeart.setImageResource(
                favorite ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline
        );

        // Click στο κουμπί καρδιάς
        holder.btnHeart.setOnClickListener(v -> {

            // Ξαναελέγχουμε αν είναι favorite εκείνη τη στιγμή
            boolean isFavNow = dbHelper.isFavorite(currentUserEmail, monument.getId());

            if (isFavNow) {
                // Αν είναι ήδη favorite, το αφαιρούμε από τη βάση
                dbHelper.removeFavorite(currentUserEmail, monument.getId());

                // Και αλλάζουμε το icon σε άδεια καρδιά
                holder.btnHeart.setImageResource(R.drawable.ic_heart_outline);
            } else {
                // Αν δεν είναι favorite, το προσθέτουμε στη βάση
                dbHelper.addFavorite(currentUserEmail, monument.getId());

                // Και αλλάζουμε το icon σε γεμάτη καρδιά
                holder.btnHeart.setImageResource(R.drawable.ic_heart_filled);
            }
        });
    }

    // Επιστρέφει πόσα items έχει η λίστα
    @Override
    public int getItemCount() {
        return monumentList.size();
    }

    // ViewHolder για κάθε item του RecyclerView
    static class MonumentViewHolder extends RecyclerView.ViewHolder {

        ImageView imgMonument;
        TextView tvTitle, tvSubtitle, tvDescription;
        Button btnExplore;
        ImageButton btnHeart;

        public MonumentViewHolder(@NonNull View itemView) {
            super(itemView);

            // Σύνδεση των views του item xml
            imgMonument = itemView.findViewById(R.id.imgMonument);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubtitle = itemView.findViewById(R.id.tvSubtitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnExplore = itemView.findViewById(R.id.btnExplore);
            btnHeart = itemView.findViewById(R.id.btnHeart);
        }
    }
}