package com.example.howareu.databases.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.howareu.databases.ActivityDatabase;
import com.example.howareu.databases.MoodDatabase;
import com.example.howareu.databases.dao.ActivityDao;
import com.example.howareu.databases.dao.MoodDao;
import com.example.howareu.model.Activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ActivityRepository {
    private ActivityDao activityDao;

    public ActivityRepository(Application application) {
        ActivityDatabase database = ActivityDatabase.getInstance(application);
        activityDao = database.activityDao();
    }


    public void insertActivity(Activity activity){activityDao.insert(activity);}
    public int geMoodId(int activityId){
        return activityDao.getMoodID(activityId);
    }
    public String getActivityname(int activityId){return activityDao.getActivityName(activityId);}
    public Date getDate(int activityId){
        return activityDao.getActivityDate(activityId);
    }
    public  LiveData<List<Activity>> getAllActivity(){return activityDao.getAllActivity();
    }
}
