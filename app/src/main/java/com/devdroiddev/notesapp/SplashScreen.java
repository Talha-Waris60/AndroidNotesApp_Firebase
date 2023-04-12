package com.devdroiddev.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ImageView notesImage;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        notesImage = findViewById(R.id.notesImage);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.custom_anim);
        notesImage.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the next activity
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish(); // Call finish() to close the splash activity
            }
        }, 6000);
    }
}
