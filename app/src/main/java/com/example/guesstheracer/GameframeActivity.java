package com.example.guesstheracer;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;

public class GameframeActivity extends AppCompatActivity {

    FirebaseDatabase db;

    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameframe);

        db = FirebaseDatabase.getInstance();

        imgView = (ImageView) findViewById(R.id.game_img);


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

   /* private String getValue(FirebaseDatabase _db, String key){
        DatabaseReference ref = db.getReference(key);
    }
*/

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