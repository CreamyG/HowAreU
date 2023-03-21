package com.example.howareu.databases.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.howareu.databases.ActivityDatabase;
import com.example.howareu.databases.HowAreYouDatabase;
import com.example.howareu.databases.JournalDatabase;
import com.example.howareu.databases.dao.JournalDao;
import com.example.howareu.model.Activity;
import com.example.howareu.model.Journal;

import java.util.Date;
import java.util.List;

public class JournalRepository {
    private JournalDao journalDao;

    public JournalRepository(Application application) {
        HowAreYouDatabase database = HowAreYouDatabase.getInstance(application);
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

    public LiveData<List<Journal>> getJournalByDate(String month,String year){return journalDao.getJournalByDate(month,year);}
    public LiveData<List<Journal>> getJournalByDateDesc(String month,String year){return journalDao.getJournalByDateDesc(month,year);}
    public LiveData<List<Journal>> getJournalBySearch(String month,String year,String search){return journalDao.getJournalBySearch(month,year,search);}
    public LiveData<List<Journal>> getJournalBySearchDesc(String month,String year,String search){return journalDao.getJournalBySearchDesc(month,year,search);}

    public List<Journal> getJournalByWholeDate(String day,String month,String year){return journalDao.getJournalByWholeDate(day,month,year);}

}
