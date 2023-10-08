package com.example.howareu.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.howareu.NotificationReceiver;
import com.example.howareu.R;
import com.example.howareu.activity.Modules.HomeActivity;
import com.example.howareu.activity.Modules.JournalActivity;
import com.example.howareu.activity.Modules.StatisticsActivity;
import com.example.howareu.activity.fragments.HomeFragment;
import com.example.howareu.activity.fragments.JournalHistoryFragment;
import com.example.howareu.activity.fragments.StatisticsFragment;
import com.example.howareu.constant.Integers;
import com.example.howareu.constant.Strings;
import com.example.howareu.databases.repository.MoodRepository;
import com.example.howareu.databases.repository.UserRepository;
import com.example.howareu.model.Mood;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainMenuActivity extends AppCompatActivity {
    private TextView nameTextView;
    private UserRepository userRep;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;

    private byte[] byteArray;



    //March 15 Added
    private MoodRepository moodDb;
    private Fragment fragmentHome;
    private Fragment fragmentStat;
    private Fragment fragmentJournal;

    TextView textMonth,textDate;


    private SharedPreferences mPrefs2;

    private long currentTime;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);


        NotificationReceiver receiver = new NotificationReceiver();
        receiver.setBroadcast(this);




        mAuth = FirebaseAuth.getInstance();
        byteArray = getIntent().getByteArrayExtra("myBitmap");
        moodDb = new MoodRepository(getApplication());
        mPrefs2 = getSharedPreferences(Strings.START_PREF_NAME, Context.MODE_PRIVATE);
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                if(moodDb.isMoodTableEmpty()) {
                    setMoodDb();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                // Do something with the result
            }
        }.execute();




        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");


        fragmentHome = new HomeFragment();
        fragmentStat = new StatisticsFragment();
        fragmentJournal = new JournalHistoryFragment();




        LinearLayout homeLayout = findViewById(R.id.home);
        homeLayout.setOnClickListener(view -> {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        });

        LinearLayout statsLayout = findViewById(R.id.statistics);
        statsLayout.setOnClickListener(view -> {
            Intent i = new Intent(this, StatisticsActivity.class);
            startActivity(i);
        });

        LinearLayout journalLayout = findViewById(R.id.journalHistory);
        journalLayout.setOnClickListener(view1 -> {
            Intent i = new Intent(this, JournalActivity.class);
            startActivity(i);
        });

        //Date
        View includedLayout = findViewById(R.id.date_for_fragment_home_id);
        textMonth = includedLayout.findViewById(R.id.textMonth);
        textDate = includedLayout.findViewById(R.id.textDate);
        currentTime = System.currentTimeMillis();
        setDateForTopPart();
    }


    private void setDateForTopPart() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentTime);
        // Get the month name
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        String monthName = monthFormat.format(cal.getTime());
        if(monthName.length()>4){
            monthName = monthName.substring(0,3)+".";
        }
        textMonth.setText(monthName);
        textDate.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
    }



    //For main menu lagout
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        new MenuInflater(this).inflate(R.menu.toolbar_main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FirebaseAuth auth= FirebaseAuth.getInstance();
        int id = item.getItemId();
        switch (id){
            case R.id.about_us:
                Intent i =  new Intent(MainMenuActivity.this, AboutActivity.class);
                startActivity(i);
                break;
            case R.id.lagout:
                auth.signOut();
                mPrefs2.edit().putBoolean(Strings.IS_LOGGED,false).apply();
                mPrefs2.edit().putBoolean(Strings.FROM_LOGOUT,true).apply();
                startActivity(new Intent(this, LoggedOutActivity.class));

                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void setName(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                String email = mAuth.getCurrentUser().getEmail();
                nameTextView = findViewById(R.id.nameTextView);
                userRep = new UserRepository(getApplication());
                String id = userRep.getIdFromUser(email);
                String username = userRep.getUsernameFromUser(id);

                // Update UI with results on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(username!=null){
                            nameTextView.setText(username);
                        }
                        else{
                            nameTextView.setText("Name ERror");
                        }
                    }
                });
                return null;
            }
        }.execute();
    }

    public void setMoodDb(){
        Mood moodVerySad = new Mood(Integers.MOOD_VERY_SAD, Strings.MOOD_VERY_SAD, Integers.MOOD_PERCENT_VERY_SAD);
        Mood moodSad = new Mood(Integers.MOOD_SAD, Strings.MOOD_SAD, Integers.MOOD_PERCENT_SAD);
        Mood moodNeutral = new Mood(Integers.MOOD_NEUTRAL, Strings.MOOD_NEUTRAL, Integers.MOOD_PERCENT_NEUTRAL);
        Mood moodHappy = new Mood(Integers.MOOD_HAPPY, Strings.MOOD_HAPPY, Integers.MOOD_PERCENT_HAPPY);
        Mood moodVeryHappy = new Mood(Integers.MOOD_VERY_HAPPY, Strings.MOOD_VERY_HAPPY, Integers.MOOD_PERCENT_VERY_HAPPY);


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                moodDb.insertMood(moodVerySad);
                moodDb.insertMood(moodSad);
                moodDb.insertMood(moodNeutral);
                moodDb.insertMood(moodHappy);
                moodDb.insertMood(moodVeryHappy);

                // Update UI with results on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                return null;
            }
        }.execute();
    }






}