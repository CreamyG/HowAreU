package com.example.howareu.model;

public class SimpleTodoModel {
    public String todoName;
    public int moodrate;
    public boolean isEnabled;

    public SimpleTodoModel(String todoName, int moodrate, boolean isEnabled) {
        this.todoName = todoName;
        this.moodrate = moodrate;
        this.isEnabled = isEnabled;
    }
    public String getTodoName() {
        return todoName;
    }



    public void setTodoName(String todoName) {
        this.todoName = todoName;
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
