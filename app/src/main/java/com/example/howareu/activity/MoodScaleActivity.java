package com.example.howareu.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.howareu.R;

import java.io.ByteArrayOutputStream;

public class MoodScaleActivity extends AppCompatActivity {
    ImageView image;
    Bitmap bitmap;
    Drawable drawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moodscale_activity);
    }

    public void onButtonSelect(View view){
        switch (view.getId()){
            case R.id.happyMood:
                image = findViewById(R.id.happyMood);
                goToMoodCause();

                break;
            case R.id.sadMood:
                image = findViewById(R.id.sadMood);
                goToMoodCause();
                break;
            case R.id.angryMood:
                image = findViewById(R.id.angryMood);
                goToMoodCause();
                break;
            case R.id.cryingMood:
                image = findViewById(R.id.cryingMood);
                goToMoodCause();
                break;
            case R.id.disappointedMood:
                image = findViewById(R.id.disappointedMood);
                goToMoodCause();
                break;
            case R.id.calmMood:
                image = findViewById(R.id.calmMood);
                goToMoodCause();
                break;
            case R.id.scaredMood:
                image = findViewById(R.id.scaredMood);
                goToMoodCause();
                break;
            case R.id.tearyMood:
                image = findViewById(R.id.tearyMood);
                goToMoodCause();
                break;
            case R.id.tiredMood:
                image = findViewById(R.id.tiredMood);
                goToMoodCause();
                break;
            case R.id.coolMood:
                image = findViewById(R.id.coolMood);
                goToMoodCause();
                break;
            default:
                //do nothing
                break;

        }


    }
    public void goToMoodCause(){
        drawable = image.getDrawable();
        bitmap = ((BitmapDrawable) drawable).getBitmap();
        updateUI();
    }

    public void updateUI(){
        Intent intent = new Intent(MoodScaleActivity.this, MoodCauseActivity.class);
        intent.putExtra("myBitmap",drawableToBitmap());
        startActivity(intent);
    }



    public  byte[] drawableToBitmap(){
        // Convert the Bitmap to a byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


}