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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Authorization";

    EditText loginLine;
    EditText passwordLine;

    //mAuth is Firebase object that responsible for authorization
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    Map<String, Object> userData;

    User user;

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Authorization and database initialization
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://guess-the-racer-default-rtdb.europe-west1.firebasedatabase.app").getReference();

        //User keeps data about current user in RAM
        user = new User();
        //UserData using to take data from database
        userData = new HashMap<>();

        setContentView(R.layout.activity_login);

        //SignUp Button
        Button signUpButton = (Button) findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                intent.putExtra("user", "user");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        //Lines tha keeps email and password
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
                            FirebaseUser FBuser = mAuth.getCurrentUser();

                            //Setting ID to user
                            user.setUserID(FBuser.getUid());

                            //Getting data from Firebase about current user
                            mDatabase.child("users").child(user.getUserID()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        userData = (HashMap<String, Object>) task.getResult().getValue();

                                        //Converting data from Map<String, Object> to User type
                                        user.setName((String) userData.get("name"));
                                        user.setScores((int) ((long) userData.get("scores")));
                                        if (userData.containsKey("time")) {
                                            user.setTime_sec((int) ((long) userData.get("time")));
                                        }

                                        //I'm keeping current user as a global variable to not upload data from data base
                                        MyApplication mApp = com.example.guesstheracer.MyApplication.getInstance();
                                        mApp.setUser(user);
                                        Log.d(TAG, "User saved in RAM: " + userData);
                                    } else {
                                        Log.e(TAG, "Error getting data", task.getException());
                                    }
                                }
                            });

                            Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            Log.d(TAG, "signed in:" + email.toString());
                        }
                    }
                });
    }
}
