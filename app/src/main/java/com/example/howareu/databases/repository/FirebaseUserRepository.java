package com.example.howareu.databases.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.howareu.constant.Strings;
import com.example.howareu.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUserRepository {
    private DatabaseReference usersRef;

    public FirebaseUserRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance(Strings.FIREBASE_LINK_DB);
        usersRef = database.getReference("users");
    }

    public LiveData<List<User>> getUsers() {
        MutableLiveData<List<User>> data = new MutableLiveData<>();
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        users.add(user);
                    }
                }
                data.setValue(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                data.setValue(null);
            }
        });
        return data;
    }

    public void addUser(User user) {
        String id = usersRef.push().getKey();
        if (id != null) {
            user.setId(id);
            usersRef.child(id).setValue(user);
        }
    }

    public void updateUser(User user) {
        usersRef.child(user.getId()).setValue(user);
    }

    public void deleteUser(User user) {
        usersRef.child(user.getId()).removeValue();
    }
}