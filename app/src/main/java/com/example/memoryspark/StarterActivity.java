package com.example.memoryspark;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memoryspark.add.MainActivity;
import com.example.memoryspark.databinding.ActivityStarterBinding;
import com.example.memoryspark.history.HistoryMainActivity;
import com.example.memoryspark.quiz.QuizDeckList;

public class StarterActivity extends AppCompatActivity {

    private ActivityStarterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityStarterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListeners();
    }

    private void setListeners() {
        binding.btnOpenDeck.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        binding.btnStartTest.setOnClickListener(v -> {
            // Start Test button navigates to QuizDeckList
            Intent intent = new Intent(this, QuizDeckList.class);
            startActivity(intent);
        });

        binding.btnHistoryTest.setOnClickListener(v -> {
            // Mengarahkan ke HistoryMainActivity
            Intent intent = new Intent(this, HistoryMainActivity.class);
            startActivity(intent);
        });
    }
}