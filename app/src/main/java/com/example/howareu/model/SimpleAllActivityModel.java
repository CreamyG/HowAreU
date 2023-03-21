package com.example.howareu.model;

import java.util.Date;

public class SimpleAllActivityModel {
    public Date date;
    public String activityName;
    public int moodrate;
    public int type;

    public SimpleAllActivityModel(Date date, String activityName, int moodrate, int type) {
        this.date = date;
        this.activityName = activityName;
        this.moodrate = moodrate;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getMoodrate() {
        return moodrate;
    }

    public void setMoodrate(int moodrate) {
        this.moodrate = moodrate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
