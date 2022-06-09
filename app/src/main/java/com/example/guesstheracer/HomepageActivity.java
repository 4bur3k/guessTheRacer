package com.example.guesstheracer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomepageActivity extends AppCompatActivity {

    //Buttons that start game
    Button f1Button, nascarButton, motogpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        f1Button = (Button) findViewById(R.id.f1_button);
        f1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame("f1");

            }
        });

        nascarButton = (Button) findViewById(R.id.nascar_button);
        nascarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame("nascar");

            }
        });

        motogpButton = (Button) findViewById(R.id.motogp_button);
        motogpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame("motogp");

            }
        });
    }

    //Starts gameframe activity with package from argument
    private void startGame(String packageName){
        Intent intent = new Intent(HomepageActivity.this, GameframeActivity.class);
        intent.putExtra(packageName, packageName);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}