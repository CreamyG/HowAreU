package com.example.howareu.databases;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.howareu.databases.dao.MoodDao;
import com.example.howareu.model.Mood;

@Database(entities = {Mood.class}, version = 1)
public abstract class  MoodDatabase extends RoomDatabase {

    private static MoodDatabase DatabaseInstance;

    public abstract MoodDao moodDao();

    public static synchronized MoodDatabase getInstance(Application context) {
        if (DatabaseInstance == null) {
            DatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                           MoodDatabase.class, "mood_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return DatabaseInstance;
    }


}
