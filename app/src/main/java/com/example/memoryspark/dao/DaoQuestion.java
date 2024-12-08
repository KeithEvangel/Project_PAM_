package com.example.memoryspark.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.memoryspark.item.ItemQuestion;

import java.util.List;

@Dao
public interface DaoQuestion {
    @Insert
    void insertQuestion(ItemQuestion question);

    @Insert
    void insertQuestions(List<ItemQuestion> questions);

    @Query("SELECT * FROM question_table")
    List<ItemQuestion> getAllQuestions();

    @Update
    void updateQuestion(ItemQuestion question);

    @Delete
    void deleteQuestion(ItemQuestion question);

    @Query("DELETE FROM question_table")
    void deleteAllQuestions();
}
