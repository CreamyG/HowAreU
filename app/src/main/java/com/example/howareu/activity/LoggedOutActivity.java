package com.example.howareu.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.howareu.R;
import com.example.howareu.constant.Strings;
import com.example.howareu.databases.repository.UserLocalRepository;
import com.example.howareu.model.UserLocal;

public class LoggedOutActivity extends AppCompatActivity {
    EditText editTextName, editTextPasscode;
    TextView errorText;
    Button btnLogInUser;
    UserLocalRepository userLocalDB;
    private SharedPreferences mPrefs;
    private SharedPreferences mPrefs2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_out);
        mPrefs = getSharedPreferences(Strings.PREF_NAME, Context.MODE_PRIVATE);
        mPrefs2 = getSharedPreferences(Strings.START_PREF_NAME, Context.MODE_PRIVATE);
        editTextName = findViewById(R.id.editTextName);
        editTextPasscode = findViewById(R.id.editTextPasscode);
        btnLogInUser = findViewById(R.id.btnLogInUser2);
        errorText= findViewById(R.id.errorText2);
        userLocalDB = new UserLocalRepository(getApplication());

        btnLogInUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String pass = editTextPasscode.getText().toString();
                if(name.isEmpty() ||pass.isEmpty()){
                    errorText.setText("Check Empty Fields ");
                    errorText.setVisibility(View.VISIBLE);
                }
                else{
                    saveUser(name,pass);
                }


            }
        });
    }
    public void saveUser(String name, String pass){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                UserLocal userModel = new UserLocal(name,pass);
                boolean exist = userLocalDB.doesUserExist(name,pass);
                // Update UI with results on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(exist){
                            updateUI(userModel);
                        }
                        else{
                            errorText.setText("Wrong username or passcode");
                            errorText.setVisibility(View.VISIBLE);
                        }
                    }
                });
                return null;
            }
        }.execute();
    }
    public void updateUI(UserLocal user){
        if (user != null) {
            mPrefs2.edit().putBoolean(Strings.IS_LOGGED,true).apply();
            mPrefs2.edit().putBoolean(Strings.FROM_LOGOUT,false).apply();
            Intent intent = new Intent(LoggedOutActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        } else {
            // User is signed out
        }

    }
}