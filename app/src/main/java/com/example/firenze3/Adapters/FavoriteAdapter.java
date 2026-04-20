package com.example.firenze3.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firenze3.Activities.DetailActivity;
import com.example.firenze3.Model.Monument;
import com.example.firenze3.R;

import java.util.ArrayList;

// Adapter για το RecyclerView των favorite μνημείων
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    // Context της activity και λίστα με τα favorite monuments
    private final Context context;
    private final ArrayList<Monument> favoriteList;
    private final String userEmail;

    // Constructor
    public FavoriteAdapter(Context context, ArrayList<Monument> favoriteList, String userEmail) {
        this.context = context;
        this.favoriteList = favoriteList;
        this.userEmail = userEmail;
    }

    // Δημιουργεί το layout για κάθε item του RecyclerView
    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_monument, parent, false);
        return new FavoriteViewHolder(view);
    }

    // Βάζει τα δεδομένα του κάθε monument μέσα στο item
    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Monument monument = favoriteList.get(position);

        // Εμφάνιση τίτλου, υπότιτλου και περιγραφής
        holder.tvTitle.setText(monument.getTitle());
        holder.tvSubtitle.setText(monument.getSubtitle());
        holder.tvDescription.setText(monument.getDescription());

        // Βρίσκουμε την εικόνα από το όνομα που έχει αποθηκευτεί
        int imageResId = context.getResources().getIdentifier(
                monument.getImageName(),
                "drawable",
                context.getPackageName()
        );

        // Αν υπάρχει η εικόνα τη δείχνουμε, αλλιώς βάζουμε default εικόνα
        if (imageResId != 0) {
            holder.imgMonument.setImageResource(imageResId);
        } else {
            holder.imgMonument.setImageResource(R.drawable.firenze_poster);
        }

        // Όταν πατηθεί το Explore, ανοίγει η DetailActivity
        holder.btnExplore.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);

            // Στέλνουμε όλα τα στοιχεία του monument στην επόμενη activity
            intent.putExtra("monument_id", monument.getId());
            intent.putExtra("title", monument.getTitle());
            intent.putExtra("subtitle", monument.getSubtitle());
            intent.putExtra("description", monument.getDescription());
            intent.putExtra("category", monument.getCategory());
            intent.putExtra("image_name", monument.getImageName());
            intent.putExtra("latitude", monument.getLatitude());
            intent.putExtra("longitude", monument.getLongitude());
            intent.putExtra("more_info_url", monument.getMoreInfoUrl());
            intent.putExtra("user_email", userEmail);

            context.startActivity(intent);
        });

        // Στα favorites κρύβουμε το κουμπί καρδιάς
        holder.btnHeart.setVisibility(View.GONE);
    }

    // Επιστρέφει πόσα items έχει η λίστα
    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    // ViewHolder για τα στοιχεία κάθε item
    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMonument;
        TextView tvTitle, tvSubtitle, tvDescription;
        Button btnExplore;
        android.widget.ImageButton btnHeart;

        public FavoriteViewHolder(@NonNull View itemView) {
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