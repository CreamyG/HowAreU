package com.example.howareu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.howareu.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarAdapter extends BaseAdapter {

    private Context mContext;
    private List<Date> mDates;
    private LayoutInflater mInflater;

    public CalendarAdapter(Context context, List<Date> dates) {
        mContext = context;
        mDates = dates;
        mInflater = LayoutInflater.from(mContext);
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
            holder.imageView = convertView.findViewById(R.id.calendarCellImage);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Date date = mDates.get(position);
        holder.dateTextView.setText(String.valueOf(date.getDate()));
        holder.imageView.setImageResource(R.drawable.happy);

        return convertView;
    }

    private static class ViewHolder {
        TextView dateTextView;
        ImageView imageView;
    }

}
