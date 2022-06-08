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

    private static final String TAG = "Authorization";

    EditText loginLine;
    EditText passwordLine;

    //mAuth is Firebase object that responsible for authorization
    FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();

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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        //start of authorization initialization
        mAuth = FirebaseAuth.getInstance();


        //lines tha keeps email and password
        loginLine = (EditText) findViewById(R.id.login);
        passwordLine = (EditText) findViewById(R.id.password);


        //signInButton
        Button signInButton = (Button) findViewById(R.id.signin_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = loginLine.getText().toString();
                String password = passwordLine.getText().toString();

                if (!login.equals("") || !password.equals("")) {
                    signIn(login.toString(), password.toString());
                    Log.d(TAG, "signed in:" + login.toString());
                } else {
                    Log.d(TAG, "signing in error: field is empty");
                }
            }
        });
    }


    private void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //signed in, updating ui

                            Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                });
    }
}
