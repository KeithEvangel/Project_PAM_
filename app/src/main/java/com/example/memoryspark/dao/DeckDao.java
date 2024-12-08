package com.example.memoryspark.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.memoryspark.item.Deck;

import java.util.List;

@Dao
public interface DeckDao {

    @Insert
    void insertDeck(Deck deck);

    @Update
    void updateDeck(Deck deck);

    @Delete
    void deleteDeck(Deck deck);

    @Query("SELECT * FROM decks")
    List<Deck> getAllDecks();

    @Query("SELECT * FROM decks WHERE id = :deckId LIMIT 1")
    Deck getDeckById(int deckId);
}
