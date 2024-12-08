package com.example.memoryspark.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.memoryspark.item.ItemHistory;

import java.util.List;

@Dao
public interface DaoHistory {
    @Insert
    void insert(ItemHistory item);

    @Update
    void update(ItemHistory item);

    @Delete
    void delete(ItemHistory item);

    @Query("SELECT * FROM item_history ORDER BY id DESC")
    List<ItemHistory> getAllHistory();
}