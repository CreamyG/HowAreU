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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.howareu.databases.repository.StatRepository;
import com.example.howareu.model.Activity;
import com.example.howareu.model.Journal;
import com.example.howareu.model.SimpleActivityModel;
import com.example.howareu.model.SimpleAllActivityModel;
import com.example.howareu.model.SimpleTodoModel;
import com.example.howareu.model.Stat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class HomeFragment extends Fragment implements HomeActivityAdapter.OnDeleteActivityClickListener, HomeActivityAdapter.OnActivityMoodRateClickListener,
        HomeTodoAdapter.OnTodoMoodRateClickListener,View.OnClickListener,HomeActivityAdapter.OnTextChangeListener, HomeTodoAdapter.OnReplaceClickListener {

    //Fragment needs
    Context context;
    Application application;

    //Views
    RecyclerView activityRecycler;
    RecyclerView todoRecycler;
    EditText journalInput;
    Button btnAddActivity,btnAddTodo,btnSave;
    ImageView btnSad, btnVerySad, btnNeutral, btnHappy, btnVeryHappy,btnExit;
    TextView MoodTotalForTheDay, textMonth,textDate;
    Switch isPrivate;

    //Adapters
    HomeActivityAdapter activityAdapter;
    HomeTodoAdapter todoAdapter;

    //List
    List<SimpleActivityModel> simpleActivityModel;
    List<SimpleTodoModel> simpleTodoModel;

    List<Activity> allActivity ;
    List<Journal> allJournal;
    ArrayList<SimpleAllActivityModel> allActivityModels = new ArrayList<>();





    //Shared pref
    private SharedPreferences mPrefs;
    private SharedPreferences prefsNotif;
    Gson gson = new Gson();

    //Date and time's
    private long lastClickTime;
    private long currentTime;
    private String currentDay;
    private String currentMonth;
    private String currentYear;
    private Calendar calen;

    //Databases
    private ActivityRepository activityDb;
    private JournalRepository journalDb;
    private StatRepository statDb;




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
        //List for Adapter populate
        simpleActivityModel = new ArrayList<>();
        simpleTodoModel = new ArrayList<>();

        //SharePref
        mPrefs = getActivity().getSharedPreferences(Strings.PREF_NAME, Context.MODE_PRIVATE);
        prefsNotif = getActivity().getSharedPreferences(Strings.PREF_NOTIF, Context.MODE_PRIVATE);



        //Adapters
        activityAdapter= new HomeActivityAdapter(simpleActivityModel,this.getContext(),this,this, this);
        todoAdapter= new HomeTodoAdapter(simpleTodoModel,this.getContext(),this,this);

        //Databases
        activityDb = new ActivityRepository(application);
        journalDb = new JournalRepository(application);
        statDb = new StatRepository(application);


        //Date and Times
        lastClickTime = mPrefs.getLong(Strings.LAST_CLICK_TIME, 0);
        currentTime = System.currentTimeMillis();

        calen = Calendar.getInstance();
        calen.setTimeInMillis(System.currentTimeMillis());

        currentDay = String.valueOf(calen.get(Calendar.DAY_OF_MONTH));
        currentMonth = String.valueOf(calen.get(Calendar.MONTH)+1);
        currentYear = String.valueOf(calen.get(Calendar.YEAR));


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        //Views
        activityRecycler = view.findViewById(R.id.activity_recycler);
        todoRecycler = view.findViewById(R.id.todo_recycler);
        btnAddActivity = view.findViewById(R.id.btnAddActivity);
        btnAddTodo = view.findViewById(R.id.btnAddTodo);
        btnSave = view.findViewById(R.id.btnSave);
        journalInput = view.findViewById(R.id.journalInput);
        isPrivate = view.findViewById(R.id.isPrivate);

        //Recyclers
        activityRecycler.setAdapter(activityAdapter);
        activityRecycler.setLayoutManager(new LinearLayoutManager(context));
        todoRecycler.setAdapter(todoAdapter);
        todoRecycler.setLayoutManager(new LinearLayoutManager(context));



        //Add Activity
        btnAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(simpleActivityModel.size()<10){
                    //Add a empty value to simpleActivityModel to add an item on to the Activity adapter
                    simpleActivityModel.add(new SimpleActivityModel("",0,true));
                    //update the adapter
                    activityAdapter.notifyItemInserted(simpleActivityModel.size()-1);
                }
                else{
                    //Reached maximum Activity to add
                    Toast.makeText(context, "Limited to 10 Activity only", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Delete Activity
        activityAdapter.setOnDeleteItemClickListener(new HomeActivityAdapter.OnDeleteActivityClickListener() {
            @Override
            public void onDeleteActivityClicked(int position) {
                //remove a date on the simpleActivityModel to remove an item on to the adapter
                simpleActivityModel.remove(position);
                //update the adapter
                activityAdapter.notifyItemRemoved(position);
                //update the pref for unsaved progress
                unsavedActivityToPref();

            }
        });
        //AddTodo Activity
        btnAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check first if todo_list has more activity to get
                if (simpleTodoModel.size()<Arrays.todoArrayList().size()) {
                    //check if existing todoActivities are done or rated
                    if (isTodoRateAll()) {
                        //Add a empty value to SimpleTodoModel to add an item on to the Todo_adapter
                        simpleTodoModel.add(new SimpleTodoModel(Arrays.todoArrayList().get(getRandomToDo()), 0, true));
                        //update the adapter
                        todoAdapter.notifyItemInserted(simpleTodoModel.size() - 1);

                    }

                }
                else{
                    Toast.makeText(context, "Out of To do Activity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Replace TodoActivity
        todoAdapter.setOnReplaceClickListener(new HomeTodoAdapter.OnReplaceClickListener() {
            @Override
            public void onReplaceClicked(int position) {
                if(simpleTodoModel.size()<Arrays.todoArrayList().size()){
                    simpleTodoModel.get(position).setTodoName(Arrays.todoArrayList().get(getRandomToDo()));
                    todoAdapter.notifyItemChanged(position);
                }
                else{
                    Toast.makeText(context, "Out of To do Activity", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Mood Rate Activity
        activityAdapter.setOnMoodRateClickListener(new HomeActivityAdapter.OnActivityMoodRateClickListener() {
            @Override
            public void onMoodRateClicked(String name,int position) {
                rateMoodPopUp(position,true, name);

            }
        });
        //Rate Mood onTodo
        todoAdapter.setOnTodoMoodRateClickListener(new HomeTodoAdapter.OnTodoMoodRateClickListener() {
            @Override
            public void onTodoMoodRateClicked(int position) {
                rateMoodPopUp(position,false,null);

            }
        });

        //TextChangeListener To save in preference(save progress)
        activityAdapter.setOnTextChangeListener(new HomeActivityAdapter.OnTextChangeListener() {
            @Override
            public void onTextChanged(String name, int position) {
                simpleActivityModel.get(position).setActivityName(name);
                unsavedActivityToPref();
            }
        });

        //Save when user is done
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                savePopUp();

            }
        });

        //Journal TextChange To save in preference(save progress)
        journalInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!checkIfAlreadyDone()){
                    mPrefs.edit().putString(Strings.JOURNAL_UNSAVED,journalInput.getText().toString()).apply();
                }


            }
        });



        getActivityList();//From Database
        setDateForTopPart();//Date for Top part
        return view;


    }


    //Get Random TodoAct
    public int getRandomToDo(){
        Random random = new Random();
        List<String> names = new ArrayList<>();

        for (SimpleTodoModel todo : simpleTodoModel) {
            names.add(todo.getTodoName());
        }

        int numb = random.nextInt(Arrays.todoArrayList().size());

        while (names.contains(Arrays.todoArrayList().get(numb))) {
            numb = random.nextInt(Arrays.todoArrayList().size());
        }

        return numb;
    }

    //Check if rated all
    public boolean isTodoRateAll(){
        boolean ratedAll = true;
        for(SimpleTodoModel todo: simpleTodoModel){
            if(todo.getMoodrate()==0) {
                ratedAll = false;
                break;
            }
        }
        return ratedAll;
    }

    //Setting the current Date on top part
    private void setDateForTopPart() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentTime);
        // Get the month name
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        String monthName = monthFormat.format(cal.getTime());
        if(monthName.length()>3){
            monthName = monthName.substring(0,3)+".";
        }
        textMonth.setText(monthName);
        textDate.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
    }


    //Populate all fields
    public void populateForm(){

        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        if (checkIfAlreadyDone()) {
            if(simpleActivityModel.isEmpty()&&simpleTodoModel.isEmpty()) {
                getSavedData();
            }
            disableButtonandForms();

            String journalText2 = mPrefs.getString(Strings.JOURNAL_UNSAVED, "");
            activityAdapter.notifyDataSetChanged();
            todoAdapter.notifyDataSetChanged();
            String journalText = mPrefs.getString(Strings.JOURNAL_SAVE, "");
            journalInput.setText(journalText);
            Boolean isChecked = mPrefs.getBoolean(Strings.JOURNAL_PRIVACY, false);
            isPrivate.setChecked(isChecked);

        }
        else if(checkIfDateExist()){
            for(SimpleAllActivityModel act:allActivityModels){
                disableButtonandForms();
                journalSetByDateExist();
                if(act.getType()==Integers.ACTIVITY){
                    simpleActivityModel.add(new SimpleActivityModel(act.getActivityName(),act.getMoodrate(),false));
                }
                if(act.getType()==Integers.TODO){
                    simpleTodoModel.add(new SimpleTodoModel(act.getActivityName(),act.getMoodrate(),false));
                }
            }

            activityAdapter.notifyDataSetChanged();
            todoAdapter.notifyDataSetChanged();
        }
        else {
            String journalText = mPrefs.getString(Strings.JOURNAL_UNSAVED, "");
            if(!journalText.isEmpty()){
                journalInput.setText(journalText);
            }
            if(simpleActivityModel.isEmpty()){
                if(!checkUnSavedActivitySPisNull(type)){
                    getUnSavedActivity(type,true);
                }
                else{
                    simpleActivityModel.add(new SimpleActivityModel("", 0, true));
                }

            }
            activityAdapter.notifyItemInserted(simpleActivityModel.size() - 1);


            if(simpleTodoModel.isEmpty()) {
                if(!checkUnSavedToDoSPisNull(type)){

                    getUnSavedTodo(type,true);
                    todoAdapter.notifyDataSetChanged();
                }
                else{
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
                    if(todoAdapter!=null){
                        todoAdapter.notifyDataSetChanged();
                    }
                    unsavedToDoToPref();
                }

            }


        }

    }


    //Disable all fields and buttons
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


    //Disable all buttons and forms
    public void disableButtonandForms(){
        btnAddActivity.setEnabled(false);
        btnSave.setEnabled(false);
        journalInput.setEnabled(false);
        isPrivate.setEnabled(false);
        btnAddTodo.setEnabled(false);
    }


    //Check if activity has current Date on DB
    public boolean checkIfDateExist(){
        boolean alreadyDone= false;
        if(!allActivityModels.isEmpty()){

            for(SimpleAllActivityModel act:allActivityModels){
                Calendar cal = Calendar.getInstance();
                cal.setTime(act.getDate());
                Calendar cal2 = Calendar.getInstance();
                cal2.setTimeInMillis(currentTime);
                if(isSameDay(cal,cal2)){
                    alreadyDone = true;
                    break;
                }

            }


        }
        return alreadyDone;
    }


    //If journalDB date exist then set it
    public void journalSetByDateExist(){
        if(!allJournal.isEmpty()){
            for(Journal journal:allJournal){
                Calendar cal = Calendar.getInstance();
                cal.setTime(journal.getDate());
                Calendar cal2 = Calendar.getInstance();
                cal2.setTimeInMillis(currentTime);
                if(isSameDay(cal,cal2)){
                    journalInput.setText(journal.getContent());
                    break;
                }

            }
        }
    }

    //Check if current date already saved or done
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
            btnAddTodo.setEnabled(true);
            journalInput.setEnabled(true);
            isPrivate.setEnabled(true);
            alreadyDone=false;
        } else {
            alreadyDone = true;
            // disable the button click if it's the same day
        }
        return alreadyDone;


    }

    //Pop up for Mood Rating Activity or To do
    public void rateMoodPopUp(int position, boolean isActivity, String name){

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
                    activityAdapter.notifyDataSetChanged();
                }

                switch(v.getId()){
                    case R.id.btnExit:
                        dialog.dismiss();
                        break;
                    case R.id.btnSad:
                        if(isActivity){
                            simpleActivityModel.get(position).setMoodrate(Integers.MOOD_PERCENT_SAD);
                            activityAdapter.notifyDataSetChanged();
                            unsavedActivityToPref();
                        }
                        else{
                            simpleTodoModel.get(position).setMoodrate(Integers.MOOD_PERCENT_SAD);
                            todoAdapter.notifyDataSetChanged();
                            unsavedToDoToPref();
                        }
                        dialog.dismiss();
                        break;
                    case R.id.btnVerySad:
                        if(isActivity){
                            simpleActivityModel.get(position).setMoodrate(Integers.MOOD_PERCENT_VERY_SAD);
                            activityAdapter.notifyDataSetChanged();
                            unsavedActivityToPref();
                        }
                        else{
                            simpleTodoModel.get(position).setMoodrate(Integers.MOOD_PERCENT_VERY_SAD);
                            todoAdapter.notifyDataSetChanged();
                            unsavedToDoToPref();
                        }
                        dialog.dismiss();

                        break;
                    case R.id.btnNeutral:
                        if(isActivity){
                            simpleActivityModel.get(position).setMoodrate(Integers.MOOD_PERCENT_NEUTRAL);
                            activityAdapter.notifyDataSetChanged();
                            unsavedActivityToPref();
                        }
                        else{
                            simpleTodoModel.get(position).setMoodrate(Integers.MOOD_PERCENT_NEUTRAL);
                            todoAdapter.notifyDataSetChanged();
                            unsavedToDoToPref();
                        }

                        dialog.dismiss();
                        break;
                    case R.id.btnHappy:
                        if(isActivity){
                            simpleActivityModel.get(position).setMoodrate(Integers.MOOD_PERCENT_HAPPY);
                            activityAdapter.notifyDataSetChanged();
                            unsavedActivityToPref();
                        }
                        else{
                            simpleTodoModel.get(position).setMoodrate(Integers.MOOD_PERCENT_HAPPY);
                            todoAdapter.notifyDataSetChanged();
                            unsavedToDoToPref();
                        }

                        dialog.dismiss();
                        break;
                    case R.id.btnVeryHappy:
                        if(isActivity){
                            simpleActivityModel.get(position).setMoodrate(Integers.MOOD_PERCENT_VERY_HAPPY);
                            activityAdapter.notifyDataSetChanged();
                            unsavedActivityToPref();
                        }
                        else{
                            simpleTodoModel.get(position).setMoodrate(Integers.MOOD_PERCENT_VERY_HAPPY);
                            todoAdapter.notifyDataSetChanged();
                            unsavedToDoToPref();
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
        View dialogView = inflater.inflate(R.layout.dialog_confirm_saving, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
        Button btnConfirmSave =dialogView.findViewById(R.id.btnConfirmSave2);
        Button btnCancelSave =dialogView.findViewById(R.id.btnCancel);

        btnConfirmSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmedSave();
                dialog.dismiss();
            }
        });

        btnCancelSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

    }

    public void confirmedSave(){
        boolean hasNoEmptyActivity = true;
        boolean hasNoEmptyActivityRate = true;
        boolean hasNoEmptyTodoRate = true;
        String errorMessage = "";
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
            saveStatDb();
            if(!journalInput.getText().toString().trim().equals("")) {
                saveJournalToDb();
            }
            mPrefs.edit().putLong(Strings.LAST_CLICK_TIME, currentTime).apply();
            mPrefs.edit().putString(Strings.JOURNAL_SAVE, journalInput.getText().toString()).apply();
            mPrefs.edit().putBoolean(Strings.JOURNAL_PRIVACY, isPrivate.isChecked()).apply();
            disableEverything();
            prefsNotif.edit().putBoolean(Strings.IS_DAY_DONE,true).apply();
            savedPopUp();
        }
        else{
            if(!hasNoEmptyActivity){
                errorMessage ="Check Fields Has Empty Activity Input \n " ;
            }
            if(!hasNoEmptyActivityRate){
                errorMessage +="Please Rate Every Activity\n" ;
            }
            if(!hasNoEmptyTodoRate){
                errorMessage +="Please Rate Every To do Activity\n" ;
                
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_not_saved, null);
            builder.setView(dialogView);
            AlertDialog dialogError = builder.create();
            dialogError.show();
            Button btnConfirmError =dialogView.findViewById(R.id.btnConfirmError);
            TextView errorMes = dialogView.findViewById(R.id.errorMessage);
            errorMes.setText(errorMessage);
            btnConfirmError.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogError.dismiss();
                }
            });
        }
    }

    public void savedPopUp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_saved, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        MoodTotalForTheDay = dialogView.findViewById(R.id.MoodTotalForTheDay);
        MoodTotalForTheDay = dialogView.findViewById(R.id.MoodTotalForTheDay);

        MoodTotalForTheDay.setText(getCalculatedMoodString(calculateMood()));
        Button btnConfirm =dialogView.findViewById(R.id.btnConfirm);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    //Get all activity list of current date
    public void getActivityList(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_loading, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {

                dialog.show();
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {

                if(currentMonth.length()<2){
                    currentMonth="0"+currentMonth;
                }
                allActivity = activityDb.getActivityByDate(currentDay,currentMonth,currentYear);

                allJournal = journalDb.getJournalByWholeDate(currentDay,currentMonth,currentYear);
                int x= 0;
                // Update UI with results on the main thread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (allActivity != null) {


                            for (Activity act : allActivity) {
                                allActivityModels.add(new SimpleAllActivityModel(
                                        act.getDate(),
                                        act.getContent(),
                                        getMoodRateById(act.getMood_id()),
                                        act.getType()));
                            }



                        }

                    }

                });

                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                populateForm();
                dialog.dismiss();
                super.onPostExecute(unused);
            }
        }.execute();
    }

    //Save acts and journal To Database
    public void saveActivitiesToDb(){

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ArrayList<String> activityName = new ArrayList<>();
                ArrayList<String> activityRate = new ArrayList<>();
                for(SimpleActivityModel x: simpleActivityModel){
                    //Save in Database
                    int moodId = getMoodIdByRate(x.getMoodrate());
                    Activity activityAdd = new Activity(x.activityName,moodId,Integers.ACTIVITY);
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
                    Activity activityAdd = new Activity(x.todoName,moodId,Integers.TODO);
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
                        mPrefs.edit().putString(Strings.ACTIVITY_NAME_UNSAVED,null).apply();
                        mPrefs.edit().putString(Strings.ACTIVITY_RATE_UNSAVED, null).apply();
                        mPrefs.edit().putString(Strings.TODO_NAME_UNSAVED, null).apply();
                        mPrefs.edit().putString(Strings.TODO_RATE_UNSAVED, null).apply();
                        mPrefs.edit().putString(Strings.JOURNAL_UNSAVED, null).apply();
                    }
                });
                return null;
            }
        }.execute();

    }

    //Save Journal to Database
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

    //Save calculated mood to stat database
    public void saveStatDb(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                statDb.insertStat(new Stat(calculateMood(),getMoodIdByCalculatedRate(calculateMood())));

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

    //save unsaved todo_progress to sharedpref
    public void unsavedToDoToPref(){
        ArrayList<String> todoName = new ArrayList<>();
        ArrayList<String> todoRate = new ArrayList<>();
        for(SimpleTodoModel x: simpleTodoModel){
            //Save in SharedPref
            todoName.add(x.getTodoName());
            todoRate.add(String.valueOf(x.getMoodrate()));
        }
        String jsonTodoName = gson.toJson(todoName);
        String jsonTodoRate = gson.toJson(todoRate);
        mPrefs.edit().putString(Strings.TODO_NAME_UNSAVED, jsonTodoName).apply();
        mPrefs.edit().putString(Strings.TODO_RATE_UNSAVED, jsonTodoRate).apply();
    }

    //save unsaved activity_progress to sharedpref
    public void unsavedActivityToPref(){
        ArrayList<String> activityName = new ArrayList<>();
        ArrayList<String> activityRate = new ArrayList<>();
        for(SimpleActivityModel x: simpleActivityModel){

            //Save in SharedPref
            activityName.add(x.getActivityName());
            activityRate.add(String.valueOf(x.getMoodrate()));
        }

        String jsonActivityName = gson.toJson(activityName);
        String jsonActivityRate = gson.toJson(activityRate);
        mPrefs.edit().putString(Strings.ACTIVITY_NAME_UNSAVED, jsonActivityName).apply();
        mPrefs.edit().putString(Strings.ACTIVITY_RATE_UNSAVED, jsonActivityRate).apply();
    }


    //Gettings unsaved todo_progress from sharedpref
    public void getUnSavedTodo(Type type, Boolean isEnabled){
        String jsonName =mPrefs.getString(Strings.TODO_NAME_UNSAVED, "");
        String jsonRate =mPrefs.getString(Strings.TODO_RATE_UNSAVED, "");

        ArrayList<String> savedTodoNameArray = gson.fromJson(jsonName, type);
        ArrayList<String> savedTodoRateArray = gson.fromJson(jsonRate, type);


        for(int x =0; x<savedTodoNameArray.size();x++){
            String qwe=savedTodoRateArray.get(x);
            simpleTodoModel.add(new SimpleTodoModel(savedTodoNameArray.get(x),Integer.valueOf(savedTodoRateArray.get(x)),isEnabled));
        }
    }

    //Gettings unsaved activity_progress from sharedpref
    public void getUnSavedActivity(Type type, Boolean isEnabled){
        String jsonName =mPrefs.getString(Strings.ACTIVITY_NAME_UNSAVED, "");
        String jsonRate =mPrefs.getString(Strings.ACTIVITY_RATE_UNSAVED, "");
        ArrayList<String> savedActivityNameArray = gson.fromJson(jsonName, type);
        ArrayList<String> savedActivityRateArray = gson.fromJson(jsonRate, type);
        for(int x =0; x<savedActivityNameArray.size();x++){
            simpleActivityModel.add(new SimpleActivityModel(savedActivityNameArray.get(x),Integer.valueOf(savedActivityRateArray.get(x)),isEnabled));
        }
    }

    //Get All savedData from sharedpref(if user was done)
    public void getSavedData(){
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        getSaveActivity(type,false);
        getSaveTodo(type,false);

    }
    //Get All saved TodoData from sharedpref(if user was done)
    public void getSaveTodo(Type type, Boolean isEnabled){
        String jsonName =mPrefs.getString(Strings.TODO_NAME_SAVE, "");
        String jsonRate =mPrefs.getString(Strings.TODO_RATE_SAVE, "");

        ArrayList<String> savedTodoNameArray = gson.fromJson(jsonName, type);
        ArrayList<String> savedTodoRateArray = gson.fromJson(jsonRate, type);


        for(int x =0; x<savedTodoNameArray.size();x++){

            simpleTodoModel.add(new SimpleTodoModel(savedTodoNameArray.get(x),Integer.valueOf(savedTodoRateArray.get(x)),isEnabled));
        }
    }

    //Get All saved ActivityData from sharedpref(if user was done)
    public void getSaveActivity(Type type, Boolean isEnabled){
        String jsonName =mPrefs.getString(Strings.ACTIVITY_NAME_SAVE, "");
        String jsonRate =mPrefs.getString(Strings.ACTIVITY_RATE_SAVE, "");
        ArrayList<String> savedActivityNameArray = gson.fromJson(jsonName, type);
        ArrayList<String> savedActivityRateArray = gson.fromJson(jsonRate, type);
        for(int x =0; x<savedActivityNameArray.size();x++){
            simpleActivityModel.add(new SimpleActivityModel(savedActivityNameArray.get(x),Integer.valueOf(savedActivityRateArray.get(x)),isEnabled));
        }
    }


    //Check if unsaved Act shared pref is empty
    public boolean checkUnSavedActivitySPisNull(Type type){
        String json =mPrefs.getString(Strings.ACTIVITY_NAME_UNSAVED, "");
        ArrayList<String> savedActivityNameArray = gson.fromJson(json, type);
        boolean isEmpty = false;
        if(savedActivityNameArray == null){
            isEmpty = true;
        }else{
            if(savedActivityNameArray.isEmpty()){
                isEmpty = true;
            }
        }

        return isEmpty ;
    }

    //Check if unsaved todo_shared pref is empty
    public boolean checkUnSavedToDoSPisNull(Type type){
        String json =mPrefs.getString(Strings.TODO_NAME_UNSAVED, "");
        ArrayList<String> savedActivityNameArray = gson.fromJson(json, type);
        boolean isEmpty = false;
        if(savedActivityNameArray == null){
            isEmpty = true;
        }
        else{
            if(savedActivityNameArray.isEmpty()){
                isEmpty = true;
            }
        }
        return isEmpty ;
    }

    //Check if Same date of the 2  dates in the parameter
    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        // compare the year, month, and day of the two timestamps
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    //Mood calculate
    public double calculateMood(){

        ArrayList<Integer> allRate = new ArrayList<Integer>();

        for(SimpleActivityModel x: simpleActivityModel){
            allRate.add(x.getMoodrate());
        }
        for(SimpleTodoModel x:simpleTodoModel){
            allRate.add(x.getMoodrate());
        }


        double sumOfRate = 0 ;


        for(int  x:allRate) {
            sumOfRate+=x;
        }


        double mood = sumOfRate/allRate.size();
        return mood;
    }

    //Calculated mood to its mood name
    public String getCalculatedMoodString(double mood){
        String moodForTheDay= "";
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
        // Create a DecimalFormat object with the desired format pattern
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        // Apply the formatting to the number
        String formattedNumber = decimalFormat.format(mood);
        moodForTheDay+=formattedNumber;
        return moodForTheDay;
    }

    //Calculated mood to its mood id
    public int getMoodIdByCalculatedRate(double mood){
        int moodId= 0;
        if(mood>=Integers.MOOD_PERCENT_VERY_SAD  && mood < Integers.MOOD_PERCENT_SAD){
            moodId = Integers.MOOD_VERY_SAD;
        }
        else if(mood>=Integers.MOOD_PERCENT_SAD  && mood < Integers.MOOD_PERCENT_NEUTRAL){
            moodId = Integers.MOOD_SAD;
        }
        else if(mood>=Integers.MOOD_PERCENT_NEUTRAL  && mood < Integers.MOOD_PERCENT_HAPPY){
            moodId = Integers.MOOD_NEUTRAL;
        }
        else if(mood>=Integers.MOOD_PERCENT_HAPPY  && mood < Integers.MOOD_PERCENT_VERY_HAPPY){
            moodId = Integers.MOOD_HAPPY;
        }
        else if(mood==Integers.MOOD_PERCENT_VERY_HAPPY){
            moodId = Integers.MOOD_VERY_HAPPY;
        }

        return moodId;
    }

    //MoodRate to Id
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

    //MoodId to MoodRate
    public int getMoodRateById(int id){
        int moodId = 0;
        switch(id){
            case Integers.MOOD_SAD:
                moodId=  Integers.MOOD_PERCENT_SAD;
                break;
            case Integers.MOOD_VERY_SAD:
                moodId=  Integers.MOOD_PERCENT_VERY_SAD;
                break;
            case Integers.MOOD_NEUTRAL:
                moodId=  Integers.MOOD_PERCENT_NEUTRAL;
                break;
            case Integers.MOOD_HAPPY:
                moodId= Integers.MOOD_PERCENT_HAPPY;
                break;
            case Integers.MOOD_VERY_HAPPY:
                moodId= Integers.MOOD_PERCENT_VERY_HAPPY;
                break;
            default:
                moodId=0;
                break;


        }
        return moodId;
    }




    //Unused Required Interfaces
    @Override
    public void onTodoMoodRateClicked(int position) {

    }
    @Override
    public void onMoodRateClicked(String name, int position) {

    }

    @Override
    public void onTextChanged(String name, int position) {

    }

    @Override
    public void onReplaceClicked(int position) {

    }
    @Override
    public void onDeleteActivityClicked(int position) {

    }



    @Override
    public void onClick(View v) {

    }
}