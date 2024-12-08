package com.example.memoryspark.history;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memoryspark.AppDatabase;
import com.example.memoryspark.StarterActivity;
import com.example.memoryspark.dao.DaoHistory;
import com.example.memoryspark.item.ItemHistory;
import com.example.memoryspark.R;
import com.example.memoryspark.fragment.FragmentHasilQuiz;

public class HistoryHasilQuiz extends AppCompatActivity {

    private ImageButton homeButton;
    private TextView tvNamaDeck, tvTanggal;
    private AppDatabase database; // Room Database
    private DaoHistory historyDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_hasil_quiz); // ID sesuai XML

        homeButton = findViewById(R.id.ibHome);
        tvNamaDeck = findViewById(R.id.tvNamaDeckHasil);
        tvTanggal = findViewById(R.id.textView2);

        // Inisialisasi Database
        database = AppDatabase.getInstance(this);
        historyDao = database.itemHistoryDao();

        // Ambil data dari intent
        ItemHistory history = (ItemHistory) getIntent().getSerializableExtra("history");

        if (history != null) {
            // Tampilkan data di TextView
            tvNamaDeck.setText("Deck: " + history.getDeckName());
            tvTanggal.setText(history.getTanggal());

            // Kirim data ke fragment
            Bundle bundle = new Bundle();
            bundle.putSerializable("history", history);

            FragmentHasilQuiz fragment = new FragmentHasilQuiz();
            fragment.setArguments(bundle);


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView,
                            fragment).commit();

        // Tombol kembali ke Home
            homeButton.setOnClickListener(v -> {
                Intent intent = new Intent(HistoryHasilQuiz.this, StarterActivity.class); // Arahkan ke home
                startActivity(intent);
                finish();
            });
        }
}}