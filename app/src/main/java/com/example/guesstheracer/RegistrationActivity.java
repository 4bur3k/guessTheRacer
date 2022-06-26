package com.example.guesstheracer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "Registration";

    Button signUpButton;
    EditText nameLine, loginLine, passwordLine;
    ImageButton uploadUserPhoto;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://guess-the-racer-default-rtdb.europe-west1.firebasedatabase.app").getReference();


        signUpButton = (Button) findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameLine = findViewById(R.id.name);
                loginLine = (EditText) findViewById(R.id.login);
                passwordLine = (EditText) findViewById(R.id.password);

                String name = nameLine.getText().toString();
                String login = loginLine.getText().toString();
                String password = passwordLine.getText().toString();

                //if lines are not empty create an account
                if (!login.equals("") || !password.equals("") || !name.equals("")) {
                    createAccount(login, password, name);
                } else{ Log.d(TAG, "createUserWithEmail:failure [field is empty]"); }
            }
        });

    }


    private void createAccount(String email, String password, String name){
        Log.d(TAG, "createAccount:" + email);

        //trying to create a new account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");

                            //creating a new user in DB
                            FirebaseUser FBuser = mAuth.getCurrentUser();

                            while(FBuser == null){
                                FBuser = mAuth.getCurrentUser();
                            }

                            User user = new User(name, FBuser.getUid());
                            //i'm keeping current user as a global variable to not upload data from data base
                            MyApplication mApp = com.example.guesstheracer.MyApplication.getInstance();
                            mApp.setUser(user);

                            addUserToDataBase(user, mDatabase);

                            Intent intent = new Intent(RegistrationActivity.this, HomepageActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    private void addUserToDataBase(User user){
        Log.d(TAG, "UID: " + user.getUserID());
        mDatabase.child("users").child(user.getUserID()).setValue(user.toMap());
    }
}
