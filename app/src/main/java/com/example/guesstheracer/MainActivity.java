package com.example.guesstheracer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText loginLine;
    EditText passwordLine;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //SignUp Button
        Button signUpButton = (Button) findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });


        //start of authorization initialization
        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    //user is signed in
                    Log.d("123456", "onAuthStateChanged:signed_in: " + user.getUid());
                } else {
                    Log.d("123456", "onAuthStateChanged:signed_out");
                }
            }
        };

        //lines tha keeps email and password
         loginLine = (EditText) findViewById(R.id.login);
         passwordLine = (EditText) findViewById(R.id.password);

         //signInButton
        Button signInButton = (Button) findViewById(R.id.signin_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable login = loginLine.getText();
                Editable password = passwordLine.getText();

                if(login != null || password != null){
                    signIn(login.toString(), password.toString());
                }
        }
    });
    }



    private void signIn(String email, String password){

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //signed in, updating ui
                                FirebaseUser user = mAuth.getCurrentUser();

                                Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
        }
    }
