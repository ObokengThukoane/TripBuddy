package com.example.mytripbuddymb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class Splashscreen extends AppCompatActivity {

    private static final long SPLASH_DELAY = 7000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Delay for splash screen display
        boolean b = new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Navigate to main activity
            Intent intent = new Intent(Splashscreen.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close splash activity
        }, SPLASH_DELAY);
    }
}

