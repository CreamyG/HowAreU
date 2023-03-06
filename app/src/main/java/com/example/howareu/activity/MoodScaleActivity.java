package com.example.howareu.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.howareu.MoodCauseActivity;
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