/*
Extracting data method (lines 75-86) working async
so I have to update UI inside onComplete method
otherwise it doesn't work(Extracting completes too late) :(
*/

package com.example.guesstheracer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    boolean[] rightAnswer = {false, false, false, false};
    int rateCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameframe);

        //getting reference on Firebase database to extract data
        mDatabase = FirebaseDatabase.getInstance("https://guess-the-racer-default-rtdb.europe-west1.firebasedatabase.app").getReference();

        imgView =  findViewById(R.id.game_img);
        firstButton =  findViewById(R.id.first_button);
        secondButton =  findViewById(R.id.second_button);
        thirdButton =  findViewById(R.id.third_button);
        fourthButton =  findViewById(R.id.fourth_button);

        getDataFromFirebase();

        //Start timer
        chronometer =  findViewById(R.id.chronometer);
        long startTime = SystemClock.elapsedRealtime();
        chronometer.setBase(startTime);
        chronometer.start();

        Log.d(TAG, "View created");
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Animation rightAnswerAnim = AnimationUtils.loadAnimation(GameframeActivity.this, R.anim.right_answer_anim);
        Animation wrongAnswerAnim = AnimationUtils.loadAnimation(GameframeActivity.this, R.anim.right_answer_anim);

        wrongAnswerAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                updateGameContent(list);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter != list.size()) {
                    if(rightAnswer[0]){
                        rightAnswer[0] = false;
                        rateCounter ++;

                        updateGameContent(list);
                    } else { firstButton.startAnimation(wrongAnswerAnim); }

                }
            }
        });

        secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter != list.size()) {
                    if(rightAnswer[1]){
                        rightAnswer[1] = false;
                        rateCounter ++;

                        updateGameContent(list);
                    } else { secondButton.startAnimation(wrongAnswerAnim); }

                }
            }
        });

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter != list.size()) {
                    if(rightAnswer[2]){
                        rightAnswer[2] = false;
                        rateCounter ++;

                        updateGameContent(list);
                    }  else { thirdButton.startAnimation(wrongAnswerAnim); }

                }
            }
        });

        fourthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter != list.size()) {
                    if(rightAnswer[3]){
                        rightAnswer[3] = false;
                        rateCounter ++;

                        updateGameContent(list);
                    } else { fourthButton.startAnimation(wrongAnswerAnim); }

                }
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG, "Scores:" + rateCounter);
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
        return list;
    }


    //Updating ImageView and Buttons[1,2,3,4] by racers pics and name
    private void updateGameContent(List<Map.Entry<String, String>> list) {
        setImage(list.get(counter).getValue());
        Log.d(TAG, "Data: " + list.get(counter).getKey() + " " + list.get(counter).getValue());
        Random rand = new Random();

        switch (rand.nextInt(5)) {
            case (1):
                updateButtonsAndImage(list);
                firstButton.setText(list.get(counter).getKey());
                rightAnswer[0] = true;
                break;
            case (2):
                updateButtonsAndImage(list);
                secondButton.setText(list.get(counter).getKey());
                rightAnswer[1] = true;
                break;
            case (3):
                updateButtonsAndImage(list);
                thirdButton.setText(list.get(counter).getKey());
                rightAnswer[2] = true;
                break;
            case (4):
                updateButtonsAndImage(list);
                fourthButton.setText(list.get(counter).getKey());
                rightAnswer[3] = true;
                break;
        }
        Log.d(TAG, "Answers " + rightAnswer[0] + " " +rightAnswer[1]+ " " +rightAnswer[2]+ " " + rightAnswer[3] );
        counter ++;
    }

    // Updating 4 Buttons by random Names from List<Map.Entry<String, String>> list
    private void updateButtonsAndImage(List<Map.Entry<String, String>> list){
        Random rand = new Random();
        int size = list.size();
        ArrayList<Integer> usedIndexes = new ArrayList<>();
        usedIndexes.add(counter);

        int index = rand.nextInt(size);

        if(!usedIndexes.contains(index)) { firstButton.setText(list.get(index).getKey()); usedIndexes.add(index);}
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
            usedIndexes.add(index);
        }
    }

    private void updateGameContentEnd(){
        chronometer.stop();
        setImage("start_flag");
        firstButton.setText("");
        secondButton.setText("");
        thirdButton.setText("");
        fourthButton.setText("");
    }

}

