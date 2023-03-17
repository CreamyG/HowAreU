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
public interface JournalDao {
    @Insert
    void insert(Journal journal);

    @Update
    void update(Journal journal);

    @Delete
    void delete(Journal journal);

    @Query("SELECT * FROM journal")
    LiveData<List<Journal>> getAllJournal();

    @Query("SELECT content FROM journal WHERE id = :journalId")
    String getContentById(int journalId);

    @Query("SELECT content FROM journal WHERE date = :date")
    String getContentByDate(Date date);

    @Query("SELECT date FROM journal WHERE id = :activityId")
    Date getDateById(int activityId);

    @Query("SELECT is_private FROM journal WHERE date = :date")
    boolean getPrivacyByDate(Date date);

    @Query("SELECT is_private FROM journal WHERE id = :journalId")
    boolean getPrivacyById(int journalId);


}
