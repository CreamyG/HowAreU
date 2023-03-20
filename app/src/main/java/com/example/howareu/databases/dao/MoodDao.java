package com.example.howareu.databases.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.howareu.model.Mood;

import java.util.List;

@Dao
public interface MoodDao {
    @Insert
    void insert(Mood mood);

    @Update
    void update(Mood mood);

    @Delete
    void delete(Mood mood);

    @Query("SELECT * FROM mood")
    LiveData<List<Mood>> getAllMood();

    @Query("SELECT percent FROM mood WHERE id = :moodId")
    int getMoodPercent(int moodId);

    @Query("SELECT name FROM mood WHERE id = :moodId")
    String getMoodName(int moodId);


    @Query("SELECT COUNT(*) == 0 FROM mood ")
    boolean isMoodTableEmpty();


    @Query("SELECT * FROM mood WHERE id = :moodId")
    LiveData<Mood> getMoodById(int moodId);

}
