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
            "Hello there! \"How are U\" is a mood monitoring app that aims to empower individuals in achieving and maintaining good mental health. We believe that by providing the tools, resources, and personalized support necessary, users can actively participate in their mental well-being, ultimately leading to a happier and more fulfilling life",
            "Here is where you input activity, rate the mood of that activity, and see the recommendations of activity to do. Here you will also input your journal.",
            "After completing the activities, users can evaluate their progress by accessing the statistics section. Here, they can find the mood count and a summary of their moods. The application includes a calendar view that displays the user's daily mood inputs. This calendar allows users to track their moods over time. Users can also view their overall mood for the month, which provides insights into their emotional well-being on a broader scale. Additionally, the application enables users to access and review their previous inputs, promoting reflection and self-awareness.",
            "The application includes a journal history feature that displays all the user's inputted journal entries. Users can access the journal history in two ways: through the journal button or by clicking on specific dates on the calendar. The calendar is clickable, allowing users to tap or click on a date where they recorded their mood. Upon selecting a date, the corresponding journal entry for that day will be automatically displayed.",
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
