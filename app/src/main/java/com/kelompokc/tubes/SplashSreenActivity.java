package com.kelompokc.tubes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class SplashSreenActivity extends AppCompatActivity
{
    private static int SPLASH_ANIMATION = 3000;
    private boolean aBoolean;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        sharedPreferences  = getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        if(sharedPreferences!=null)
        {
            aBoolean = sharedPreferences.getBoolean("switch1", false);
            if(aBoolean)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                getApplicationContext().setTheme(R.style.darkTheme);
            }
            else
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                getApplicationContext().setTheme(R.style.AppTheme);
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sreen);
        Animation splashAnim = AnimationUtils.loadAnimation(this, R.anim.animasi);
        ImageView imageView = findViewById(R.id.imageView2);
        imageView.setAnimation(splashAnim);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(SplashSreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_ANIMATION);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animasi);
    }
}