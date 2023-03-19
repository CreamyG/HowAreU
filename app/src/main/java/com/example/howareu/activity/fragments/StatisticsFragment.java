package com.example.howareu.activity.fragments;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.howareu.R;
import com.example.howareu.adapter.CalendarAdapter;
import com.example.howareu.constant.Strings;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class StatisticsFragment extends Fragment {

    PieChart pieChart;
    TextView textVerySad, textSad, textNeutral, textHappy,textVeryHappy, monthLabel;
    Context context;
    Application application;
    private GridView mGridView;
    private CalendarAdapter mAdapter;
    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.application = getActivity().getApplication();
    }

    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        textVerySad = view.findViewById(R.id.textVerySad);
        textSad = view.findViewById(R.id.textSad);
        textNeutral = view.findViewById(R.id.textNeutral);
        textHappy = view.findViewById(R.id.textHappy);
        textVeryHappy = view.findViewById(R.id.textVeryHappy);
        pieChart = view.findViewById(R.id.piechart);
        // Set the percentage of language used
        textVerySad.setText(Integer.toString(40));
        textSad.setText(Integer.toString(30));
        textNeutral.setText(Integer.toString(5));
        textHappy.setText(Integer.toString(25));
        textVeryHappy.setText(Integer.toString(100));


        pieChart.addPieSlice(
                new PieModel(
                        Strings.MOOD_LABEL_VERY_SAD,
                        Integer.parseInt(textVerySad.getText().toString()),
                        ContextCompat.getColor(context, R.color.very_sad)));
        pieChart.addPieSlice(
                new PieModel(
                        Strings.MOOD_LABEL_SAD,
                        Integer.parseInt(textSad.getText().toString()),
                        ContextCompat.getColor(context, R.color.sad)));
        pieChart.addPieSlice(
                new PieModel(
                        Strings.MOOD_LABEL_NEUTRAL,
                        Integer.parseInt(textNeutral.getText().toString()),
                        ContextCompat.getColor(context, R.color.neutral)));
        pieChart.addPieSlice(
                new PieModel(
                        Strings.MOOD_LABEL_HAPPY,
                        Integer.parseInt(textHappy.getText().toString()),
                        ContextCompat.getColor(context, R.color.happy)));
        pieChart.addPieSlice(
                new PieModel(
                        Strings.MOOD_LABEL_VERY_HAPPY,
                        Integer.parseInt(textVeryHappy.getText().toString()),
                        ContextCompat.getColor(context, R.color.very_happy)));

        pieChart.startAnimation();




        mGridView = view.findViewById(R.id.calendarGrid);
        monthLabel = view.findViewById(R.id.month_label);
        // Create a list of dates to be displayed in the calendar
        ArrayList<Date> dates = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginning = cal.get(Calendar.DAY_OF_WEEK) - 1;
        cal.add(Calendar.DAY_OF_MONTH, -monthBeginning);
        while (dates.size() < 42) {
            String x = String.valueOf(cal.getTime());

            if(dates.size()>7){
                if(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)).equals("1")){
                    while (dates.size() < 42) {
                        dates.add(null);
                    }
                }
            }

            dates.add(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);

        }


        // Get the month name
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        String monthName = monthFormat.format(cal2.getTime());
        // Set the month label text
        monthLabel.setText(monthName);
        // Create a HashMap to store badge IDs for specific dates
        HashMap<Date, Integer> badgeMap = new HashMap<>();
        badgeMap.put(dates.get(3), R.drawable.happy); // Add a badge to the fourth date
        badgeMap.put(dates.get(10), R.drawable.sad); // Add a badge to the eleventh date

        // Create a new CalendarAdapter and set it to the GridView
        mAdapter = new CalendarAdapter(context, dates, badgeMap);
        mGridView.setAdapter(mAdapter);



        return view;
    }

}