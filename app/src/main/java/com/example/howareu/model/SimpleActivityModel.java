package com.example.howareu.model;

public class SimpleActivityModel {
    public String activityName;
    public int moodrate;

    public SimpleActivityModel(String activityName, int moodrate) {
        this.activityName = activityName;
        this.moodrate = moodrate;
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
}
