package com.example.howareu.databases.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.howareu.databases.ActivityDatabase;
import com.example.howareu.databases.JournalDatabase;
import com.example.howareu.databases.dao.JournalDao;
import com.example.howareu.model.Activity;
import com.example.howareu.model.Journal;

import java.util.Date;
import java.util.List;

public class JournalRepository {
    private JournalDao journalDao;

    public JournalRepository(Application application) {
        JournalDatabase database = JournalDatabase.getInstance(application);
        journalDao = database.journalDao();
    }
    public void insertJournal(Journal journal){journalDao.insert(journal);}
    public String getContentById(int journalId){
        return journalDao.getContentById(journalId);
    }
    public String getContentByDate(Date date){return journalDao.getContentByDate(date);}
    public Date getDateById(int activityId){
        return journalDao.getDateById(activityId);
    }
    public boolean getPrivacyByDate(Date date){return journalDao.getPrivacyByDate(date);}
    public boolean getPrivacyById(int journalId){return journalDao.getPrivacyById(journalId);}

    public LiveData<List<Journal>> getAllJournal(){return journalDao.getAllJournal();}
}
