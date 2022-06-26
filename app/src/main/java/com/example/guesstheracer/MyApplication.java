package com.example.guesstheracer;

import android.app.Application;

//Class keeps global variables
public class MyApplication{
    User user;

    private static final MyApplication Instance = new MyApplication();
    public static MyApplication getInstance(){
        return Instance;
    }

    public User getUser(){ return user; }
    public void setUser(User user){ this.user = user; }
}
