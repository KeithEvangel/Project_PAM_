package com.example.memoryspark.quiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoryspark.AppDatabase;
import com.example.memoryspark.R;
import com.example.memoryspark.item.Deck;
import com.example.memoryspark.dao.DeckDao;
import com.example.memoryspark.adapter.AdapterQuizDeck;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class QuizDeckList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterQuizDeck deckAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testdeck_list);

        recyclerView = findViewById(R.id.rvDeck);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Gunakan Executor untuk mengambil data dari database
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Ambil data deck dari database
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                DeckDao deckDao = db.deckDao();
                List<Deck> deckList = deckDao.getAllDecks();

                // Setelah data diambil, update UI di main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        deckAdapter = new AdapterQuizDeck(QuizDeckList.this, deckList, QuizDeckList.this::openMainActivity);
                        recyclerView.setAdapter(deckAdapter);
                    }
                });
            }
        });
    }

    // Buka aktivitas utama dengan data deck
    private void openMainActivity(Deck deck) {
        Intent intent = new Intent(QuizDeckList.this, QuestionMainActivity.class);
        intent.putExtra("deckId", deck.getId()); // ID deck
        intent.putExtra("deckName", deck.getName()); // Nama deck
        startActivity(intent);
}}