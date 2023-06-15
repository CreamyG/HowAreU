package com.example.howareu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.howareu.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class GuideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public GuideAdapter(Context context){
        this.context = context;
    }

    public int[] slide_images={
            R.drawable.page_zero,
            R.drawable.page_one,
            R.drawable.page_two,
            R.drawable.page_three,
            R.drawable.page_four,
            R.drawable.page_five,
            R.drawable.page_six,
            R.drawable.page_seven,
            R.drawable.page_last
    };

    public String[] slide_head={
            "Greetings!","Main Menu","Activity","Todo", "Journal","Statistics","Mood for the Month","Journal History","You are all set"

    };

    public String[] slide_dec={
            "Hello there! \"How are U\" is a mood monitoring app that aims to empower individuals in achieving and maintaining good mental health.",
            "Here we have three options, the Activity module, statistics and Journal history. We also have the current date and the menu",
            "Here is where you input activity, rate the mood of that activity. You can click the add button to add more activity to input",
            "Here we have the Recommendation of activity which is the todo. You can click the change button to change the activity",
            "Here you can type your journals, it is optional. You can either set it to private or not. After finishing all mood inputs and rate you can click save to finish for the day.",
            "After completing the activities, users can evaluate their progress by accessing the statistics section. Here we have the Mood Count",
            "Here we have the summary for the mood of the month, we have the calendar which is clickable to go to the written journal for the date.",
            "The application includes a journal history feature that displays all the user's inputted journal entries.",
            "You are all set and ready to go. Have a nice day!"

    };
    @Override
    public int getCount() {
        return slide_head.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==(RelativeLayout)o;
    }

    @SuppressLint("ServiceCast")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view =layoutInflater.inflate(R.layout.slider_layout_guide,container,false);
        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_images);
        TextView slideHead= (TextView) view.findViewById(R.id.slide_head);
        TextView slide_decs= (TextView) view.findViewById(R.id.slide_decs);

        slideImageView.setImageResource(slide_images[position]);
        slideHead.setText(slide_head[position]);
        slide_decs.setText(slide_dec[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
