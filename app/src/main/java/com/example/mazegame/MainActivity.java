package com.example.mazegame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    CountDownTimer cTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startTimer();

    }


    //start timer function
    void startTimer() {
        this.cTimer = new CountDownTimer(60000, 1000) {
            TextView timer = (TextView) findViewById(R.id.timer);
            public void onTick(long millisUntilFinished) {
                timer.setText("Time left: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                timer.setText("LEVEL OVER");
            }
        };
        cTimer.start();
    }


    protected void onDestroy() {
        super.onDestroy();
        cTimer.cancel();
    }

}




