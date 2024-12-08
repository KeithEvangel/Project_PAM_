package com.example.memoryspark.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.memoryspark.item.Card;

import java.util.List;

@Dao
public interface CardDao {

    @Insert
    void insertCard(Card card);

    @Update
    void updateCard(Card card);

    @Delete
    void deleteCard(Card card);

    @Query("SELECT * FROM cards WHERE deckId = :deckId")
    List<Card> getCardsForDeck(int deckId);

    @Query("SELECT * FROM cards WHERE id = :cardId LIMIT 1")
    Card getCardById(int cardId);
}
