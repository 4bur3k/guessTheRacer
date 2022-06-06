package com.example.guesstheracer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Chronometer;

public class GameframeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameframe);

        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        long startTime = SystemClock.elapsedRealtime();
        chronometer.setBase(startTime);
        chronometer.start();

    }
}