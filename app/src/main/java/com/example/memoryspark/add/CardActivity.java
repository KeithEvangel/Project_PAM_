package com.example.memoryspark.add;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView; // Import added
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoryspark.AppDatabase;
import com.example.memoryspark.R;
import com.example.memoryspark.adapter.CardAdapter;
import com.example.memoryspark.item.Card;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CardActivity extends AppCompatActivity implements CardAdapter.OnCardSelectedListener {

    private AppDatabase db;
    private List<Card> cardList;
    private CardAdapter cardAdapter;
    private int deckId;
    private String deckName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_card);

        // Get intent extras
        deckId = getIntent().getIntExtra("deckId", -1);
        deckName = getIntent().getStringExtra("deckName");

        if (deckId == -1 || deckName == null || deckName.isEmpty()) {
            Toast.makeText(this, "Invalid deck. Returning to main screen.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView tvDeckTitle = findViewById(R.id.deck_title);
        tvDeckTitle.setText(deckName);

        // Initialize Room Database
        db = AppDatabase.getInstance(this);

        // Initialize card list and adapter
        cardList = new ArrayList<>();
        cardAdapter = new CardAdapter(cardList, this); // Pass 'this' as the listener

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // Setup RecyclerView
        RecyclerView cardRecyclerView = findViewById(R.id.cardRecyclerView);
        cardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardRecyclerView.setAdapter(cardAdapter);

        // Setup FloatingActionButton
        LinearLayout addCardBtn = findViewById(R.id.addCardBtn);
        addCardBtn.setOnClickListener(view -> showAddCardDialog());

        // Fetch Cards from Room
        fetchCards();
    }

    // Handle Toolbar back button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Show Add Card Dialog
    private void showAddCardDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_card, null);

        // Create dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        // Access views inside the dialog
        TextInputEditText questionsInput = dialogView.findViewById(R.id.questionInput);
        TextInputEditText answerInput = dialogView.findViewById(R.id.answerInput);
        MaterialButton btnSave = dialogView.findViewById(R.id.btn_save);
        MaterialButton btnCancel = dialogView.findViewById(R.id.btn_cancel);

        btnSave.setOnClickListener(view -> {
            String question = Objects.requireNonNull(questionsInput.getText()).toString().trim();
            String answer = Objects.requireNonNull(answerInput.getText()).toString().trim();
            if (!question.isEmpty() && !answer.isEmpty()) {
                Card newCard = new Card(question, answer, deckId);
                new InsertCardTask().execute(newCard);
            } else {
                Toast.makeText(CardActivity.this, "Please enter both question and answer", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // Show Edit Card Dialog
    private void showEditCardDialog(Card card, int position) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_card, null);

        // Create dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        // Access views inside the dialog
        TextView tvTitle = dialogView.findViewById(R.id.tv_title);
        TextInputEditText questionsInput = dialogView.findViewById(R.id.questionInput);
        TextInputEditText answerInput = dialogView.findViewById(R.id.answerInput);
        MaterialButton btnSave = dialogView.findViewById(R.id.btn_save);
        MaterialButton btnCancel = dialogView.findViewById(R.id.btn_cancel);

        questionsInput.setText(card.getQuestion());
        answerInput.setText(card.getAnswer());

        tvTitle.setText("Edit Card");

        btnSave.setOnClickListener(view -> {
            String question = Objects.requireNonNull(questionsInput.getText()).toString().trim();
            String answer = Objects.requireNonNull(answerInput.getText()).toString().trim();
            if (!question.isEmpty() && !answer.isEmpty()) {
                card.setQuestion(question);
                card.setAnswer(answer);
                new UpdateCardTask(card, position).execute();
            } else {
                Toast.makeText(CardActivity.this, "Please enter both question and answer", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // Confirm Delete Card
    private void confirmDeleteCard(Card card, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Card");
        builder.setMessage("Are you sure you want to delete this card?");
        builder.setPositiveButton("Yes", (dialog, which) -> new DeleteCardTask(card, position).execute());
        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // Fetch cards from the Room database
    private void fetchCards() {
        new FetchCardsTask().execute();
    }

    // AsyncTask to fetch cards from Room
    private class FetchCardsTask extends AsyncTask<Void, Void, List<Card>> {
        @Override
        protected List<Card> doInBackground(Void... voids) {
            return db.cardDao().getCardsForDeck(deckId);
        }

        @Override
        protected void onPostExecute(List<Card> cards) {
            cardList.clear();
            cardList.addAll(cards);
            cardAdapter.notifyDataSetChanged();
        }
    }

    // AsyncTask to insert a new card into Room
    private class InsertCardTask extends AsyncTask<Card, Void, Void> {
        @Override
        protected Void doInBackground(Card... cards) {
            db.cardDao().insertCard(cards[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            fetchCards();
            Toast.makeText(CardActivity.this, "Card added successfully", Toast.LENGTH_SHORT).show();
        }
    }

    // AsyncTask to update an existing card in Room
    private class UpdateCardTask extends AsyncTask<Void, Void, Boolean> {
        private Card card;
        private int position;

        public UpdateCardTask(Card card, int position) {
            this.card = card;
            this.position = position;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                db.cardDao().updateCard(card);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                cardList.set(position, card);
                cardAdapter.notifyItemChanged(position);
                Toast.makeText(CardActivity.this, "Card updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CardActivity.this, "Failed to update card", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // AsyncTask to delete a card from Room
    private class DeleteCardTask extends AsyncTask<Void, Void, Boolean> {
        private Card card;
        private int position;

        public DeleteCardTask(Card card, int position) {
            this.card = card;
            this.position = position;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                db.cardDao().deleteCard(card);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                cardList.remove(position);
                cardAdapter.notifyItemRemoved(position);
                Toast.makeText(CardActivity.this, "Card deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CardActivity.this, "Failed to delete card", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Implementation of the new interface method to handle single clicks
    @Override
    public void onCardClicked(Card card) {
        showCardDetailsDialog(card);
    }

    // Method to display card details in a dialog
    private void showCardDetailsDialog(Card card) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Card Details");

        // Inflate a custom layout for the dialog
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_card_details, null);
        TextView questionTextView = view.findViewById(R.id.detailQuestionTextView);
        TextView answerTextView = view.findViewById(R.id.detailAnswerTextView);

        // Set the question and answer
        questionTextView.setText(card.getQuestion());
        answerTextView.setText(card.getAnswer());

        builder.setView(view);

        // Add an OK button
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        builder.show();
    }


    // Implementation of the interface methods
    @Override
    public void onCardEdit(Card card, int position) {
        showEditCardDialog(card, position);
    }

    @Override
    public void onCardDelete(Card card, int position) {
        confirmDeleteCard(card, position);
    }
}
