package com.example.howareu.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.howareu.R;
import com.example.howareu.constant.Arrays;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CalendarAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Date> mDates;
    private HashMap<Date, Integer> mBadgeMap;
    private LayoutInflater mInflater;
    private ArrayList<String> daysInWeek;

    public CalendarAdapter(Context context, ArrayList<Date> dates, HashMap<Date, Integer> badgeMap) {
        mContext = context;
        mDates = dates;
        mBadgeMap = badgeMap;
        mInflater = LayoutInflater.from(context);
        this.daysInWeek = Arrays.daysInWeek();
    }

    @Override
    public int getCount() {
        return mDates.size()+6;
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
        int realPosition = position - 7;


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.date_calendar, parent, false);
            holder = new ViewHolder();
            holder.dateTextView = convertView.findViewById(R.id.calendarCellText);
            holder.badgeImageView = convertView.findViewById(R.id.calendarCellImage);
            holder.calendarCellLinearLayout= convertView.findViewById(R.id.calendarCellLinearLayout);
            holder.CalendarCell = convertView.findViewById(R.id.CalendarCell);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position<7){
            holder.calendarCellLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
            holder.badgeImageView.setVisibility(View.GONE);
            holder.dateTextView.setText(daysInWeek.get(position));

        }
        else {
            if(position<mDates.size()) {

                if (mDates.get(realPosition) != null) {
                    Date date = mDates.get(realPosition);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                    if (!(realPosition < 7 && dayOfMonth > 10)) {
                        holder.dateTextView.setText(String.valueOf(dayOfMonth));
                    } else {
                        holder.dateTextView.setText(" ");

                    }
                    Integer badgeId = mBadgeMap.get(date);
                    if (badgeId != null) {
                        holder.badgeImageView.setImageResource(badgeId);
                        holder.badgeImageView.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.badgeImageView.setVisibility(View.GONE);
                    }

                }
            }

            if(position>41 && mDates.get(42-7) == null){
                holder.CalendarCell.setVisibility(View.GONE);
            }

        }
        return convertView;
    }

    private static class ViewHolder {
        CardView CalendarCell;
        TextView dateTextView;
        LinearLayout calendarCellLinearLayout;
        ImageView badgeImageView;
    }

}
