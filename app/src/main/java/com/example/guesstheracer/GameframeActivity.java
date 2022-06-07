/*
Extracting data method (lines 75-86) working async
so I have to update UI inside onComplete method
otherwise it doesn't work(Extracting completes too late) :(
*/

package com.example.guesstheracer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.OverloadResolutionByLambdaReturnType;

public class GameframeActivity extends AppCompatActivity {

    String TAG = "GameFrameActivity";
    DatabaseReference mDatabase;
    ImageView imgView;
    Button firstButton;
    Chronometer chronometer;
    //It's a map contains list of racers [key:name, value: picture name]
    //why don't work if res is local variable(asking to make it final)?
    Map<String, String> res = new HashMap<>();
    List<Map.Entry<String, String>> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameframe);

        //getting reference on Firebase database to extract data
        mDatabase = FirebaseDatabase.getInstance("https://guess-the-racer-default-rtdb.europe-west1.firebasedatabase.app").getReference();

        imgView = (ImageView) findViewById(R.id.game_img);
        firstButton = (Button) findViewById(R.id.first_button);

        getDataFromFirebase();

        //Start timer
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        long startTime = SystemClock.elapsedRealtime();
        chronometer.setBase(startTime);
        chronometer.start();

        Log.d(TAG, "View created");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "View started");


        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!list.isEmpty()) {
                    updateGameContent(list);
                } else { chronometer.stop(); setImage("start_flag");}
            }
        });
    }


    //should returns Map<String, String> contains Names and pic names
    private void getDataFromFirebase() {

        mDatabase.child("f1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    res = (HashMap<String, String>) task.getResult().getValue();
                    Log.d(TAG, "Successful get:" + res);

                    list = randomizeData(res);
                    updateGameContent(list);
                } else {
                    Log.e(TAG, "Error getting data", task.getException());
                }
            }
        });
    }


    //setting image on main ImageView
    private void setImage(String name) {
        try {
            imgView.setImageDrawable(getDrawableFromAssets("f1/"+ name +".jpg"));
            imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Log.d(TAG, "image was set:" + name);
        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
        }
    }

    //making drawable from asset picture
    private Drawable getDrawableFromAssets(String path) throws IOException {
        return Drawable.createFromStream(getAssets().open(path), null);
    }


    //returns shuffled list of hashmap elements [List<Map.Entry<String, String>>]
    private List<Map.Entry<String, String>> randomizeData(Map<String, String> map) {
        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(map.entrySet());

        Collections.shuffle(list);
        Log.d(TAG, "shuffled data:" + list);
        return list;
    }


    //Updating ImageView and Buttons[1,2,3,4] by racers pics and name
    private void updateGameContent(List<Map.Entry<String, String>> list) {
        setImage(list.remove(0).getValue());

        Log.d(TAG, "interface updated");
    }

}

