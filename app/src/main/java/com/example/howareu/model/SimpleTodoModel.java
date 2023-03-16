package com.example.howareu.model;

public class SimpleTodoModel {
    public String todoName;
    public int moodrate;

    public SimpleTodoModel(String todoName, int moodrate) {
        this.todoName = todoName;
        this.moodrate = moodrate;
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
}
