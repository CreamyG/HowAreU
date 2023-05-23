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
            R.drawable.happy,
            R.drawable.home,
            R.drawable.history,
    };

    public String[] slide_head={
            "Greetings!","Home Menu","Translating via Camera"

    };

    public String[] slide_dec={
            "Good day and HI! \n\nHI (Hand Ilocano) is an Ilocano sign language translator that has a 2-way communication model. The dataset of our app are simple Ilocano words that everyone is using daily. \n\nBefore you get started, this tutorial will help you to understand and know the function of our application. \nThis will be a brief walkthrough on our app.",
            "On the home menu, we have 2 buttons (the camera and the keyboard). \n\n The camera button will lead you to a form where the app can translate sign language via camera. \n\nWhile the keyboard button will let you type a given word and a video will appear at the screen representing your input.",
            "Point the camera on the person you want to communicate with. A text or word will appear on the textbox. \n\n If you want to switch camera, click the switch icon on the left side of the textbox. \n\n If you want to read aloud the text on the textbox, click the speaker icon on the right side of the textbox."
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
