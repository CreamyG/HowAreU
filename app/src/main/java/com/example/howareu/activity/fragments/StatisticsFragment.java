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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class StatisticsFragment extends Fragment {

    PieChart pieChart;
    TextView textVerySad, textSad, textNeutral, textHappy,textVeryHappy;
    Context context;
    Application application;
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

        // Get current month and year
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

         // Set calendar to first day of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1);

         // Calculate number of days in the month
        int numDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Create datelist containing the dates of the current month
        List<Date> dateList = new ArrayList<>();
        for (int i = 0; i < numDaysInMonth; i++) {
            dateList.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }


        GridView gridView = view.findViewById(R.id.calendarGrid);
        CalendarAdapter adapter = new CalendarAdapter(context, dateList);
        gridView.setAdapter(adapter);



        return view;
    }

}