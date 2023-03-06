package com.example.howareu.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.howareu.activity.fragments.Calendar;
import com.example.howareu.activity.fragments.Histroy;
import com.example.howareu.activity.fragments.Recommender;
import com.example.howareu.databases.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;

public class MainMenuActivity extends AppCompatActivity {
    private TextView nameTextView;
    private UserRepository userRep;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private Fragment fragmentRecommender;
    private Fragment fragmenCalendar;
    private Fragment fragmentHistory;
    private byte[] byteArray;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main_menu);
        byteArray = getIntent().getByteArrayExtra("myBitmap");


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        fragmentRecommender = new Recommender();

        fragmenCalendar = new Calendar();

        fragmentHistory = new Histroy();

        setName();


        ImageView homeImageView = findViewById(R.id.home);
        Bundle args = new Bundle();
        args.putByteArray("byteArray",byteArray);
        fragmentRecommender.setArguments(args);



        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentRecommender)
                .commit();
        homeImageView.setOnClickListener(view -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentRecommender)
                    .commit();
        });

        ImageView calendarImageView = findViewById(R.id.calendar);
        calendarImageView.setOnClickListener(view -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmenCalendar)
                    .commit();
        });

        ImageView hisImageView = findViewById(R.id.history);
        hisImageView.setOnClickListener(view1 -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentHistory)
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
            case R.id.setting:
                Toast.makeText(this, "Still no Setting Activity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about_us:
                Toast.makeText(this, "Still no About Us Activity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.lagout:
                auth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
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

}