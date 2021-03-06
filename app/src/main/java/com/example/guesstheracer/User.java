package com.example.guesstheracer;

import java.util.HashMap;
import java.util.Map;

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

    User(String name, String userID){
        this.name = name;
        this.userID = userID;
        scores = 0;
        time = 0;
    }

    public String getName() {
        return name;
    }

    public long getTime_ms() {
        return time * 1000L;
    }
    public int getTime_sec() {
        return time;
    }
    public String getTime_min(){return ("" + ((int)time / 60) + "." + time);}


    public int getScores() {
        return scores;
    }

    public String getScoresString() {
        return "" + scores;
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

    //Converting User user to Map to upload data in database
    public Map<String, Object>  toMap(){
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", name);
        result.put("scores", scores);
        result.put("time", time);

        return result;
    }
}


