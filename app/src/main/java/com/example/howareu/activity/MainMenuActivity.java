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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.howareu.R;
import com.example.howareu.activity.fragments.HomeFragment;
import com.example.howareu.activity.fragments.JournalHistoryFragment;
import com.example.howareu.activity.fragments.StatisticsFragment;
import com.example.howareu.constant.Integers;
import com.example.howareu.constant.Strings;
import com.example.howareu.databases.repository.MoodRepository;
import com.example.howareu.databases.repository.UserRepository;
import com.example.howareu.model.Mood;
import com.google.firebase.auth.FirebaseAuth;

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


    private SharedPreferences mPrefs2;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main_menu);
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


        //setName();


        ImageView homeImageView = findViewById(R.id.home);
        Bundle args = new Bundle();

        //Transfer Image(For future use if ever to transfer Data)
        //args.putByteArray("byteArray",byteArray);
        //fragmentHome.setArguments(args);



        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentHome)
                .commit();
        homeImageView.setOnClickListener(view -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentHome)
                    .commit();
        });

        ImageView statisticImageView = findViewById(R.id.statistics);
        statisticImageView.setOnClickListener(view -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentStat)
                    .commit();
        });

        ImageView journalImageView = findViewById(R.id.journalHistory);
        journalImageView.setOnClickListener(view1 -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentJournal)
                    .commit();
        });

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