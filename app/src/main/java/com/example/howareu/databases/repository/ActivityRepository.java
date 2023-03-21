package com.example.howareu.databases.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.howareu.databases.HowAreYouDatabase;
import com.example.howareu.databases.dao.ActivityDao;
import com.example.howareu.model.Activity;

import java.util.Date;
import java.util.List;


public class ActivityRepository {
    private ActivityDao activityDao;

    public ActivityRepository(Application application) {
        HowAreYouDatabase database = HowAreYouDatabase.getInstance(application);
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
    public  LiveData<List<Activity>> getAllActivity(){return activityDao.getAllActivity();}
    public  List<Activity>  getActivityByDate(String day,String month, String year){return activityDao.getActivityByDate(day,month,year);};
}
