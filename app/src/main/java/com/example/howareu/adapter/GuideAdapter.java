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
            R.drawable.cool,
            R.drawable.home,
            R.drawable.stats,
            R.drawable.journal,
            R.drawable.ready,
    };

    public String[] slide_head={
            "Greetings!","Activity Module","Statistics", "Journal History","Ready!"

    };

    public String[] slide_dec={
            "Hello there! \"How are U\" is a mood monitoring app that aims to empower individuals in achieving and maintaining good mental health.",
            "Here is where you input activity, rate the mood of that activity, and see the recommendations of activity to do. Here you will also input your journal.",
            "After completing the activities, users can evaluate their progress by accessing the statistics section. ",
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
        CircleImageView slideImageView = (CircleImageView) view.findViewById(R.id.slide_images);
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
