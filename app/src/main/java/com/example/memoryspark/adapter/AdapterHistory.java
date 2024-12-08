package com.example.memoryspark.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoryspark.item.ItemHistory;
import com.example.memoryspark.R;

import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {

    private List<ItemHistory> historyItems;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ItemHistory item);
    }

    public AdapterHistory(List<ItemHistory> items, OnItemClickListener listener) {
        this.historyItems = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemHistory item = historyItems.get(position);
        Log.d("HistoryAdapter", "Memuat item: " + item.getDeckName() + ", " + item.getNilai() + ", " + item.getTanggal());
        holder.tvDeckName.setText(item.getDeckName());
        holder.tvTanggal.setText(item.getTanggal());
        holder.tvPersentase.setText(item.getNilai());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeckName, tvTanggal, tvPersentase;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDeckName = itemView.findViewById(R.id.rowVDeckName);
            tvTanggal = itemView.findViewById(R.id.rowVTanggal);
            tvPersentase = itemView.findViewById(R.id.rowVNilai);
        }
    }
}