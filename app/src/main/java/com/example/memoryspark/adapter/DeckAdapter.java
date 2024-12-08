package com.example.memoryspark.adapter;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoryspark.R;
import com.example.memoryspark.item.Deck;

import java.util.List;

public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.DeckViewHolder> {

    private List<Deck> deckList;
    private OnDeckSelectedListener listener;

    // Interface to handle deck selection, editing, and deletion
    public interface OnDeckSelectedListener {
        void onDeckSelected(Deck deck);

        void onDeckEdit(Deck deck, int position);

        void onDeckDelete(Deck deck, int position);
    }

    public DeckAdapter(List<Deck> deckList, OnDeckSelectedListener listener) {
        this.deckList = deckList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deck, parent, false);
        return new DeckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        Deck deck = deckList.get(position);
        holder.deckNameTextView.setText(deck.getName());

        // Handle deck selection
        holder.cardView.setOnClickListener(v -> listener.onDeckSelected(deck));

        // Handle deck options (Edit/Delete) via PopupMenu
        holder.btnMenu.setOnClickListener(v -> {
            showPopupMenu(v, deck, holder.getAdapterPosition());
        });
    }

    private void showPopupMenu(View view, Deck deck, int position) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.deck_item_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit_deck) {
                listener.onDeckEdit(deck, position);
                return true;
            } else if (item.getItemId() == R.id.action_delete_deck) {
                listener.onDeckDelete(deck, position);
                return true;
            }
            return false;
        });
        popup.show();
    }

    @Override
    public int getItemCount() {
        return deckList.size();
    }

    public static class DeckViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cardView;
        TextView deckNameTextView;
        ImageView btnMenu;

        public DeckViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardCardView);
            deckNameTextView = itemView.findViewById(R.id.deckNameTextView);
            btnMenu = itemView.findViewById(R.id.btn_menu);
        }
    }
}
