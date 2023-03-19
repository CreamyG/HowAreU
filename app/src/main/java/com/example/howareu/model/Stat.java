package com.example.howareu.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "stat")
public class Stat {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "date")
    private Date date;
    @ColumnInfo(name = "mood_percent")
    private int mood_percent;
    @ColumnInfo(name = "mood_id")
    private int mood_id;

    public Stat(int mood_percent, int mood_id) {
        this.date =  date == null ? new Date() : date;
        this.mood_percent = mood_percent;
        this.mood_id = mood_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMood_percent() {
        return mood_percent;
    }

    public void setMood_percent(int mood_percent) {
        this.mood_percent = mood_percent;
    }

    public int getMood_id() {
        return mood_id;
    }

    public void setMood_id(int mood_id) {
        this.mood_id = mood_id;
    }
}
