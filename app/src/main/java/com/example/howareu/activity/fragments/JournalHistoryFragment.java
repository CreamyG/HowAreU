package com.example.howareu.activity.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.howareu.R;


public class JournalHistoryFragment extends Fragment {



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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_journal_history, container, false);
    }
}