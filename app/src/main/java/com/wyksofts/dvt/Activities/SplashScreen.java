package com.wyksofts.dvt.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.wyksofts.dvt.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        openMainActivity();
    }

    void openMainActivity() {
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(1300);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.start();
    }
}