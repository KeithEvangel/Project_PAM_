package com.example.memoryspark.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoryspark.item.ItemQuestion;
import com.example.memoryspark.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterQuestion extends RecyclerView.Adapter<AdapterQuestion.QuestionViewHolder> {

    private List<ItemQuestion> questionList = new ArrayList<>();
    private final OnOptionClickListener listener;

    public AdapterQuestion(OnOptionClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        ItemQuestion question = questionList.get(position);
        holder.bind(question, listener);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public void setData(List<ItemQuestion> questions) {
        this.questionList = questions;
        notifyDataSetChanged();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView questionText, option1, option2, option3, option4;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.textQuestion);
            option1 = itemView.findViewById(R.id.textOption1);
            option2 = itemView.findViewById(R.id.textOption2);
            option3 = itemView.findViewById(R.id.textOption3);
            option4 = itemView.findViewById(R.id.textOption4);
        }

        public void bind(ItemQuestion question, OnOptionClickListener listener) {
            questionText.setText(question.getQuestion());
            option1.setText(question.getOption1());
            option2.setText(question.getOption2());
            option3.setText(question.getOption3());
            option4.setText(question.getOption4());

            // Set listener untuk opsi
            View.OnClickListener clickListener = v -> {
                String selectedOption = ((TextView) v).getText().toString();
                listener.onOptionClick(question, selectedOption);
            };
            option1.setOnClickListener(clickListener);
            option2.setOnClickListener(clickListener);
            option3.setOnClickListener(clickListener);
            option4.setOnClickListener(clickListener);
        }
    }

    // Interface untuk listener opsi
    public interface OnOptionClickListener {
        void onOptionClick(ItemQuestion question, String selectedOption);
    }
}