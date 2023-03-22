package com.example.howareu.databases.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.howareu.model.UserLocal;

import java.util.List;
@Dao
public interface UserLocalDao {
    @Insert
    void insert(UserLocal userLocal);

    @Update
    void update(UserLocal userLocal);

    @Delete
    void delete(UserLocal userLocal);

    @Query("SELECT * FROM local_user")
    LiveData<List<UserLocal>> getAllUsers();

    @Query("SELECT name FROM local_user WHERE id = :userId")
    String getUsersUsername(String userId);

    @Query("SELECT passcode FROM local_user WHERE name = :name")
    String getUsersIdUsingEmail(String name);

    @Query("SELECT COUNT(*)  FROM local_user WHERE name = :name AND passcode=:passcode")
    Boolean doesUserExist(String name,String passcode);
}
