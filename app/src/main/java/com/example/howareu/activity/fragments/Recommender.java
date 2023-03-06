package com.example.howareu.activity.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.howareu.R;


public class Recommender extends Fragment {

    public  byte[] byteArray;
    public Recommender() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu_home_recommender, container, false);
        Bundle args = getArguments();
        if (args != null) {
            byteArray =args.getByteArray("byteArray");
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            // Convert the Bitmap to a Drawable
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            ImageView moodPic2= view.findViewById(R.id.moodPic2);
            // Set the Drawable to an ImageView or any other View that supports a Drawable
            moodPic2.setImageDrawable(drawable);

        }
        // Inflate the layout for this fragment
        return view;
    }
}