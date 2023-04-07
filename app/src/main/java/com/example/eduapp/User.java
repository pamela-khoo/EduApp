package com.example.eduapp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    String username;
    String email;
    String dob;
    String uid;
    String url;
    int score;
    String lastStreakDay;
    String consecutiveStreakDays;
    ArrayList<String> lessonsCompleted;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setScore(int score) {this.score = score;}

    public void setlastStreakDay(String lastStreakDay) {
        this.lastStreakDay = lastStreakDay;
    }

    public void setconsecutiveStreakDays(String consecutiveStreakDays) { this.consecutiveStreakDays = consecutiveStreakDays; }

    public void setLessonsCompleted(ArrayList<String> lessonsCompleted) {this.lessonsCompleted = lessonsCompleted; }

    public User() {

    }
}
