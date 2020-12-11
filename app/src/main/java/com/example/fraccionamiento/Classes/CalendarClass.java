package com.example.fraccionamiento.Classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CalendarClass {
    private String title;
    private String description;
    private String hour1;
    private String hour2;
    private String minute1;
    private String day;
    private String month;
    private String year;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String hourSystem;
    private String key;

    public CalendarClass() {
    }

    public CalendarClass(String title, String description, String hour1, String hour2, String minute1, String day, String month, String year, String hourSystem) {
        this.title = title;
        this.description = description;
        this.hour1 = hour1;
        this.hour2 = hour2;
        this.minute1 = minute1;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hourSystem = hourSystem;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHour1() {
        return hour1;
    }

    public void setHour1(String hour1) {
        this.hour1 = hour1;
    }

    public String getHour2() {
        return hour2;
    }

    public void setHour2(String hour2) {
        this.hour2 = hour2;
    }

    public String getMinute1() {
        return minute1;
    }

    public void setMinute1(String minute1) {
        this.minute1 = minute1;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getHourSystem() {
        return hourSystem;
    }

    public void setHourSystem(String hourSystem) {
        this.hourSystem = hourSystem;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("description", description);
        result.put("hour1",hour1);
        result.put("hour2", hour2);
        result.put("minute1", minute1);
        result.put("day", day);
        result.put("month", month);
        result.put("year", year);
        result.put("hourSystem", hourSystem);
        return result;
    }
}
