package com.example.firenze3.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firenze3.Model.MonumentStat;
import com.example.firenze3.R;

import java.util.ArrayList;

// Adapter για το RecyclerView που εμφανίζει στατιστικά μνημείων
public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticViewHolder> {

    // Λίστα με τα στατιστικά κάθε μνημείου
    private final ArrayList<MonumentStat> statList;

    // Constructor → παίρνει τη λίστα των στατιστικών
    public StatisticsAdapter(ArrayList<MonumentStat> statList) {
        this.statList = statList;
    }

    // Δημιουργεί το layout για κάθε item της λίστας
    @NonNull
    @Override
    public StatisticViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Φορτώνουμε το XML layout του item
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_statistic, parent, false);

        return new StatisticViewHolder(view);
    }

    // Συνδέει τα δεδομένα κάθε monument stat με το item view
    @Override
    public void onBindViewHolder(@NonNull StatisticViewHolder holder, int position) {

        // Παίρνουμε το stat object στη συγκεκριμένη θέση
        MonumentStat stat = statList.get(position);

        // Βάζουμε δεδομένα στα TextViews
        holder.tvStatTitle.setText(stat.getMonumentTitle());
        holder.tvOpenedCount.setText(String.valueOf(stat.getOpenedCount()));
        holder.tvListenedCount.setText(String.valueOf(stat.getListenedCount()));
    }

    // Πόσα items θα εμφανιστούν στο RecyclerView
    @Override
    public int getItemCount() {
        return statList.size();
    }

    // ViewHolder class → κρατά references στα views του item
    static class StatisticViewHolder extends RecyclerView.ViewHolder {

        TextView tvStatTitle, tvOpenedCount, tvListenedCount;

        public StatisticViewHolder(@NonNull View itemView) {
            super(itemView);

            // Σύνδεση των views από το item_statistic.xml
            tvStatTitle = itemView.findViewById(R.id.tvStatTitle);
            tvOpenedCount = itemView.findViewById(R.id.tvOpenedCount);
            tvListenedCount = itemView.findViewById(R.id.tvListenedCount);
        }
    }
}