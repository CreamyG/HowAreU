package com.example.howareu.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.howareu.R;
import com.example.howareu.constant.Strings;

import java.util.Calendar;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000; // 3 seconds
    private long lastLog;
    private long currentTime;
    private SharedPreferences mPrefs;
    private boolean isLogged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mPrefs= getSharedPreferences(Strings.START_PREF_NAME, Context.MODE_PRIVATE);
        currentTime = System.currentTimeMillis();
        lastLog = mPrefs.getLong(Strings.LAST_LOGIN_TIME, 0);
        isLogged= mPrefs.getBoolean(Strings.IS_LOGGED,false);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(lastLog);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(currentTime);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = null;
                boolean fromLogout = mPrefs.getBoolean(Strings.FROM_LOGOUT,false);
                if(isSameDay(cal,cal2)){


                    if(fromLogout){
                        i= new Intent(SplashScreenActivity.this, LoggedOutActivity.class);
                    }
                    else if(!isLogged){
                        i= new Intent(SplashScreenActivity.this, SetUserActivity.class);

                    }
                    else{
                        i= new Intent(SplashScreenActivity.this, MainMenuActivity.class);
                    }



                }
                else{

                    i= new Intent(SplashScreenActivity.this, InspirationalQuotesActivity.class);

                }
                mPrefs.edit().putLong(Strings.LAST_LOGIN_TIME, currentTime).apply();

                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private boolean isSameDay(Calendar cal1, Calendar cal2) {


        // compare the year, month, and day of the two timestamps
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
}