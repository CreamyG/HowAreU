package com.example.howareu.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.howareu.R;

public class MoodCauseActivity extends AppCompatActivity {
    Bitmap bitmap = null;
    public  byte[] byteArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_cause);

        ImageView imageView = findViewById(R.id.moodPic);

       byteArray = getIntent().getByteArrayExtra("myBitmap");

         // Convert the byte array to a Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

         // Convert the Bitmap to a Drawable
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

        // Set the Drawable to an ImageView or any other View that supports a Drawable
        imageView.setImageDrawable(drawable);


        ImageView prevImage = findViewById(R.id.prev);
        prevImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoodCauseActivity.this, MoodScaleActivity.class);
                startActivity(intent);
            }
        });

        Button svbutton = findViewById(R.id.savebutton);
        svbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoodCauseActivity.this, MainMenuActivity.class);
                intent.putExtra("myBitmap",byteArray);
                startActivity(intent);
            }
        });
    }
}