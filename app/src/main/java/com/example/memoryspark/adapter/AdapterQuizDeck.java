package com.example.memoryspark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoryspark.item.Deck;
import com.example.memoryspark.R;

import java.util.List;

// RecyclerView Adapter for displaying decks
public class AdapterQuizDeck extends RecyclerView.Adapter<AdapterQuizDeck.DeckViewHolder> {

    private List<Deck> deckList;
    private Context context;
    private DeckClickListener listener;

    // Tambahkan listener sebagai parameter constructor
    public AdapterQuizDeck(Context context, List<Deck> deckList, DeckClickListener listener) {
        this.context = context;
        this.deckList = deckList;
        this.listener = listener;  // Simpan listener
    }

    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_deck
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_select_deck, parent, false);
        return new DeckViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        // Ambil deck yang sesuai dengan posisi
        Deck deck = deckList.get(position);

        // Pastikan deck tidak null dan TextView ada
        if (deck != null && holder.deckNameTextView != null) {
            holder.deckNameTextView.setText(deck.getName());
        }

        // Set listener untuk meng-handle klik pada item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeckClick(deck);  // Panggil listener saat item diklik
            }
        });
    }

    @Override
    public int getItemCount() {
        return deckList != null ? deckList.size() : 0;
    }

    public static class DeckViewHolder extends RecyclerView.ViewHolder {
        TextView deckNameTextView;

        public DeckViewHolder(View itemView) {
            super(itemView);
            // Mendapatkan referensi ke TextView dari item layout
            deckNameTextView = itemView.findViewById(R.id.rowVDeckName);
        }
    }

    // Interface untuk klik listener
    public interface DeckClickListener {
        void onDeckClick(Deck deck);
    }
}
