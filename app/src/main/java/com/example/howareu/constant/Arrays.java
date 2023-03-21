package com.example.howareu.constant;

import java.util.ArrayList;

public abstract class Arrays {


    public ArrayList<String> activitiesArrayList() {
        ArrayList<String> activitiesArrayList = new ArrayList<String>();

        return activitiesArrayList;
    }

    public static final ArrayList<String> todoArrayList() {
        ArrayList<String> todoArrayList = new ArrayList<String>();
        todoArrayList.add("Jog");
        todoArrayList.add("Cook");
        todoArrayList.add("Eat");
        todoArrayList.add("Sleep");
        todoArrayList.add("Bike");
        return todoArrayList;
    }
    public static final ArrayList<String> daysInWeek() {
        ArrayList<String> daysInWeek = new ArrayList<String>();
        daysInWeek.add("Su");
        daysInWeek.add("Mo");
        daysInWeek.add("Tu");
        daysInWeek.add("We");
        daysInWeek.add("Th");
        daysInWeek.add("Fr");
        daysInWeek.add("Sa");
        return daysInWeek;
    }


    public static final ArrayList<String> quotes() {
        ArrayList<String> quotes = new ArrayList<String>();
        quotes.add("Ride the wind bullsEye!");
        quotes.add("Yey its Friyay");
        quotes.add("Hey soul sister");
        return quotes;
    }

}
