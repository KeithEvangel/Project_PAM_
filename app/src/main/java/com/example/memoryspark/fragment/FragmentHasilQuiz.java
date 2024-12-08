package com.example.memoryspark.fragment;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.memoryspark.item.ItemHistory;
import com.example.memoryspark.R;

public class FragmentHasilQuiz extends Fragment {

    private TextView tvBenar, tvSalah;
    private ItemHistory history;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hasil_quiz, container, false);

        tvBenar = view.findViewById(R.id.tvBenar);
        tvSalah = view.findViewById(R.id.tvSalah);

        if (getArguments() != null) {
            history = (ItemHistory) getArguments().getSerializable("history");
            if (history != null) {
                tvBenar.setText(String.valueOf(history.getJumlahBenar()));
                tvSalah.setText(String.valueOf(history.getJumlahSalah()));
            }
        }

        return view;
    }
}
