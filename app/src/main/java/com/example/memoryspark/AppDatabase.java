package com.example.memoryspark;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.memoryspark.item.Card;
import com.example.memoryspark.dao.CardDao;
import com.example.memoryspark.item.Deck;
import com.example.memoryspark.dao.DeckDao;
import com.example.memoryspark.dao.DaoHistory;
import com.example.memoryspark.item.ItemHistory;
import com.example.memoryspark.item.ItemQuestion;
import com.example.memoryspark.dao.DaoQuestion;

@Database(entities = {Deck.class, Card.class, ItemHistory.class, ItemQuestion.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract DeckDao deckDao();
    public abstract CardDao cardDao();

    // Singleton pattern to prevent having multiple instances of the database opened at the same time.
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "question_database")
                    .fallbackToDestructiveMigration() // Untuk menangani perubahan versi
                    .build();
        }
        return instance;
    }


    public abstract DaoHistory itemHistoryDao();

    public abstract DaoQuestion questionDao();
}
