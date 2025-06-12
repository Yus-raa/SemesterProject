package com.example.medicalmanagementsystem;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2500; // 2.5 seconds total time

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.splashLogo);

        // Fade-in animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(fadeIn);

        // After fade-in → fade-out → start MainActivity
        new Handler().postDelayed(() -> {
            Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
            logo.startAnimation(fadeOut);

            // After fade-out → start MainActivity
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // close splash
            }, 1000); // fade out duration
        }, 1500); // fade in duration
    }
}
