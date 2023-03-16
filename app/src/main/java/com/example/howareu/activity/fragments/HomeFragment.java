package com.example.howareu.activity.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.howareu.R;
import com.example.howareu.adapter.HomeActivityAdapter;
import com.example.howareu.adapter.HomeTodoAdapter;
import com.example.howareu.constant.Integers;
import com.example.howareu.model.SimpleActivityModel;
import com.example.howareu.model.SimpleTodoModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeActivityAdapter.OnDeleteActivityClickListener, HomeActivityAdapter.OnActivityMoodRateClickListener,
        HomeTodoAdapter.OnTodoMoodRateClickListener,View.OnClickListener,HomeActivityAdapter.OnTextChangeListener {
    RecyclerView activityRecycler;
    RecyclerView todoRecycler;
    Button btnAddActivity;
    Button btnSave;
    Button btnSad, btnVerySad, btnNeutral, btnHappy, btnVeryHappy,btnExit;
    HomeActivityAdapter activityAdapter;
    HomeTodoAdapter todoAdapter;
    Context context;
    List<SimpleActivityModel> simpleActivityModel = new ArrayList<>();
    List<SimpleTodoModel> simpleTodoModel = new ArrayList<>();
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
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
        activityAdapter= new HomeActivityAdapter(simpleActivityModel,this.getContext(),this,this, this);
        todoAdapter= new HomeTodoAdapter(simpleTodoModel,this.getContext(),this);
        if(simpleActivityModel.isEmpty()){
            simpleActivityModel.add(new SimpleActivityModel("",0));
            activityAdapter.notifyItemInserted(simpleActivityModel.size()-1);
        }
        simpleTodoModel.add(new SimpleTodoModel("Jog",0));
        simpleTodoModel.add(new SimpleTodoModel("Cook",0));

        if (getArguments() != null) {
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        activityRecycler = view.findViewById(R.id.activity_recycler);
        todoRecycler = view.findViewById(R.id.todo_recycler);
        btnAddActivity = view.findViewById(R.id.btnAddActivity);
        btnSave = view.findViewById(R.id.btnSave);

        activityRecycler.setAdapter(activityAdapter);
        activityRecycler.setLayoutManager(new LinearLayoutManager(context));

        todoRecycler.setAdapter(todoAdapter);
        todoRecycler.setLayoutManager(new LinearLayoutManager(context));

        //Add Activity
        btnAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                simpleActivityModel.add(new SimpleActivityModel("",0));
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

        return view;


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
        btnExit= dialogView.findViewById(R.id.btnExit);
        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.btnExit:
                        dialog.dismiss();
                        break;
                    case R.id.btnSad:
                        if(isActivity){
                            simpleActivityModel.get(position).setMoodrate(Integers.MOOD_PERCENT_SAD);
                            simpleActivityModel.get(position).setActivityName(name);
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
                            simpleActivityModel.get(position).setActivityName(name);
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
                            simpleActivityModel.get(position).setActivityName(name);
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
                            simpleActivityModel.get(position).setActivityName(name);
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
                            simpleActivityModel.get(position).setActivityName(name);
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
}