package com.kelompokc.tubes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashSreenActivity extends AppCompatActivity
{
    private static int SPLASH_ANIMATION = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sreen);
        Animation splashAnim = AnimationUtils.loadAnimation(this, R.anim.animasi);
        ImageView imageView = findViewById(R.id.splash_logo);
        imageView.setAnimation(splashAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashSreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_ANIMATION);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animasi);
    }
}