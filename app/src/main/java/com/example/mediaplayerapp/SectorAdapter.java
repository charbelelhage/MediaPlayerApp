package com.example.mediaplayerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SectorAdapter extends RecyclerView.Adapter<SectorAdapter.SectorViewHolder> {
    private Context context;
    private List<String> sectors;
    private OnSectorClickListener listener;

    public SectorAdapter(Context context, List<String> sectors, OnSectorClickListener listener) {
        this.context = context;
        this.sectors = sectors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sector_card, parent, false);
        return new SectorViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SectorViewHolder holder, int position) {
        String sector = sectors.get(position);
        holder.sectorName.setText(sector);
    }

    @Override
    public int getItemCount() {
        return sectors.size();
    }

    public static class SectorViewHolder extends RecyclerView.ViewHolder {
        TextView sectorName;

        public SectorViewHolder(@NonNull View itemView, OnSectorClickListener listener) {
            super(itemView);
            sectorName = itemView.findViewById(R.id.textView_sectorName);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onSectorClicked(sectorName.getText().toString());
                }
            });
        }
    }

    public interface OnSectorClickListener {
        void onSectorClicked(String sector);
    }
}
