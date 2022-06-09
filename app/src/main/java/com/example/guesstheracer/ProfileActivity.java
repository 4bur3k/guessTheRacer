package com.example.guesstheracer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    String TAG = "ProfileActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User user;

        MyApplication mApp = com.example.guesstheracer.MyApplication.getInstance();
        user = mApp.getUser();
        Log.d(TAG, "user got" + user.getName() + " " + user.getScores() + " " + user.getTime_sec());

        TextView name, scbPosition, scbName, scbScores, scbTime;

        name = (TextView) findViewById(R.id.main_name);

        scbName = (TextView) findViewById(R.id.scoreboard_name);
        scbPosition = (TextView) findViewById(R.id.scoreboard_position);
        scbScores = (TextView) findViewById(R.id.scoreboard_scores);
        scbTime = (TextView) findViewById(R.id.scoreboard_time);

        name.setText(user.getName());
        scbPosition.setText("1");
        scbName.setText(user.getName());
        scbScores.setText(user.getScoresString());
        scbTime.setText(user.getTime_min());
    }
}