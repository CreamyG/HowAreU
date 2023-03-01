package com.example.howareu.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.howareu.R;
import com.example.howareu.databases.dao.UserDao;
import com.example.howareu.databases.repository.UserRepository;
import com.example.howareu.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class MainMenuActivity extends AppCompatActivity {
    private TextView nameTextView;
    private UserRepository userRep;
    private FirebaseAuth mAuth;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main_menu);
        setName();
        ImageView msImageView = findViewById(R.id.moodscale);

        msImageView.setOnClickListener(view -> {

    Intent intent = new Intent(MainMenuActivity.this, MoodScaleActivity.class);
    startActivity(intent);

        });


        ImageView mjImageView = findViewById(R.id.journal);
        mjImageView.setOnClickListener(view -> {
        Intent intent = new Intent(MainMenuActivity.this, MoodJournalActivity.class);
        startActivity(intent);
    });


        ImageView ssImageView = findViewById(R.id.music);
        ssImageView.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, SoothingSoundsActivity.class);
            startActivity(intent);
        });


        ImageView iqImageView = findViewById(R.id.quotes);
        iqImageView.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, InspirationalQuotesActivity.class);
            startActivity(intent);
        });


        ImageView awImageView = findViewById(R.id.wheel);
        awImageView.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, WheelActivity.class);
            startActivity(intent);
        });


        ImageView beImageView = findViewById(R.id.breathing);
        beImageView.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, BreathingActivity.class);
            startActivity(intent);
        });

        ImageView homeImageView = findViewById(R.id.home);
        homeImageView.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, MainMenuActivity.class);
            startActivity(intent);
        });

        ImageView calendarImageView = findViewById(R.id.calendar);
        calendarImageView.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, Calendar.class);
            startActivity(intent);
        });

        ImageView hisImageView = findViewById(R.id.history);
        hisImageView.setOnClickListener(view1 -> {
            Intent intent = new Intent(MainMenuActivity.this, Evaluation.class);
            startActivity(intent);
        });

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

}