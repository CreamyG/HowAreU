package com.example.howareu.databases.repository;
import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.howareu.databases.HowAreYouDatabase;
import com.example.howareu.databases.UserDatabase;
import com.example.howareu.databases.dao.UserDao;
import com.example.howareu.model.User;
import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private FirebaseUserRepository firebaseUserRepository;

    public UserRepository(Application application) {
        HowAreYouDatabase database = HowAreYouDatabase.getInstance(application);
        userDao = database.userDao();
        firebaseUserRepository = new FirebaseUserRepository();
    }

    public LiveData<List<User>> getUsers() {
        return userDao.getAllUsers();
    }

    public void addUser(User user, boolean hasInternet)
    {
        if(hasInternet) {
            firebaseUserRepository.addUser(user);
        }
        userDao.insert(user);
    }

    public void updateUser(User user, boolean hasInternet) {
        firebaseUserRepository.updateUser(user);
    }

    public void deleteUser(User user, boolean hasInternet) {
        firebaseUserRepository.deleteUser(user);
    }
    public String getUsernameFromUser(String id){
        String getUsernameFromUser=null;
        if(id!=null) {
            getUsernameFromUser= userDao.getUsersUsername(id);
        }
        return getUsernameFromUser;
    }

    public String getIdFromUser(String email){
        String getUsersIdUsingEmail = null;
        if(email!=null) {
            return userDao.getUsersIdUsingEmail(email);
        }
        return getUsersIdUsingEmail;
    }

}
