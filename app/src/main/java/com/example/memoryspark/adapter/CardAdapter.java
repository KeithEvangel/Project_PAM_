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
import com.example.memoryspark.item.Card;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<Card> cardList;
    private OnCardSelectedListener listener;

    // Updated Interface to include onCardClicked
    public interface OnCardSelectedListener {
        void onCardEdit(Card card, int position);

        void onCardDelete(Card card, int position);

        void onCardClicked(Card card); // New method for single clicks
    }

    public CardAdapter(List<Card> cardList, OnCardSelectedListener listener) {
        this.cardList = cardList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cardList.get(position);
        holder.questionText.setText(card.getQuestion());

        // Handle single click to show details
        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCardClicked(card);
            }
        });

        // Handle card options (Edit/Delete) via PopupMenu on long click
        holder.btnMenu.setOnClickListener(v -> {
            showPopupMenu(v, card, holder.getAdapterPosition());
        });
    }

    private void showPopupMenu(View view, Card card, int position) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_item_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit_card) {
                listener.onCardEdit(card, position);
                return true;
            } else if (item.getItemId() == R.id.action_delete_card) {
                listener.onCardDelete(card, position);
                return true;
            }
            return false;
        });
        popup.show();
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cardView;
        TextView questionText;
        ImageView btnMenu;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ensure that cardCardView exists in item_card.xml
            cardView = itemView.findViewById(R.id.cardCardView);
            questionText = itemView.findViewById(R.id.questionTextView);
            btnMenu = itemView.findViewById(R.id.btn_menu);
        }
    }
}
