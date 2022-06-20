package com.omkar.varma.keepprivate.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.omkar.varma.keepprivate.R;

public class SplashScreen extends AppCompatActivity {

    LottieAnimationView animationView;
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        animationView = findViewById(R.id.animationView);

        setting = this.getSharedPreferences("appInfoKeepNotes",MODE_PRIVATE);
        boolean firstTime = setting.getBoolean("first_time",true);

        animationView.animate().translationY(3500).setDuration(1000).setStartDelay(5000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @SuppressLint("ApplySharedPref")
            @Override
            public void onAnimationEnd(Animator animation) {

                if(firstTime){
                    editor = setting.edit();
                    editor.putBoolean("first_time", false);
                    editor.commit();
                    Intent intent = new Intent(SplashScreen.this, On_Boarding_Screen_Intro.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
}