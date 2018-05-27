package com.mayankrajoria.logintimeout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {
    TextView timerTextView;
    long startTime = 0;
    final String MyPREFERENCES = "com.mayankrajoria.loginstate";

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            Log.d("UA", "startime in runnable: " + startTime);
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            // Log out the user is more than 20 have elapsed
            // since login.
            if(seconds > 20) {
                Log.d("UA", "Logging out on timer end");
                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("loggedin", "no");
                editor.putLong("starttime", 0);
                editor.commit();

                timerHandler.removeCallbacks(timerRunnable);
                Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                Toast.makeText(getApplicationContext(), "Session timed out", Toast.LENGTH_SHORT).show();
                startActivity(it);
                finish();
                return;
            }

            Log.d("UA", "Setting time in runnable");
            timerTextView.setText(String.format("%d:%02d", minutes, seconds));
//            timerTextView.setText("" + millis);

            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        timerTextView = (TextView) findViewById(R.id.timer_TV);

        // If a start time exists in SharedPreferences, read it
        // else set current time as start time.
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(sharedpreferences.getLong("starttime", 0) != 0) {
            startTime = sharedpreferences.getLong("starttime", 0);
            return;
        }
        startTime = System.currentTimeMillis();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Log.d("UA", "Setting start time in SP");
        editor.putLong("starttime", startTime);
        editor.commit();

        // Start the runnable to keep track of the user's session
        timerHandler.postDelayed(timerRunnable, 0);
    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        long st = sharedpreferences.getLong("starttime", 0);
        Log.d("UA", "Checking state on resume in user activity: " + st);
        if(st != 0) {
            timerHandler.removeCallbacks(timerRunnable);
            timerHandler.postDelayed(timerRunnable, 0);
            startTime = sharedpreferences.getLong("starttime", 0);
            return;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }
}