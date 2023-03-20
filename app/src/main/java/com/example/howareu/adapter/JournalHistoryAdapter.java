package com.example.howareu.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;

import com.example.howareu.R;
import com.example.howareu.constant.Arrays;
import com.example.howareu.model.Journal;
import com.example.howareu.model.SimpleJournalModel;
import com.example.howareu.model.StatDateAndMoodId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class JournalHistoryAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<SimpleJournalModel> journalList;
    private LayoutInflater mInflater;
    private OnCardClickListener onCardClickListener;

    public JournalHistoryAdapter(Context context, ArrayList<SimpleJournalModel> journal,OnCardClickListener onCardClickListener ) {
        this.mContext = context;
        this.journalList = journal;
        this.mInflater = LayoutInflater.from(context);
        this.onCardClickListener=onCardClickListener;
    }

    @Override
    public int getCount() {
        return journalList.size();
    }

    @Override
    public Object getItem(int position) {
        return journalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JournalHistoryAdapter.ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.journal_cell, parent, false);
            holder = new JournalHistoryAdapter.ViewHolder();
            holder.textDateJournalHistory = convertView.findViewById(R.id.textDateJournalHistory);
            holder.journalCellText = convertView.findViewById(R.id.journalCellText);
            holder.privacyJournal= convertView.findViewById(R.id.privacyJournal);
            holder.JournalCellCardView= convertView.findViewById(R.id.JournalCellCardView);
            convertView.setTag(holder);
        } else {
            holder = (JournalHistoryAdapter.ViewHolder) convertView.getTag();
        }

        holder.textDateJournalHistory.setText(journalList.get(position).getDate());
        holder.journalCellText.setText(journalList.get(position).getContent());
        if(journalList.get(position).isPrivate()){
            holder.privacyJournal.setImageResource(R.drawable.angry);
        }
        else{
            holder.privacyJournal.setImageResource(R.drawable.happy);
        }

        holder.JournalCellCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 onCardClickListener.onCardClicked(journalList.get(position).getContent(),journalList.get(position).getDate(),journalList.get(position).isPrivate());
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView textDateJournalHistory,journalCellText;
        ImageView privacyJournal;
        CardView JournalCellCardView;
    }

    public interface OnCardClickListener {
        void onCardClicked(String content,String date, boolean isPrivate);
    }

    public void setOnTextChangeListener(OnCardClickListener listener) {
        this.onCardClickListener = listener;
    }




}
