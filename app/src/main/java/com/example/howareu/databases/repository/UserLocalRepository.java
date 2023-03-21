package com.example.howareu.databases.repository;

import android.app.Application;

import com.example.howareu.databases.HowAreYouDatabase;
import com.example.howareu.databases.dao.UserLocalDao;
import com.example.howareu.model.Journal;
import com.example.howareu.model.UserLocal;

public class UserLocalRepository {
    private UserLocalDao userLocal;

    public UserLocalRepository(Application application) {
        HowAreYouDatabase database = HowAreYouDatabase.getInstance(application);
        userLocal = database.userLocalDao();

    }

    public void insertUser(UserLocal user){userLocal.insert(user);}
}
