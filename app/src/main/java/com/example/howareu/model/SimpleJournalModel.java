package com.example.howareu.model;

import androidx.room.ColumnInfo;

import java.util.Date;

public class SimpleJournalModel {
    private String date;
    private String content;
    private boolean isPrivate;

    public SimpleJournalModel(String date, String content, boolean isPrivate) {
        this.date = date;
        this.content = content;
        this.isPrivate = isPrivate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
}

