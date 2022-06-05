package com.example.guesstheracer;


import java.sql.Time;

public class Person {
    private String name;
    private int rate;
    private Time time;

    public String getName() {
        return name;
    }

    public Time getTime() {
        return time;
    }

    public int getRate() {
        return rate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRate(int rate){
        this.rate = rate;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}

