package com.example.howareu.databases.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.howareu.model.Activity;
import com.example.howareu.model.Journal;


import java.util.Date;
import java.util.List;

@Dao
public interface ActivityDao {
    @Insert
    void insert(Activity activity);

    @Update
    void update(Activity activity);

    @Delete
    void delete(Activity activity);

    @Query("SELECT * FROM activities")
    LiveData<List<Activity>> getAllActivity();

    @Query("SELECT mood_id FROM activities WHERE id = :activityId")
    int getMoodID(int activityId);

    @Query("SELECT content FROM activities WHERE id = :activityId")
    String getActivityName(int activityId);

    @Query("SELECT date FROM activities WHERE id = :activityId")
    Date getActivityDate(int activityId);



    @Query("SELECT * FROM activities WHERE  strftime('%m', datetime(date/1000, 'unixepoch')) = :month AND strftime('%d', datetime(date/1000, 'day')) = :year AND strftime('%Y', datetime(date/1000, 'unixepoch')) = :year ORDER BY date  ASC")
    LiveData<List<Activity>>  getActivityByDate(String day,String month, String year);



}
