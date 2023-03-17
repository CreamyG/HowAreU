package com.example.howareu.model;

public class SimpleActivityModel {
    public String activityName;
    public int moodrate;
    public boolean isEnabled;

    public SimpleActivityModel(String activityName, int moodrate, boolean isEnabled) {
        this.activityName = activityName;
        this.moodrate = moodrate;
        this.isEnabled = isEnabled;
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

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
