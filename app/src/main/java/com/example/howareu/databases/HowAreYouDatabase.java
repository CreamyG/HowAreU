package com.example.howareu.databases;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.howareu.databases.dao.ActivityDao;
import com.example.howareu.databases.dao.DateConverter;
import com.example.howareu.databases.dao.JournalDao;
import com.example.howareu.databases.dao.MoodDao;
import com.example.howareu.databases.dao.StatDao;
import com.example.howareu.databases.dao.UserDao;
import com.example.howareu.model.Activity;
import com.example.howareu.model.Journal;
import com.example.howareu.model.Mood;
import com.example.howareu.model.Stat;
import com.example.howareu.model.StatDateAndMoodId;
import com.example.howareu.model.User;

@Database(entities = {Stat.class, Mood.class, Activity.class, Journal.class, User.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class HowAreYouDatabase extends RoomDatabase {
    private static HowAreYouDatabase DatabaseInstance;


    public abstract ActivityDao activityDao();
    public abstract JournalDao journalDao();
    public abstract MoodDao moodDao();
    public abstract StatDao statDao();
    public abstract UserDao userDao();

    public static synchronized HowAreYouDatabase getInstance(Application context) {
        if (DatabaseInstance == null) {
            DatabaseInstance =
                    Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    HowAreYouDatabase.class,
                                    "how_are_you_database")
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return DatabaseInstance;
    }

}
