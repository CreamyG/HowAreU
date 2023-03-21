package com.example.howareu.databases.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.howareu.model.Activity;

import com.example.howareu.model.Stat;
import com.example.howareu.model.StatDateAndMoodId;

import com.example.howareu.model.Mood;

import java.util.Date;
import java.util.List;

@Dao
public interface StatDao {
    @Insert
    void insert(Stat stat);

    @Update
    void update(Stat stat);

    @Delete
    void delete(Stat stat);

    @Query("SELECT * FROM stat")
    LiveData<List<Stat>> getAllStat();

    @Query("SELECT mood_id FROM stat WHERE id = :statId")
    int getMoodIdById(int statId);

    @Query("SELECT mood_percent FROM stat WHERE id = :statId")
    int getMoodPercentById(int statId);

    @Query("SELECT date FROM stat WHERE id = :statId")
    Date getStatDatebyId(int statId);


    @Query("SELECT COUNT(*) FROM stat WHERE mood_id = :mood_id AND strftime('%m', datetime(date/1000, 'unixepoch')) = :month  AND strftime('%Y', datetime(date/1000, 'unixepoch')) = :year")
    int getMoodCount(int mood_id, String month, String year);

    @Query("SELECT AVG(mood_percent) FROM stat WHERE strftime('%m', datetime(date/1000, 'unixepoch')) = :month  AND strftime('%Y', datetime(date/1000, 'unixepoch')) = :year")
    int getMoodMonthAvg(String month,String year);


    @Query("SELECT date,mood_id, mood.name FROM stat JOIN mood on stat.mood_id = mood.id WHERE strftime('%m', datetime(date/1000, 'unixepoch')) = :month  AND strftime('%Y', datetime(date/1000, 'unixepoch')) = :year ORDER BY date  ASC")
    LiveData<List<StatDateAndMoodId>>  getMoodIdAndDate(String month,String year);

}
