package com.example.memoryspark.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.memoryspark.AppDatabase;
import com.example.memoryspark.dao.DaoQuestion;
import com.example.memoryspark.history.HistoryHasilQuiz;
import com.example.memoryspark.item.Card;
import com.example.memoryspark.item.ItemHistory;
import com.example.memoryspark.item.ItemQuestion;
import com.example.memoryspark.R;
import com.example.memoryspark.dao.DaoHistory;
import com.example.memoryspark.adapter.AdapterQuestion;
import com.example.memoryspark.history.HistoryMainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class QuestionMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterQuestion adapter;
    private AppDatabase database;
    private DaoQuestion questionDao;
    private Button buttonFinish;

    private int score = 0;
    private int currentQuestionIndex = 0;
    private List<ItemQuestion> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_question);

        // Ambil deckId dari Intent
        String deckName = getIntent().getStringExtra("deckName");

        int deckId = getIntent().getIntExtra("deckId", -1);

        if (deckId == -1) {
            Toast.makeText(this, "Invalid deck", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterQuestion(this::onOptionSelected);
        recyclerView.setAdapter(adapter);

        buttonFinish = findViewById(R.id.buttonFinish);
        buttonFinish.setOnClickListener(v -> finishQuiz());

        database = AppDatabase.getInstance(this);
        questionDao = database.questionDao();

        // Muat pertanyaan berdasarkan deckId
        loadQuestions(deckId);
    }

    private void loadQuestions(int deckId) {
        new Thread(() -> {
            // Ambil pertanyaan dari tabel cards berdasarkan deckId
            List<Card> cards = database.cardDao().getCardsForDeck(deckId);

            Random random = new Random();

            // Konversi data Card ke ItemQuestion
            for (Card card : cards) {
                // Generate opsi lain (angka acak)
                String correctAnswer = card.getAnswer();
                List<String> options = new ArrayList<>();
                options.add(correctAnswer);

                while (options.size() < 4) {
                    String randomOption = String.valueOf(random.nextInt(100)); // Angka acak 0-99
                    if (!options.contains(randomOption) && !randomOption.equals(correctAnswer)) {
                        options.add(randomOption);
                    }
                }

                // Shuffle opsi
                Collections.shuffle(options);

                // Tambahkan ke daftar pertanyaan
                questions.add(new ItemQuestion(
                        card.getQuestion(),
                        options.get(0),
                        options.get(1),
                        options.get(2),
                        options.get(3),
                        correctAnswer // Tetap simpan jawaban benar
                ));
            }

            runOnUiThread(() -> {
                if (questions.isEmpty()) {
                    Toast.makeText(this, "No questions available for this deck", Toast.LENGTH_SHORT).show();
                } else {
                    // Tampilkan hanya pertanyaan pertama
                    adapter.setData(Collections.singletonList(questions.get(currentQuestionIndex)));
                }
            });
        }).start();
    }

    private List<ItemQuestion> getDummyQuestions() {
        List<ItemQuestion> questions = new ArrayList<>();
        questions.add(new ItemQuestion("What is the capital of France?", "Paris", "London", "Berlin", "Madrid", "Paris"));
        questions.add(new ItemQuestion("What is 2 + 2?", "3", "4", "5", "6", "4"));
        questions.add(new ItemQuestion("Who wrote 'Hamlet'?", "Shakespeare", "Hemingway", "Tolkien", "Austen", "Shakespeare"));
        return questions;
    }

    private void onOptionSelected(ItemQuestion question, String selectedOption) {
        // Cek jawaban benar atau salah
        if (selectedOption.equals(question.getCorrectAnswer())) {
            score++;
        }

        // Pindah ke pertanyaan berikutnya
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            // Tampilkan pertanyaan berikutnya
            recyclerView.setAlpha(0); // Efek transparansi sebelum transisi
            recyclerView.animate().alpha(1).setDuration(300).start(); // Animasi masuk
            adapter.setData(Collections.singletonList(questions.get(currentQuestionIndex)));
        } else {
            // Jika tidak ada lagi pertanyaan, akhiri kuis
            finishQuiz();
        }
    }

    private void finishQuiz() {
        new Thread(() -> {
            // Hitung jumlah pertanyaan dan persentase nilai
            int totalQuestions = currentQuestionIndex ;
            int incorrectAnswers = totalQuestions - score;
            int percentage = (int) ((double) score / totalQuestions * 100);

            // Simpan data ke tabel ItemHistory
            String deckName = getIntent().getStringExtra("deckName");
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String scoreText = percentage + "%";

            ItemHistory history = new ItemHistory(deckName, currentDate, scoreText, score, incorrectAnswers);
            DaoHistory historyDao = database.itemHistoryDao();
            historyDao.insert(history);

            // Pindah ke HistoryMainActivity
            runOnUiThread(() -> {
                Intent intent = new Intent(QuestionMainActivity.this, HistoryHasilQuiz.class);
                intent.putExtra("history", history);
                startActivity(intent);
                finish();
            });
        }).start();
    }
}