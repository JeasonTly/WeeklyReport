package com.aorise.weeklyreport;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aorise.weeklyreport.activity.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        WRApplication.getInstance().addActivity(this);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mIntent = new Intent();
                mIntent.setClass(SplashActivity.this, LoginActivity.class);
                startActivity(mIntent);
                finish();
            }
        }, 1000);

    }
}
