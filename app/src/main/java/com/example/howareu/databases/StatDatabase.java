package com.example.howareu.databases;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.howareu.databases.dao.DateConverter;
import com.example.howareu.databases.dao.MoodDao;
import com.example.howareu.databases.dao.StatDao;
import com.example.howareu.model.Mood;
import com.example.howareu.model.Stat;

@Database(entities = {Stat.class, Mood.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class StatDatabase extends RoomDatabase {
    private static StatDatabase DatabaseInstance;

    public abstract StatDao statDao();
    public abstract MoodDao moodDao();

    public static synchronized StatDatabase getInstance(Application context) {
        if (DatabaseInstance == null) {
            DatabaseInstance =
                    Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    StatDatabase.class,
                                    "stat_database")
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return DatabaseInstance;
    }

}
