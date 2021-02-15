package com.vasu.localchat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.vasu.localchat.MainActivity;
import com.vasu.localchat.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new CountDownTimer(1500,500) {
            @Override
            public void onTick(long l) {

            }
            @Override
            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        }.start();
    }
}