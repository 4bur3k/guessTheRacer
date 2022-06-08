package com.example.guesstheracer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "Registration";

    Button signUpButton;
    EditText loginLine;
    EditText passwordLine;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        signUpButton = (Button) findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginLine = (EditText) findViewById(R.id.login);
                passwordLine = (EditText) findViewById(R.id.password);

                String login = loginLine.getText().toString();
                String password = passwordLine.getText().toString();

                //if lines are not empty create an account
                if (!login.equals("") || !password.equals("")) {
                    createAccount(login, password);
                } else{ Log.d(TAG, "createUserWithEmail:failure [field is empty]"); }
            }
        });
    }


    private void createAccount(String email, String password){
        Log.d(TAG, "createAccount:" + email);

        //trying to create a new account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");

                            Intent intent = new Intent(RegistrationActivity.this, HomepageActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }
}