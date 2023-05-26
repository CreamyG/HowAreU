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
import com.example.howareu.model.User;
import com.example.howareu.model.UserLocal;

import java.util.regex.Pattern;

public class SetUserActivity extends AppCompatActivity {
    EditText editTextName, editTextPasscode, editTextPasscodeConfirm;
    TextView errorText;
    Button btnLogInUser;
    UserLocalRepository userLocalDB;
    private SharedPreferences mPrefs;
    private SharedPreferences mPrefs2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user);
        mPrefs = getSharedPreferences(Strings.PREF_NAME, Context.MODE_PRIVATE);
        mPrefs2 = getSharedPreferences(Strings.START_PREF_NAME, Context.MODE_PRIVATE);
        editTextName = findViewById(R.id.editTextName);
        editTextPasscode = findViewById(R.id.editTextPasscode);
        editTextPasscodeConfirm = findViewById(R.id.editTextPasscodeConfirm);
        btnLogInUser = findViewById(R.id.btnLogInUser);
        errorText= findViewById(R.id.errorText);
        userLocalDB = new UserLocalRepository(getApplication());


        btnLogInUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasError= false;
                String name = editTextName.getText().toString();
                String pass = editTextPasscode.getText().toString();
                String pass2 = editTextPasscodeConfirm.getText().toString();
                String errorMessage = "";



                if(name.isEmpty() ||pass.isEmpty() || pass2.isEmpty()){
                    errorMessage="Check Empty Fields ";
                    hasError=true;
                }
                if(!pass.equals(pass2)){
                    if(errorMessage.isEmpty()){
                        errorMessage="PassCode Not the same";
                    }
                    else{
                        errorMessage="Check Fields";
                    }
                    hasError=true;
                }

                if (pass.length()<8 ) {
                    errorMessage="PassCode size must be atleast 8 ";
                    hasError=true;
                }
                String regex = "^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+$";
                Pattern pattern = Pattern.compile(regex);
                if( pattern.matcher(pass).find()){
                    if (!errorMessage.isEmpty()) {
                        errorMessage += "\n";
                    }
                    errorMessage+="PassCode  must contain atleast 1 special Character";
                    hasError=true;
                }

                if(!hasError){
                    saveUser(name,pass);
                }
                else{
                    errorText.setText(errorMessage);
                    errorText.setVisibility(View.VISIBLE);
                }

            }
        });

    }
    public void saveUser(String name, String pass){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                UserLocal userModel = new UserLocal(name,pass);
                userLocalDB.insertUser(userModel);
                // Update UI with results on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(userModel);
                    }
                });
                return null;
            }
        }.execute();
    }
    public void updateUI(UserLocal user){
        if (user != null) {
            mPrefs2.edit().putBoolean(Strings.IS_LOGGED,true).apply();
            mPrefs.edit().putString(Strings.PASSCODE,user.getPasscode()).apply();
            Intent intent = new Intent(SetUserActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        } else {
            // User is signed out
        }

    }


}