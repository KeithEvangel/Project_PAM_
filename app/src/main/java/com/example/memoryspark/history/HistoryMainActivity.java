package com.example.memoryspark.history;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoryspark.AppDatabase;
import com.example.memoryspark.dao.DaoHistory;
import com.example.memoryspark.item.ItemHistory;
import com.example.memoryspark.R;
import com.example.memoryspark.adapter.AdapterHistory;

import java.util.ArrayList;
import java.util.List;

public class HistoryMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterHistory adapter;
    private List<ItemHistory> historyList = new ArrayList<>();
    private AppDatabase database; // Room Database
    private DaoHistory historyDao;
    private ImageButton btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_history); // ID sesuai XML

        recyclerView = findViewById(R.id.recyclerView); // ID sesuai XML
        btBack = findViewById(R.id.btBack); // Tombol kembali sesuai XML

        // Inisialisasi Database
        database = AppDatabase.getInstance(this);
        historyDao = database.itemHistoryDao();

        // Konfigurasi RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterHistory(historyList, this::onHistoryItemClick);
        recyclerView.setAdapter(adapter);

        // Muat data riwayat dari database
        loadHistoryData();

        // Tombol kembali
        btBack.setOnClickListener(v -> finish());
    }

    private void loadHistoryData() {
        new Thread(() -> {
            historyList.clear();
            historyList.addAll(historyDao.getAllHistory()); // Ambil data dari Room
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        }).start();
    }

    private void onHistoryItemClick(ItemHistory history) {
        // Pindah ke layar detail hasil kuis
        Intent intent = new Intent(this, HistoryHasilQuiz.class);
        intent.putExtra("history", history);
        startActivity(intent);
    }

}