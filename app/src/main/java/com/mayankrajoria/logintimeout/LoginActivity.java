package com.mayankrajoria.logintimeout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button loginButton;
    EditText useridET, passET;
    final String USER_ID = "user";
    final String PASSWORD = "pass";
    final String MyPREFERENCES = "com.mayankrajoria.loginstate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // If user is already loggedin, go to UserActivity
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String loggedinState = sharedpreferences.getString("loggedin", "no");
        Log.d("LA", "Checking state on create: " + loggedinState);
        if(loggedinState.equals("yes")) {
            if(checkSession()) {
                Intent it = new Intent(this, UserActivity.class);
                startActivity(it);
                finish();
            }
        }

        loginButton = (Button) findViewById(R.id.login_button);
        useridET = (EditText) findViewById(R.id.userid_et);
        passET = (EditText) findViewById(R.id.pass_et);
        loginButton.setOnClickListener(this);
    }

    // Check if the previous loggedin session has expired.
    // This is done by checking if more than predefined time has
    // elapsed since last login.
    boolean checkSession() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String loggedinState = sharedpreferences.getString("loggedin", "no");
        if(loggedinState.equals("yes")) {
            long startTime = sharedpreferences.getLong("starttime", 0);
            long millis = System.currentTimeMillis() - startTime;
            if(millis/1000 > 20) {
                Toast.makeText(this, "Your previous session has expired", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("loggedin", "no");
                editor.putLong("starttime", 0);
                editor.commit();
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        // If user is already loggedin, go to UserActivity
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String loggedinState = sharedpreferences.getString("loggedin", "no");
        Log.d("LA", "Checking state on resume: " + loggedinState);
        if(loggedinState.equals("yes")) {
            if(checkSession()) {
                Intent it = new Intent(this, UserActivity.class);
                startActivity(it);
                finish();
            }
        }
    }

    // Check if the user input is correct
    boolean validateInput() {
        String useridString = useridET.getText().toString();
        if(useridString.equals("")) {
            Toast.makeText(this, "Enter a Userid", Toast.LENGTH_SHORT).show();
            return false;
        }

        String passwordString = passET.getText().toString();
        if(passwordString.equals("")) {
            Toast.makeText(this, "Enter a password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!useridString.equals(USER_ID)) {
            Toast.makeText(this, "Invalid Userid", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!passwordString.equals(PASSWORD)) {
            Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
            return false;
        }

        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                // If user input is correct, mark user as logged in,
                // move to the UserActivity and finish() this activity.
                if(validateInput()) {
                    SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("loggedin", "yes");
                    editor.commit();
                    Intent it = new Intent(this, UserActivity.class);
                    startActivity(it);
                    finish();
                }
        }
    }
}
