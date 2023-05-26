package com.example.howareu.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.howareu.R;
import com.example.howareu.adapter.GuideAdapter;
import com.example.howareu.constant.Strings;

public class GuideActivity extends AppCompatActivity {
    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private GuideAdapter sliderAdapter;
    private TextView[] mDots;
    private Button mprevBtn;
    private Button mnextBtn;
    private int currentPage;
    private SharedPreferences mPrefs;
    private boolean isLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mPrefs= getSharedPreferences(Strings.START_PREF_NAME, Context.MODE_PRIVATE);
        mSlideViewPager = (ViewPager)findViewById(R.id.slideViewPager);
        mDotLayout= (LinearLayout) findViewById(R.id.dotsLayout);
        sliderAdapter= new GuideAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        mprevBtn=(Button)findViewById(R.id.prevBtn);
        mnextBtn=(Button)findViewById(R.id.nextBtn);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
        mnextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage==mDots.length-1){
                    openMain();
                }
                mSlideViewPager.setCurrentItem(currentPage+1);
            }
        });
        mprevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSlideViewPager.setCurrentItem(currentPage-1);

            }
        });
    }
    private void openMain() {
        isLogged = mPrefs.getBoolean(Strings.IS_LOGGED,false);
        Intent i = null;
        if(isLogged){
            i =new Intent(this, MainMenuActivity.class);

        }
        else{
            boolean fromLogout = mPrefs.getBoolean(Strings.FROM_LOGOUT,false);
            if(fromLogout){
                i= new Intent(this, LoggedOutActivity.class);
            }
            else{
                i= new Intent(this, SetUserActivity.class);
            }

        }
        startActivity(i);
        finish();

    }

    public void addDotsIndicator(int position){
        mDots = new TextView[5];
        mDotLayout.removeAllViews();
        for(int i=0;i<mDots.length;i++){
            mDots[i] = new TextView(this);

            mDots[i].setText( HtmlCompat.fromHtml("&#8226;", HtmlCompat.FROM_HTML_MODE_LEGACY));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(Color.parseColor("#CCFFFFFF"));
            mDotLayout.addView(mDots[i]);
        }
        if(mDots.length>0){
            mDots[position].setTextColor(Color.WHITE);
        }
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            currentPage=i;

            if(i==0){
                mnextBtn.setEnabled(true);
                mprevBtn.setEnabled(false);
                mprevBtn.setVisibility(View.INVISIBLE);
                mnextBtn.setText("Next");
                mprevBtn.setText("");
            }
            else if(i==mDots.length-1){
                mnextBtn.setEnabled(true);
                mprevBtn.setEnabled(true);
                mprevBtn.setVisibility(View.VISIBLE);
                mnextBtn.setText("Finish");
                mprevBtn.setText("Back");
            }
            else{
                mnextBtn.setEnabled(true);
                mprevBtn.setEnabled(true);
                mprevBtn.setVisibility(View.VISIBLE);
                mnextBtn.setText("Next");
                mprevBtn.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}