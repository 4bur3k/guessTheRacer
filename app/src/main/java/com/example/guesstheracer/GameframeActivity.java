/*
Extracting data method (lines 75-86) working async
so I have to update UI inside onComplete method
otherwise it doesn't work(Extracting completes too late) :(
*/

package com.example.guesstheracer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameframeActivity extends AppCompatActivity {

    String TAG = "GameFrameActivity";
    DatabaseReference mDatabase;
    ImageView imgView;
    Button firstButton, secondButton, thirdButton, fourthButton;
    Chronometer chronometer;
    //It's a map contains list of racers [key:name, value: picture name]
    //why don't work if res is local variable(asking to make it final)?
    Map<String, String> res = new HashMap<>();
    List<Map.Entry<String, String>> list;
    int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameframe);

        //getting reference on Firebase database to extract data
        mDatabase = FirebaseDatabase.getInstance("https://guess-the-racer-default-rtdb.europe-west1.firebasedatabase.app").getReference();

        imgView = (ImageView) findViewById(R.id.game_img);
        firstButton = (Button) findViewById(R.id.first_button);
        secondButton = (Button) findViewById(R.id.second_button);
        thirdButton = (Button) findViewById(R.id.third_button);
        fourthButton = (Button) findViewById(R.id.fourth_button);

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
                if(counter != list.size()) {
                    updateGameContent(list);
                } else {
                    chronometer.stop();
                    setImage("start_flag");
                    firstButton.setText("");
                    secondButton.setText("");
                    thirdButton.setText("");
                    fourthButton.setText("");
                }
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
        setImage(list.get(counter).getValue());
        Random rand = new Random();
        int size = list.size();

        switch (rand.nextInt(5)) {
            case (1):
                updateButtons(list);
                firstButton.setText(list.get(counter).getKey());
                break;
            case (2):
                updateButtons(list);
                secondButton.setText(list.get(counter).getKey());
                break;
            case (3):
                updateButtons(list);
                thirdButton.setText(list.get(counter).getKey());
                break;
            case (4):
                updateButtons(list);
                fourthButton.setText(list.get(counter).getKey());
                break;
        }

        Log.d(TAG, "interface updated");
        counter ++;
    }

    // Updating 4 Buttons by random Names from List<Map.Entry<String, String>> list
    private void updateButtons(List<Map.Entry<String, String>> list){
        Random rand = new Random();
        int size = list.size();
        ArrayList<Integer> usedIndexes = new ArrayList<>();
        usedIndexes.add(counter);

        int index = rand.nextInt(size);

        if(!usedIndexes.contains(index)) { firstButton.setText(list.get(index).getKey()); usedIndexes.add(index); }
        else { while(usedIndexes.contains(index)){
                index = rand.nextInt(size);
            }

            firstButton.setText(list.get(index).getKey());
            usedIndexes.add(index);
        }

        index = rand.nextInt(size);
        if(!usedIndexes.contains(index)){ secondButton.setText(list.get(index).getKey()); usedIndexes.add(index); }
        else { while(usedIndexes.contains(index)){
            index = rand.nextInt(size);
        }

            secondButton.setText(list.get(index).getKey());
            usedIndexes.add(index);
        }

        index = rand.nextInt(size);
        if(!usedIndexes.contains(index)){ thirdButton.setText(list.get(index).getKey()); usedIndexes.add(index);}
        else { while(usedIndexes.contains(index)){
            index = rand.nextInt(size);
        }

            thirdButton.setText(list.get(index).getKey());
            usedIndexes.add(index);
        }

        index = rand.nextInt(size);
        if(!usedIndexes.contains(index)){ fourthButton.setText(list.get(index).getKey()); usedIndexes.add(index);}
        else { while(usedIndexes.contains(index)){
            index = rand.nextInt(size);
        }

            fourthButton.setText(list.get(index).getKey());
        }
    }

}

