package com.example.howareu.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.howareu.R;

public class BreathingActivity extends AppCompatActivity {

    private TextView timerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing);


            timerTextView = findViewById(R.id.timer);

        CountDownTimer countDownTimer;
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Time remaining: " + millisUntilFinished / 1000 + " seconds");
            }

            @Override
            public void onFinish() {
                timerTextView.setText("Time's up!");
            }
        };

        countDownTimer.start();

    }
}