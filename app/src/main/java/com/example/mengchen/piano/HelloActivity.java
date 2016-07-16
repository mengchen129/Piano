package com.example.mengchen.piano;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mengchen.piano.utils.SysApplication;

public class HelloActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        SysApplication.getInstance().addActivity(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(HelloActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 1000);
    }
}
