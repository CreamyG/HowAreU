package com.example.howareu.activity.fragments;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.howareu.R;
import com.example.howareu.adapter.HomeActivityAdapter;
import com.example.howareu.adapter.HomeTodoAdapter;
import com.example.howareu.constant.Arrays;
import com.example.howareu.constant.Integers;
import com.example.howareu.constant.Strings;
import com.example.howareu.databases.repository.ActivityRepository;
import com.example.howareu.databases.repository.JournalRepository;
import com.example.howareu.model.Activity;
import com.example.howareu.model.Journal;
import com.example.howareu.model.SimpleActivityModel;
import com.example.howareu.model.SimpleTodoModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class HomeFragment extends Fragment implements HomeActivityAdapter.OnDeleteActivityClickListener, HomeActivityAdapter.OnActivityMoodRateClickListener,
        HomeTodoAdapter.OnTodoMoodRateClickListener,View.OnClickListener,HomeActivityAdapter.OnTextChangeListener {
    RecyclerView activityRecycler;
    RecyclerView todoRecycler;
    EditText journalInput;
    Button btnAddActivity;
    Button btnSave;
    Button btnSad, btnVerySad, btnNeutral, btnHappy, btnVeryHappy,btnExit;
    Button btnConfirm;
    TextView MoodTotalForTheDay;

    HomeActivityAdapter activityAdapter;
    HomeTodoAdapter todoAdapter;
    Context context;
    Application application;
    List<SimpleActivityModel> simpleActivityModel = new ArrayList<>();
    List<SimpleTodoModel> simpleTodoModel = new ArrayList<>();

    Switch isPrivate;
    Gson gson = new Gson();


    private SharedPreferences mPrefs;

    private long lastClickTime;
    private long currentTime;

    private ActivityRepository activityDb;
    private JournalRepository journalDb;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.application = getActivity().getApplication();
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = getActivity().getSharedPreferences(Strings.PREF_NAME, Context.MODE_PRIVATE);
        activityAdapter= new HomeActivityAdapter(simpleActivityModel,this.getContext(),this,this, this);
        todoAdapter= new HomeTodoAdapter(simpleTodoModel,this.getContext(),this);

        activityDb = new ActivityRepository(application);
        journalDb = new JournalRepository(application);
        lastClickTime = mPrefs.getLong(Strings.LAST_CLICK_TIME, 0);
        currentTime = System.currentTimeMillis();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        activityRecycler = view.findViewById(R.id.activity_recycler);
        todoRecycler = view.findViewById(R.id.todo_recycler);
        btnAddActivity = view.findViewById(R.id.btnAddActivity);
        btnSave = view.findViewById(R.id.btnSave);
        journalInput = view.findViewById(R.id.journalInput);
        isPrivate = view.findViewById(R.id.isPrivate);
        activityRecycler.setAdapter(activityAdapter);
        activityRecycler.setLayoutManager(new LinearLayoutManager(context));

        todoRecycler.setAdapter(todoAdapter);
        todoRecycler.setLayoutManager(new LinearLayoutManager(context));



        //Add Activity
        btnAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                simpleActivityModel.add(new SimpleActivityModel("",0,true));
                activityAdapter.notifyItemInserted(simpleActivityModel.size()-1);
            }
        });

        //Delete Activity
        activityAdapter.setOnDeleteItemClickListener(new HomeActivityAdapter.OnDeleteActivityClickListener() {
            @Override
            public void onDeleteActivityClicked(int position) {
                  simpleActivityModel.remove(position);
                  activityAdapter.notifyItemRemoved(position);

            }
        });

        //Mood Rate Activity
        activityAdapter.setOnMoodRateClickListener(new HomeActivityAdapter.OnActivityMoodRateClickListener() {
            @Override
            public void onMoodRateClicked(String name,int position) {
                rateMoodPopUp(position,true, name);
            }
        });
        todoAdapter.setOnTodoMoodRateClickListener(new HomeTodoAdapter.OnTodoMoodRateClickListener() {
            @Override
            public void onTodoMoodRateClicked(int position) {
                rateMoodPopUp(position,false,null);
            }
        });
        activityAdapter.setOnTextChangeListener(new HomeActivityAdapter.OnTextChangeListener() {
            @Override
            public void onTextChanged(String name, int position) {
                simpleActivityModel.get(position).setActivityName(name);

            }
        });

        //Save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasNoEmptyActivity = true;
                boolean hasNoEmptyActivityRate = true;
                boolean hasNoEmptyTodoRate = true;
                for(SimpleActivityModel x: simpleActivityModel){
                    if(x.getActivityName().isEmpty()){
                        hasNoEmptyActivity=false;
                    }
                    if(x.getMoodrate()==0){
                        hasNoEmptyActivityRate=false;
                    }
                }
                for(SimpleTodoModel x: simpleTodoModel){
                    if(x.getMoodrate()==0){
                        hasNoEmptyTodoRate=false;
                    }
                }

                if(hasNoEmptyActivity&&hasNoEmptyActivityRate&&hasNoEmptyTodoRate){
                    //Success no empty Form
                    currentTime = System.currentTimeMillis();
                    saveActivitiesToDb();
                    saveJournalToDb();
                    mPrefs.edit().putLong(Strings.LAST_CLICK_TIME, currentTime).apply();
                    mPrefs.edit().putString(Strings.JOURNAL_SAVE, journalInput.getText().toString()).apply();
                    mPrefs.edit().putBoolean(Strings.JOURNAL_PRIVACY, isPrivate.isChecked()).apply();
                    disableEverything();
                    savePopUp();

                }
                else{
                    //Message or Warning
                }

            }
        });

        populateForm();
        return view;


    }
    public void populateForm(){


        if (checkIfAlreadyDone()) {
            activityAdapter.notifyDataSetChanged();
            todoAdapter.notifyDataSetChanged();
            String journalText = mPrefs.getString(Strings.JOURNAL_SAVE, "");
            journalInput.setText(journalText);
            Boolean isChecked = mPrefs.getBoolean(Strings.JOURNAL_PRIVACY, false);
            isPrivate.setChecked(isChecked);

        } else {
            if(simpleActivityModel.isEmpty()&&simpleTodoModel.isEmpty()) {
                simpleActivityModel.add(new SimpleActivityModel("", 0, true));
                activityAdapter.notifyItemInserted(simpleActivityModel.size() - 1);

                List<Integer> randomNumbers = new ArrayList<>();
                Random random = new Random();
                while (randomNumbers.size() < 3) {
                    int numb = random.nextInt(Arrays.todoArrayList().size());
                    if (!randomNumbers.contains(numb)) {
                        randomNumbers.add(numb);
                    }
                }

                for (int x : randomNumbers) {
                    simpleTodoModel.add(new SimpleTodoModel(Arrays.todoArrayList().get(x), 0, true));
                }
            }

        }

    }

    public void disableEverything(){
        disableButtonandForms();

        for(SimpleActivityModel x:simpleActivityModel){
            x.setEnabled(false);
        }
        for(SimpleTodoModel x:simpleTodoModel){
            x.setEnabled(false);
        }
        activityAdapter.notifyDataSetChanged();
        todoAdapter.notifyDataSetChanged();

    }
    public void disableButtonandForms(){
        btnAddActivity.setEnabled(false);
        btnSave.setEnabled(false);
        journalInput.setEnabled(false);
        isPrivate.setEnabled(false);
    }

    public boolean checkIfAlreadyDone(){
        boolean alreadyDone= false;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentTime);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(lastClickTime);
        if (!isSameDay(cal, cal2)) {
            // enable the button click if it's not the same day
            btnSave.setEnabled(true);
            btnAddActivity.setEnabled(true);
            journalInput.setEnabled(true);
            isPrivate.setEnabled(true);
            alreadyDone=false;
        } else {
            if(simpleActivityModel.isEmpty()&&simpleTodoModel.isEmpty()) {
                getSavedData();
            }
            disableButtonandForms();
            alreadyDone = true;
            // disable the button click if it's the same day
        }
        return alreadyDone;


    }

    public void saveJournalToDb(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                String journalText = journalInput.getText().toString();
                Boolean isJournalPrivate = isPrivate.isChecked();
                journalDb.insertJournal(new Journal(journalText, isJournalPrivate));

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


    @Override
    public void onDeleteActivityClicked(int position) {

    }



    @Override
    public void onClick(View v) {

    }

    //Pop up for Mood Rating Activity or To do
    public void rateMoodPopUp(int position, boolean isActivity, String name){
        if(isActivity) {
            activityAdapter.notifyDataSetChanged();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_rate_mood, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
        btnSad= dialogView.findViewById(R.id.btnSad);
        btnVerySad = dialogView.findViewById(R.id.btnVerySad);
        btnNeutral = dialogView.findViewById(R.id.btnNeutral);
        btnHappy = dialogView.findViewById(R.id.btnHappy);
        btnVeryHappy = dialogView.findViewById(R.id.btnVeryHappy);
        btnExit = dialogView.findViewById(R.id.btnExit);

        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isActivity){
                    simpleActivityModel.get(position).setActivityName(name);
                }

                switch(v.getId()){
                    case R.id.btnExit:
                        dialog.dismiss();
                        break;
                    case R.id.btnSad:
                        if(isActivity){
                            simpleActivityModel.get(position).setMoodrate(Integers.MOOD_PERCENT_SAD);
                            activityAdapter.notifyDataSetChanged();
                        }
                        else{
                            simpleTodoModel.get(position).setMoodrate(Integers.MOOD_PERCENT_SAD);
                            todoAdapter.notifyDataSetChanged();
                        }
                        dialog.dismiss();
                        break;
                    case R.id.btnVerySad:
                        if(isActivity){
                            simpleActivityModel.get(position).setMoodrate(Integers.MOOD_PERCENT_VERY_SAD);
                            activityAdapter.notifyDataSetChanged();
                        }
                        else{
                            simpleTodoModel.get(position).setMoodrate(Integers.MOOD_PERCENT_VERY_SAD);
                            todoAdapter.notifyDataSetChanged();
                        }
                        dialog.dismiss();

                        break;
                    case R.id.btnNeutral:
                        if(isActivity){
                            simpleActivityModel.get(position).setMoodrate(Integers.MOOD_PERCENT_NEUTRAL);
                            activityAdapter.notifyDataSetChanged();
                        }
                        else{
                            simpleTodoModel.get(position).setMoodrate(Integers.MOOD_PERCENT_NEUTRAL);
                            todoAdapter.notifyDataSetChanged();
                        }

                        dialog.dismiss();
                        break;
                    case R.id.btnHappy:
                        if(isActivity){
                            simpleActivityModel.get(position).setMoodrate(Integers.MOOD_PERCENT_HAPPY);
                            activityAdapter.notifyDataSetChanged();
                        }
                        else{
                            simpleTodoModel.get(position).setMoodrate(Integers.MOOD_PERCENT_HAPPY);
                            todoAdapter.notifyDataSetChanged();
                        }

                        dialog.dismiss();
                        break;
                    case R.id.btnVeryHappy:
                        if(isActivity){
                            simpleActivityModel.get(position).setMoodrate(Integers.MOOD_PERCENT_VERY_HAPPY);
                            activityAdapter.notifyDataSetChanged();
                        }
                        else{
                            simpleTodoModel.get(position).setMoodrate(Integers.MOOD_PERCENT_VERY_HAPPY);
                            todoAdapter.notifyDataSetChanged();
                        }

                        dialog.dismiss();
                        break;
                    default:

                        break;
                }
            }
        };
        btnSad.setOnClickListener(onClick);
        btnVerySad.setOnClickListener(onClick);
        btnNeutral.setOnClickListener(onClick);
        btnHappy.setOnClickListener(onClick);
        btnVeryHappy.setOnClickListener(onClick);
        btnExit.setOnClickListener(onClick);

        btnExit = dialogView.findViewById(R.id.btnExit);


    }
    //Pop up After Saving
    public void savePopUp(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_saved, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        MoodTotalForTheDay = dialogView.findViewById(R.id.MoodTotalForTheDay);

        MoodTotalForTheDay.setText(calculateMood());
        btnConfirm =dialogView.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public String calculateMood(){
        String moodForTheDay= "";
        ArrayList<Integer> allRate = new ArrayList<Integer>();
        for(SimpleActivityModel x: simpleActivityModel){
            allRate.add(x.getMoodrate());
        }
        for(SimpleTodoModel x:simpleTodoModel){
            allRate.add(x.getMoodrate());
        }
        int sumOfRate = 0 ;
        for(int  x:allRate) {
            sumOfRate+=x;
        }
        int mood = sumOfRate/allRate.size();


        if(mood>=Integers.MOOD_PERCENT_VERY_SAD  && mood < Integers.MOOD_PERCENT_SAD){
            moodForTheDay = "Very Sad = ";
        }
        else if(mood>=Integers.MOOD_PERCENT_SAD  && mood < Integers.MOOD_PERCENT_NEUTRAL){
            moodForTheDay = "Sad = ";
        }
        else if(mood>=Integers.MOOD_PERCENT_NEUTRAL  && mood < Integers.MOOD_PERCENT_HAPPY){
            moodForTheDay = "Neutral = " ;
        }
        else if(mood>=Integers.MOOD_PERCENT_HAPPY  && mood < Integers.MOOD_PERCENT_VERY_HAPPY){
            moodForTheDay = "Happy = ";
        }
        else if(mood==Integers.MOOD_PERCENT_VERY_HAPPY){
            moodForTheDay = "Happy = ";
        }
        moodForTheDay+=mood;
        return moodForTheDay;
    }

    @Override
    public void onTodoMoodRateClicked(int position) {

    }

    @Override
    public void onMoodRateClicked(String name, int position) {

    }

    @Override
    public void onTextChanged(String name, int position) {

    }

    public void saveActivitiesToDb(){

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ArrayList<String> activityName = new ArrayList<>();
                ArrayList<String> activityRate = new ArrayList<>();
                for(SimpleActivityModel x: simpleActivityModel){
                    //Save in Database
                    int moodId = getMoodIdByRate(x.getMoodrate());
                    Activity activityAdd = new Activity(x.activityName,moodId);
                    activityDb.insertActivity(activityAdd);
                    //Save in SharedPref
                    activityName.add(x.getActivityName());
                    activityRate.add(String.valueOf(x.getMoodrate()));
                }
                ArrayList<String> todoName = new ArrayList<>();
                ArrayList<String> todoRate = new ArrayList<>();
                for(SimpleTodoModel x: simpleTodoModel){
                    //Save in Database
                    int moodId = getMoodIdByRate(x.getMoodrate());
                    Activity activityAdd = new Activity(x.todoName,moodId);
                    activityDb.insertActivity(activityAdd);

                    //Save in SharedPref
                    todoName.add(x.getTodoName());
                    todoRate.add(String.valueOf(x.getMoodrate()));
                }

                String jsonActivityName = gson.toJson(activityName);
                String jsonActivityRate = gson.toJson(activityRate);
                String jsonTodoName = gson.toJson(todoName);
                String jsonTodoRate = gson.toJson(todoRate);


                mPrefs.edit().putString(Strings.ACTIVITY_NAME_SAVE, jsonActivityName).apply();
                mPrefs.edit().putString(Strings.ACTIVITY_RATE_SAVE, jsonActivityRate).apply();
                mPrefs.edit().putString(Strings.TODO_NAME_SAVE, jsonTodoName).apply();
                mPrefs.edit().putString(Strings.TODO_RATE_SAVE, jsonTodoRate).apply();



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



    public void getSavedData(){
        String jsonActivityName =mPrefs.getString(Strings.ACTIVITY_NAME_SAVE, "");
        String jsonActivityRate =mPrefs.getString(Strings.ACTIVITY_RATE_SAVE, "");
        String jsonTodoName =mPrefs.getString(Strings.TODO_NAME_SAVE, "");
        String jsonTodoRate =mPrefs.getString(Strings.TODO_RATE_SAVE, "");
        Type type = new TypeToken<ArrayList<String>>() {}.getType();

        ArrayList<String> savedActivityNameArray = gson.fromJson(jsonActivityName, type);
        ArrayList<String> savedActivityRateArray = gson.fromJson(jsonActivityRate, type);
        ArrayList<String> savedTodoNameArray = gson.fromJson(jsonTodoName, type);
        ArrayList<String> savedTodoRateArray = gson.fromJson(jsonTodoRate, type);

        for(int x =0; x<savedActivityNameArray.size();x++){
            simpleActivityModel.add(new SimpleActivityModel(savedActivityNameArray.get(x),Integer.valueOf(savedActivityRateArray.get(x)),false));
        }
        for(int x =0; x<savedTodoNameArray.size();x++){
            simpleTodoModel.add(new SimpleTodoModel(savedTodoNameArray.get(x),Integer.valueOf(savedTodoRateArray.get(x)),false));
        }
    }



    private boolean isSameDay(Calendar cal1, Calendar cal2) {


        // compare the year, month, and day of the two timestamps
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }


    public int getMoodIdByRate(int rate){
        int moodId = 0;
        switch(rate){
            case Integers.MOOD_PERCENT_SAD:
                moodId=  Integers.MOOD_SAD;
                break;
            case Integers.MOOD_PERCENT_VERY_SAD:
                moodId=  Integers.MOOD_VERY_SAD;
                break;
            case Integers.MOOD_PERCENT_NEUTRAL:
                moodId=  Integers.MOOD_NEUTRAL;
                break;
            case Integers.MOOD_PERCENT_HAPPY:
                moodId= Integers.MOOD_HAPPY;
                break;
            case Integers.MOOD_PERCENT_VERY_HAPPY:
                moodId= Integers.MOOD_VERY_HAPPY;
                break;
            default:
                moodId=0;
                break;


        }
        return moodId;
    }
}