package com.example.guesstheracer;

public class User {
    private String userID;
    private String name;
    private int scores;
    private int time;

    User(){
        this.userID = null;
        this.name = null;
        this.scores = 0;
        this.time = 0;
    }

    User(String name, String UserID){
        this.name = name;
        scores = 0;
        time = 0;
    }

    public String getName() {
        return name;
    }

    public long getTime_ms() {
        return time;
    }
    public int getTime_sec() {
        return time;
    }

    public int getScores() {
        return scores;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScores(int scores){
        this.scores = scores;
    }

    public void setTime_ms(long time) {
        this.time = (int) time / 1000;
    }
    public void setTime_sec(int time) {
        this.time = time;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}


