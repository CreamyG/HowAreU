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

public class MoodScaleActivity extends AppCompatActivity {
    ImageView image;
    Bitmap imageBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moodscale_activity);
    }

    public void onButtonSelect(View view){
        switch (view.getId()){
            case R.id.happyMood:
                image = findViewById(R.id.happyMood);
                drawableToBitmap(image.getDrawable());
                // updateUI();
                break;
            case R.id.sadMood:
                image = findViewById(R.id.sadMood);
                drawableToBitmap(image.getDrawable());
                // updateUI();
                break;
            default:
                //do nothing
                break;

        }


    }
    public void updateUI(){
        Intent intent = new Intent(MoodScaleActivity.this, Evaluation.class);
        intent.putExtra("key",imageBitmap);
        startActivity(intent);
    }

    public  void drawableToBitmap(Drawable drawable){
        imageBitmap= ((BitmapDrawable) drawable).getBitmap();

    }


}