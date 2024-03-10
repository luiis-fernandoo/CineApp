package com.example.cineapp.Activities.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.cineapp.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imageView = findViewById(R.id.splash_img);
        Glide.with(this).asGif().load(R.raw.officine).into(imageView);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
                String savedEmail = sp.getString("email", "");

                if (!savedEmail.isEmpty()) {
                    Intent intent = new Intent(SplashActivity.this, MainActivityMenu.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent it = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(it);
                }
            }
        }, 3000);
    }
}