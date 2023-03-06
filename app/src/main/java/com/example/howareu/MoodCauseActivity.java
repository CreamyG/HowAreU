package com.example.howareu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MoodCauseActivity extends AppCompatActivity {
    Bitmap bitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_cause);

        ImageView imageView = findViewById(R.id.moodPic);

        byte[] byteArray = getIntent().getByteArrayExtra("myBitmap");

         // Convert the byte array to a Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

         // Convert the Bitmap to a Drawable
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

        // Set the Drawable to an ImageView or any other View that supports a Drawable
        imageView.setImageDrawable(drawable);


    }
}