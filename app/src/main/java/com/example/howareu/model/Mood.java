package com.example.howareu.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mood")
public class Mood {
    @PrimaryKey @NonNull
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "percent")
    private int percent;

    public Mood(int id, String name, int percent) {
        this.id = id;
        this.name = name;
        this.percent = percent;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}