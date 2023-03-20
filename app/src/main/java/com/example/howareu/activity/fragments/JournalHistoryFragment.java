package com.example.howareu.activity.fragments;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.howareu.R;
import com.example.howareu.activity.LoginActivity;
import com.example.howareu.activity.MainMenuActivity;
import com.example.howareu.activity.ViewJournalActivity;
import com.example.howareu.adapter.JournalHistoryAdapter;
import com.example.howareu.databases.repository.JournalRepository;
import com.example.howareu.model.Journal;
import com.example.howareu.model.SimpleJournalModel;
import com.example.howareu.model.StatDateAndMoodId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class JournalHistoryFragment extends Fragment implements JournalHistoryAdapter.OnCardClickListener{


    private JournalRepository journalDb;

    LiveData<List<Journal>> journalList;
    Context context;
    Application application;
    String currentMonth;
    String currentYear;
    Calendar cal2;
    GridView journalGrid;
    EditText searchQuery, passCodeEditText;
    TextView passCodeError;
    String passCode= "123";
    Boolean isDesc=false;
    JournalHistoryAdapter journalAdapter;
    ArrayList<SimpleJournalModel> journalArrayList = new ArrayList<>();
    Button sortBy,viewJournalBtn;
    ArrayList<Date> dateList = new ArrayList<>();
    ArrayList<String> content = new ArrayList<>();
    ArrayList<Boolean> isPrivate = new ArrayList<>();
    public JournalHistoryFragment() {
        // Required empty public constructor
    }


    public static JournalHistoryFragment newInstance(String param1, String param2) {
        JournalHistoryFragment fragment = new JournalHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.application = getActivity().getApplication();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        journalDb = new JournalRepository(application);
        cal2 =  Calendar.getInstance();
        currentMonth = String.valueOf(cal2.get(Calendar.MONTH)+1);
        currentYear = String.valueOf(cal2.get(Calendar.YEAR));
        journalAdapter= new JournalHistoryAdapter(context,journalArrayList,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_journal_history, container, false);
        journalGrid= view.findViewById(R.id.journalGrid);
        searchQuery= view.findViewById(R.id.searchQuery);
        sortBy = view.findViewById(R.id.sortBy);


        journalAdapter.setOnTextChangeListener(new JournalHistoryAdapter.OnCardClickListener() {
            @Override
            public void onCardClicked(String content, String date, boolean isPrivate) {

                if(isPrivate) {
                    showPassCodePopUp(content,date);
                }
                else{
                    showViewJournal(content,date);
                }

            }
        });
        searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getJournalSearch(searchQuery.getText().toString(),isDesc);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        journalGrid.setAdapter(journalAdapter);
        if(journalArrayList.isEmpty()) {
            getJournal(isDesc);
        }

        sortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDesc){
                    isDesc=false;
                }
                else{
                    isDesc=true;
                }
                journalArrayList.clear();
                journalAdapter.notifyDataSetChanged();
                getJournal(isDesc);


            }
        });
        return view;
    }

    public void getJournalSearch(String search, Boolean isDesc){
        journalArrayList.clear();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if(currentMonth.length()==1){
                    currentMonth = "0"+ currentMonth;
                }
                if(isDesc){
                    journalList = journalDb.getJournalBySearchDesc(currentMonth,currentYear,search);
                }
                else{
                    journalList = journalDb.getJournalBySearch(currentMonth,currentYear,search);
                }



                // Update UI with results on the main thread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(journalList != null){
                            journalList.observe(getViewLifecycleOwner(), new Observer<List<Journal>>() {
                                @Override
                                public void onChanged(List<Journal> journal) {
                                    List<Journal> journal2 = new ArrayList<>(journal);
                                    for (Journal j : journal2) {
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(j.getDate());
                                        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
                                        String date = monthFormat.format(cal.getTime());
                                        date += " "+cal.get(Calendar.DATE);
                                        date += " "+cal.get(Calendar.YEAR);
                                        journalArrayList.add(new SimpleJournalModel(date,j.getContent(),j.isPrivate()));
                                        journalAdapter.notifyDataSetChanged();
                                    }
                                    journalAdapter.notifyDataSetChanged();

                                }
                            });
                            journalAdapter.notifyDataSetChanged();
                        }
                    }
                });

                return null;
            }
        }.execute();
    }

    public void getJournal(boolean isDesc){
        journalArrayList.clear();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if(currentMonth.length()==1){
                    currentMonth = "0"+ currentMonth;
                }
                if(isDesc){
                    journalList = journalDb.getJournalByDateDesc(currentMonth,currentYear);
                }
                else{
                    journalList = journalDb.getJournalByDate(currentMonth,currentYear);
                }
                    // Update UI with results on the main thread
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(journalList != null){
                            journalList.observe(getViewLifecycleOwner(), new Observer<List<Journal>>() {
                                @Override
                                public void onChanged(List<Journal> journal) {
                                    List<Journal> journal2 = new ArrayList<>(journal);
                                    for (Journal j : journal2) {
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(j.getDate());
                                        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
                                        String date = monthFormat.format(cal.getTime());
                                        date += " "+cal.get(Calendar.DATE);
                                        date += " "+cal.get(Calendar.YEAR);
                                        journalArrayList.add(new SimpleJournalModel(date,j.getContent(),j.isPrivate()));
                                        journalAdapter.notifyDataSetChanged();
                                    }
                                    journalAdapter.notifyDataSetChanged();

                                }
                            });
                                journalAdapter.notifyDataSetChanged();
                            }
                        }
                    });

                return null;
            }
        }.execute();
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
    public void onCardClicked(String content, String date, boolean isPrivate) {

    }
}