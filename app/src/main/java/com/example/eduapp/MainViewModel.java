package com.example.eduapp;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private String test;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
