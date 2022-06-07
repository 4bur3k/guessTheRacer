package com.example.guesstheracer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class GameframeActivity extends AppCompatActivity {

    String TAG = "Firebase1";
    Pair pair;
    DatabaseReference mDatabase;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameframe);

        //getting reference on Firebase database to extract data
        mDatabase = FirebaseDatabase.getInstance("https://guess-the-racer-default-rtdb.europe-west1.firebasedatabase.app").getReference();

        imgView = (ImageView) findViewById(R.id.game_img);

        getDataFromFirebase();

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Start timer
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        long startTime = SystemClock.elapsedRealtime();
        chronometer.setBase(startTime);
        chronometer.start();
    }

    @Override
    protected void onRestart(){ //!!!!MAYBE U SHOULD USE OTHER WAY?
        super.onRestart();

    }

    //why don't work if res is local variable(asking to make it final)?
    Map<String, String> res;

    //returns Map<String, String> contains Names and pic names
    private Map<String, String> getDataFromFirebase(){

        mDatabase.child("f1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    res = (Map<String, String>) task.getResult().getValue();
                    Log.d(TAG, "Successful get:" + res);
                } else { Log.e(TAG, "Error getting data", task.getException());}
            }
        });

        if(!res.isEmpty()) {
            return res;
        } else {return null; }
    }


    private void setImage(String name){
        try {
            imgView.setImageDrawable(getDrawableFromAssets("f1/"+ name +".jpg"));
            imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Drawable getDrawableFromAssets(String path) throws IOException {
        return Drawable.createFromStream(getAssets().open(path), null);
    }
}

class Pair{
    String key;
    String value;

    Pair(String key, String value){
        this.key = key;
        this.value = value;
    }
}