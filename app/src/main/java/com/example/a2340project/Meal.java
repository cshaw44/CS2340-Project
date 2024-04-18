package com.example.a2340project;

import java.util.Date;

public class Meal {
    private String name;
    private int calories;
    private Date date;

    // Default constructor required for calls to DataSnapshot.getValue(Meal.class)
    public Meal() {

    }

    public Meal(String name, int calories) {
        this.name = name;
        this.calories = calories;
        date = new Date(System.currentTimeMillis()); // Sets the date to the current date
    }

    // Getters
    public String getName() {
        return name;
    }
    public int getCalories() {
        return calories;
    }
    public Date getDate() {
        return date;
    }
}
