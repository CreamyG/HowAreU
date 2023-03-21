package com.example.howareu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.howareu.R;
import com.google.firebase.auth.FirebaseAuth;

public class ViewJournalActivity extends AppCompatActivity {
    String date;
    String content;
    TextView journalDateText,journalContentText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journal);
        date = getIntent().getStringExtra("date");
        content =  getIntent().getStringExtra("content");
        journalDateText = findViewById(R.id.journalDateText);
        journalContentText = findViewById(R.id.journalContentText);
        journalDateText.setText(date);
        journalContentText.setText(content);

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
            case R.id.setting:
                Toast.makeText(this, "Still no Setting Activity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about_us:
                Toast.makeText(this, "Still no About Us Activity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.lagout:
                auth.signOut();
                startActivity(new Intent(this, OnlineLoginActivity.class));
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}