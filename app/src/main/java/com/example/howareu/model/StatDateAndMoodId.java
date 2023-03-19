package com.example.howareu.model;


import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "date_mood_id")
public class StatDateAndMoodId {


    @ColumnInfo(name = "date")
    private Date date;
    @ColumnInfo(name = "mood_id")
    private int mood_id;
    @ColumnInfo(name = "name")
    private String mood_name;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMood_id() {
        return mood_id;
    }

    public void setMood_id(int mood_id) {
        this.mood_id = mood_id;
    }

    public String getMood_name() {
        return mood_name;
    }

    public void setMood_name(String mood_name) {
        this.mood_name = mood_name;
    }
}
