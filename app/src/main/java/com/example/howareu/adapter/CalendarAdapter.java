package com.example.howareu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.howareu.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CalendarAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Date> mDates;
    private HashMap<Date, Integer> mBadgeMap;
    private LayoutInflater mInflater;

    public CalendarAdapter(Context context, ArrayList<Date> dates, HashMap<Date, Integer> badgeMap) {
        mContext = context;
        mDates = dates;
        mBadgeMap = badgeMap;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDates.size();
    }

    @Override
    public Object getItem(int position) {
        return mDates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.date_calendar, parent, false);
            holder = new ViewHolder();
            holder.dateTextView = convertView.findViewById(R.id.calendarCellText);
            holder.badgeImageView = convertView.findViewById(R.id.calendarCellImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(mDates.get(position)!= null) {
            Date date = mDates.get(position);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            if (!(position < 7 && dayOfMonth > 10)) {
                holder.dateTextView.setText(String.valueOf(dayOfMonth));
            } else {
                holder.dateTextView.setText(" ");
            }
            Integer badgeId = mBadgeMap.get(date);
            if (badgeId != null) {
                holder.badgeImageView.setImageResource(badgeId);
                holder.badgeImageView.setVisibility(View.VISIBLE);
            }
        }
        else{
            holder.dateTextView.setText(" ");
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView dateTextView;

        ImageView badgeImageView;
    }

}
