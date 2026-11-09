package com.example.mytripbuddymb;

import android.content.Intent;
import com.example.mytripbuddymb.activities.LoginActivity;
import com.example.mytripbuddymb.utils.SharedPreferencesManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class Splashlogo extends AppCompatActivity {

    private static final long SPLASH_DELAY = 4500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashlogo);


        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferencesManager spm = new SharedPreferencesManager(this);
            Intent intent;
            if (spm.isLoggedIn() && !spm.isSessionExpired()) {
                intent = new Intent(Splashlogo.this, MainActivity.class);
            } else {
                intent = new Intent(Splashlogo.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}

