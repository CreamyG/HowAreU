package com.example.howareu.databases.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.howareu.databases.HowAreYouDatabase;
import com.example.howareu.databases.dao.StatDao;
import com.example.howareu.model.Stat;
import com.example.howareu.model.StatDateAndMoodId;

import java.util.List;

public class StatRepository {
    private StatDao statDao;

    public StatRepository(Application application) {
        HowAreYouDatabase database = HowAreYouDatabase.getInstance(application);
        statDao = database.statDao();
    }


    public void insertStat(Stat stat){statDao.insert(stat);}

    public int getMoodCount(int mood_id, String month, String year){return statDao.getMoodCount(mood_id,month,year);}


    public double getMoodMonthAvg(String month, String year){return statDao.getMoodMonthAvg(month,year);}

    public LiveData<List<StatDateAndMoodId>> getMoodIdAndDate(String month,String year){return statDao.getMoodIdAndDate(month,year);}

}
