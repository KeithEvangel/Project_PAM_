package com.example.memoryspark.quiz;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memoryspark.R;

public class QuestionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity_detail);

        int score = getIntent().getIntExtra("score", 0);

        TextView textScore = findViewById(R.id.textQuestionDetail);
        textScore.setText("Your score: " + score);
    }
}