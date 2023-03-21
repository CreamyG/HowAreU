package com.example.howareu.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "activities")
public class Activity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "date")
    private Date date;
    @ColumnInfo(name = "content")
    private String content;
    @ColumnInfo(name = "mood_id")
    private int mood_id;
    @ColumnInfo(name = "type")
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Activity(String content, int mood_id, int type) {
        this.date =  date == null ? new Date() : date;
        this.content = content;
        this.mood_id = mood_id;
        this.type = type;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMood_id() {
        return mood_id;
    }

    public void setMood_id(int mood_id) {
        this.mood_id = mood_id;
    }
}

