package com.example.howareu.databases;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.howareu.databases.dao.ActivityDao;
import com.example.howareu.databases.dao.DateConverter;
import com.example.howareu.databases.dao.JournalDao;
import com.example.howareu.model.Activity;
import com.example.howareu.model.Journal;

@Database(entities = {Journal.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class JournalDatabase extends RoomDatabase {
    private static JournalDatabase DatabaseInstance;

    public abstract JournalDao journalDao();

    public static synchronized JournalDatabase getInstance(Application context) {
        if (DatabaseInstance == null) {
            DatabaseInstance =
                    Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    JournalDatabase.class,
                                    "journal_database")
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return DatabaseInstance;
    }
}
