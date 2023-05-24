package com.example.howareu.activity.fragments;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.howareu.R;
import com.example.howareu.activity.ViewJournalActivity;
import com.example.howareu.adapter.CalendarAdapter;
import com.example.howareu.constant.Integers;
import com.example.howareu.constant.Strings;
import com.example.howareu.databases.repository.JournalRepository;
import com.example.howareu.databases.repository.StatRepository;
import com.example.howareu.model.Journal;
import com.example.howareu.model.StatDateAndMoodId;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class StatisticsFragment extends Fragment implements CalendarAdapter.onClickEmoji {


    EditText  passCodeEditText;
    TextView passCodeError;
    Button viewJournalBtn;
    ImageView nextMonth, prevMonth;
    String passCode= "";
    PieChart pieChart;
    TextView textVerySad, textSad, textNeutral, textHappy,textVeryHappy, monthLabel,mood_month_ave;



    Context context;
    Application application;
    private GridView mGridView;
    private CalendarAdapter mAdapter;
    private StatRepository statDb;

    Calendar cal2;
    Calendar cal;
    ImageView mood_month_ave_image;

    String currentMonth;
    String currentYear;

    int monthChange;
    int monthChangeCounter;

    LiveData<List<StatDateAndMoodId>> statDateAndMoodId;
    private JournalRepository journalDb;
    LiveData<List<Journal>> journalList;
    ArrayList<Date> dateList;
    ArrayList<Date> dates;
    ArrayList<Integer> moodIdList;
    ArrayList<String> moodNameList;
    HashMap<Integer, Integer> badgeMap = new HashMap<>();
    HashMap<Integer, Journal> journalMap = new HashMap<>();

    ArrayList<Date> journalDateList;
    ArrayList<Boolean> isPrivateList;
    private SharedPreferences mPrefs;
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
        statDb = new StatRepository(application);
        journalDb = new JournalRepository(application);
        mPrefs = getActivity().getSharedPreferences(Strings.PREF_NAME, Context.MODE_PRIVATE);
        passCode = mPrefs.getString(Strings.PASSCODE, "CALLADMIN");
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
        mood_month_ave = view.findViewById(R.id.mood_month_ave);
        mood_month_ave_image = view.findViewById(R.id.mood_month_ave_image);
        prevMonth = view.findViewById(R.id.prevMonthButton);
        nextMonth = view.findViewById(R.id.nextMonthButton);


        cal2 = Calendar.getInstance();
        cal = Calendar.getInstance();
        cal2.setTimeInMillis(System.currentTimeMillis());
        cal.setTimeInMillis(System.currentTimeMillis());
        monthChange = 0;
        setMonthYear();


        mGridView = view.findViewById(R.id.calendarGrid);
        monthLabel = view.findViewById(R.id.month_label);
        // Create a list of dates to be displayed in the calendar


        setPieText();
        setMoodMonth();

        onClickListenersMonthChange();
        return view;
    }

    public void onClickListenersMonthChange(){
        prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearCalendar();
                monthChange=-1;
                setMonthYear();


            }
        });
        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCalendar();
                monthChange = 1;
                setMonthYear();
            }
        });
    }

    public void clearCalendar(){
        dates = new ArrayList<>();
        badgeMap =new HashMap<>();
        journalMap = new HashMap<>();
        mAdapter.notifyDataSetChanged();

    }

    public void setMonthYear(){
        cal2.add(Calendar.MONTH,monthChange);
        currentMonth = String.valueOf(cal2.get(Calendar.MONTH)+1);
        currentYear = String.valueOf(cal2.get(Calendar.YEAR));
        getDateAndMoodID();
    }

    public Integer getMoodImage(String moodName){
        int drawable = 0;
        switch(moodName){
            case Strings.MOOD_VERY_SAD:
                drawable = R.drawable.very_sad;
                break;
            case Strings.MOOD_SAD:
                drawable = R.drawable.ssad;
                break;
            case Strings.MOOD_NEUTRAL:
                drawable = R.drawable.neutral;
                break;
            case Strings.MOOD_HAPPY:
                drawable = R.drawable.smiling_face;
                break;
            case Strings.MOOD_VERY_HAPPY:
                drawable = R.drawable.very_happy;
                break;

        }

        return drawable;
    }

    public void setPieChart(){
        // Set the percentage of language used


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
    }

    public void setPieText(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                if(currentMonth.length()==1){
                    currentMonth = "0"+ currentMonth;
                }


                int verySad = statDb.getMoodCount(Integers.MOOD_VERY_SAD, currentMonth, currentYear);
                int sad = statDb.getMoodCount(Integers.MOOD_SAD, currentMonth, currentYear);
                int neutral =  statDb.getMoodCount(Integers.MOOD_NEUTRAL, currentMonth, currentYear);
                int happy =statDb.getMoodCount(Integers.MOOD_HAPPY, currentMonth, currentYear);
                int veryHappy =statDb.getMoodCount(Integers.MOOD_VERY_HAPPY, currentMonth, currentYear);
                textVerySad.setText(Integer.toString((verySad)));
                textSad.setText(Integer.toString(sad));
                textNeutral.setText(Integer.toString(neutral));
                textHappy.setText(Integer.toString(happy));
                textVeryHappy.setText(Integer.toString(veryHappy));



                // Update UI with results on the main thread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setPieChart();
                    }
                });
                return null;
            }
        }.execute();
    }

    public void getDateAndMoodID(){
        new AsyncTask<Void, Void, Void>() {
            boolean stateDone= false;
            boolean journalDone= false;



            @Override
            protected Void doInBackground(Void... voids) {
                if(currentMonth.length()==1){
                    currentMonth = "0"+ currentMonth;
                }

                dateList = new ArrayList<>();
                moodIdList = new ArrayList<>();
                moodNameList = new ArrayList<>();

                journalDateList = new ArrayList<>();
                isPrivateList= new ArrayList<>();

                statDateAndMoodId = statDb.getMoodIdAndDate(currentMonth,currentYear);
                journalList = journalDb.getJournalByDate(currentMonth,currentYear);


                // Update UI with results on the main thread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(statDateAndMoodId != null) {
                            statDateAndMoodId.observe(getViewLifecycleOwner(), new Observer<List<StatDateAndMoodId>>() {
                                @Override
                                public void onChanged(List<StatDateAndMoodId> statDateAndMoodIds) {
                                    List<StatDateAndMoodId> statArrayList = new ArrayList<>(statDateAndMoodIds);
                                    for (StatDateAndMoodId stat : statArrayList) {
                                        dateList.add(stat.getDate());
                                        moodIdList.add(stat.getMood_id());
                                        moodNameList.add(stat.getMood_name());

                                    }
                                    stateDone=true;
                                    checkIfDone();

                                }
                            });
                        }
                        else{
                            stateDone=true;
                            checkIfDone();
                        }


                    }
                });

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(journalList != null){
                            journalList.observe(getViewLifecycleOwner(), new Observer<List<Journal>>() {
                                @Override
                                public void onChanged(List<Journal> journals) {
                                    List<Journal> journArrayList = new ArrayList<>(journals);
                                    for (Journal journal : journArrayList) {
                                        journalDateList.add(journal.getDate());
                                        isPrivateList.add(journal.isPrivate());
                                    }
                                    journalDone=true;
                                    checkIfDone();
                                }
                            });
                        }
                        else{
                            stateDone=true;
                            checkIfDone();
                        }

                    }
                });


                return null;
            }
            private void checkIfDone(){
                if(journalDone&&stateDone){
                    createCalendar();


                }
            }

            @Override
            protected void onPostExecute(Void unused) {

                super.onPostExecute(unused);
                checkIfDone();


            }
        }.execute();
    }

    public void createCalendar(){
        dates = new ArrayList<>();
        cal.setTime(cal2.getTime());

        cal.set(Calendar.DAY_OF_MONTH, 1);

        int monthBeginning = cal.get(Calendar.DAY_OF_WEEK) -1;
        cal.add(Calendar.DAY_OF_MONTH, -monthBeginning);


        boolean startPlot = false;
        int emojiCounter = 0;
        int journalCounter = 0;
        while (dates.size() < 42) {


            //Nulls after end of the month
            if(dates.size()>7){
                if(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)).equals("1")){
                    while (dates.size() < 42) {
                        dates.add(null);
                    }
                }
            }
            //To start the plot if day is = 1(First day of the month)
            if(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)).equals("1")){
                startPlot = true;
            }
            //adds number date to the calendar
            dates.add(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);

            if(startPlot) {
                if (!dateList.isEmpty()) {
                    if (emojiCounter < dateList.size()) {

                        Calendar calDb = Calendar.getInstance();
                        calDb.setTime(dateList.get(emojiCounter));
                        Calendar cal3=Calendar.getInstance();
                        cal3.setTime(dates.get(dates.size()-1));
                        int date = calDb.get(Calendar.DAY_OF_MONTH);
                        int dateloop =  cal3.get(Calendar.DAY_OF_MONTH);
                        if (dateloop == date) {
                            badgeMap.put(dateList.get(emojiCounter).getDate(), getMoodImage(moodNameList.get(emojiCounter)));
                            emojiCounter++;

                            if(!journalDateList.isEmpty()){
                                if(journalCounter<journalDateList.size()){
                                    calDb = Calendar.getInstance();
                                    calDb.setTime(journalDateList.get(journalCounter));
                                    cal3=Calendar.getInstance();
                                    cal3.setTime(dates.get(dates.size()-1));
                                    date = calDb.get(Calendar.DAY_OF_MONTH);
                                    dateloop =  cal3.get(Calendar.DAY_OF_MONTH);
                                    if (dateloop == date) {
                                        journalMap.put(journalDateList.get(journalCounter).getDate(),journalList.getValue().get(journalCounter));
                                        journalCounter++;
                                    }

                                }
                            }

                        }
                    }
                }
            }



        }



        // Create a HashMap to store badge IDs for specific dates
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        String monthName = monthFormat.format(cal2.getTime());
        // Set the month label text
        monthLabel.setText(monthName+" "+ cal2.get(Calendar.YEAR));

        // Add a badge to the fourth date

        // Create a new CalendarAdapter and set it to the GridView
        mAdapter = new CalendarAdapter(context, dates, badgeMap, journalMap, this);

        mGridView.setAdapter(mAdapter);

    }

    public void setMoodMonth(){
        mood_month_ave_image.setVisibility(View.GONE);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                double ave=statDb.getMoodMonthAvg(currentMonth,currentYear);
                // Create a DecimalFormat object with the desired format pattern
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");

                // Apply the formatting to the number
                String formattedNumber = decimalFormat.format(ave);
                mood_month_ave.setText(String.valueOf(formattedNumber)+"%");
                getMoodEmoji(ave);
                // Update UI with results on the main thread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
                return null;
            }
        }.execute();
    }

    public void getMoodEmoji(double mood){


        mood_month_ave_image.setVisibility(View.VISIBLE);
        if(mood>=Integers.MOOD_PERCENT_VERY_SAD  && mood < Integers.MOOD_PERCENT_SAD){
            mood_month_ave_image.setImageResource(R.drawable.very_sad);

        }
        else if(mood>=Integers.MOOD_PERCENT_SAD  && mood < Integers.MOOD_PERCENT_NEUTRAL){
            mood_month_ave_image.setImageResource(R.drawable.ssad);
        }
        else if(mood>=Integers.MOOD_PERCENT_NEUTRAL  && mood < Integers.MOOD_PERCENT_HAPPY){
            mood_month_ave_image.setImageResource(R.drawable.neutral);
        }
        else if(mood>=Integers.MOOD_PERCENT_HAPPY  && mood < Integers.MOOD_PERCENT_VERY_HAPPY){
            mood_month_ave_image.setImageResource(R.drawable.smiling_face);
        }
        else if(mood==Integers.MOOD_PERCENT_VERY_HAPPY){
            mood_month_ave_image.setImageResource(R.drawable.very_happy);
        }
        else{
            mood_month_ave.setText(Strings.STAT_LABEL_NO_AVE);
            mood_month_ave_image.setVisibility(View.GONE);
        }

    }




    public void showPassCodePopUp(String content,String date){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_passcode, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
        passCodeEditText= dialogView.findViewById(R.id.passCodeEditText);
        viewJournalBtn= dialogView.findViewById(R.id.viewJournalBtn);
        passCodeError = dialogView.findViewById(R.id.passCodeError);
        viewJournalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passCodeEditText.getText().toString().equals(passCode)){
                    showViewJournal(content,date);
                    dialog.dismiss();
                }
                else{
                    passCodeError.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void showViewJournal(String content, String date){
        Intent intent = new Intent(getActivity(), ViewJournalActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("date", date);
        startActivity(intent);
    }

    @Override
    public void onEmojiClicked(Journal journal) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(journal.getDate());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        String date = monthFormat.format(cal.getTime());
        date += " "+cal.get(Calendar.DATE);
        date += " "+cal.get(Calendar.YEAR);

        if (journal.isPrivate()) {
            showPassCodePopUp(journal.getContent(),date);
        }
        else{
            showViewJournal(journal.getContent(),date);
        }
    }
}