package com.example.howareu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainMenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        Button msButton = findViewById(R.id.moodscale);
        msButton.setOnClickListener(view -> {

    Intent intent = new Intent(MainMenuActivity.this, MoodScaleActivity.class);
    startActivity(intent);

        });


    Button mjButton = findViewById(R.id.journal);
    mjButton.setOnClickListener(view -> {
        Intent intent = new Intent(MainMenuActivity.this, MoodJournalActivity.class);
        startActivity(intent);
    });


        Button ssButton = findViewById(R.id.music);
        ssButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, SoothingSoundsActivity.class);
            startActivity(intent);
        });


        Button iqButton = findViewById(R.id.quotes);
        iqButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, InspirationalQuotesActivity.class);
            startActivity(intent);
        });


        Button waButton = findViewById(R.id.wheel);
        waButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, WheelActivity.class);
            startActivity(intent);
        });


        Button beButton = findViewById(R.id.breathing);
        beButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, BreathingActivity.class);
            startActivity(intent);
        });

        ImageView calImageView = findViewById(R.id.calicon);
        calImageView.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, Calendar.class);
            startActivity(intent);
        });

        ImageView evalImageView = findViewById(R.id.evalicon);
        evalImageView.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, Evaluation.class);
            startActivity(intent);
        });

    }
}