package com.example.howareu.databases.repository;

import android.app.Application;

import com.example.howareu.databases.HowAreYouDatabase;
import com.example.howareu.databases.MoodDatabase;
import com.example.howareu.databases.dao.MoodDao;
import com.example.howareu.model.Mood;

public class MoodRepository {
    private MoodDao moodDao;

    public MoodRepository(Application application) {
        HowAreYouDatabase database = HowAreYouDatabase.getInstance(application);
        moodDao = database.moodDao();
    }
    public void insertMood(Mood mood){
        moodDao.insert(mood);
    }
    public int getMoodPercentage(int moodId){
        return moodDao.getMoodPercent(moodId);
    }

    public String getMoodName(int moodId){
        return moodDao.getMoodName(moodId);
    }
    public boolean isMoodTableEmpty(){
        return moodDao.isMoodTableEmpty();
    }
}
