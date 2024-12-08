package com.example.memoryspark.item;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "cards",
        foreignKeys = @ForeignKey(
                entity = Deck.class,
                parentColumns = "id",
                childColumns = "deckId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index(value = "deckId") // Adding index on deckId
)
public class Card {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String question;
    private String answer;
    private int deckId; // Foreign key referencing Deck

    // Constructors
    public Card(String question, String answer, int deckId) {
        this.question = question;
        this.answer = answer;
        this.deckId = deckId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) { // Room sets the ID
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }
}
