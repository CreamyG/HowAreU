package com.example.howareu.databases;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.howareu.databases.dao.ActivityDao;
import com.example.howareu.databases.dao.DateConverter;
import com.example.howareu.databases.dao.UserDao;
import com.example.howareu.model.Activity;
import com.example.howareu.model.User;

@Database(entities = {Activity.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class ActivityDatabase extends RoomDatabase {

    private static ActivityDatabase DatabaseInstance;

    public abstract ActivityDao activityDao();

    public static synchronized ActivityDatabase getInstance(Application context) {
        if (DatabaseInstance == null) {
            DatabaseInstance =
                    Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    ActivityDatabase.class,
                                    "activity_database")
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return DatabaseInstance;
    }


}
