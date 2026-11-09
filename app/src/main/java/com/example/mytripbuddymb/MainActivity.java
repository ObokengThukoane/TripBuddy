package com.example.mytripbuddymb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mytripbuddymb.activities.CreateMemory;
import com.example.mytripbuddymb.activities.myPlanTrip;
import com.example.mytripbuddymb.activities.ViewGallery;
import com.example.mytripbuddymb.activities.SettingsActivity;
import com.example.mytripbuddymb.utils.SharedPreferencesManager;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Apply saved theme before inflating views
        SharedPreferencesManager spm = new SharedPreferencesManager(this);
        String theme = spm.getThemeMode();
        if ("dark".equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button VWBTN = findViewById(R.id.VWBTN);
        VWBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent= new Intent(MainActivity.this, ViewGallery.class);
             startActivity(intent);
            }
        });

       Button PLNBTN = findViewById(R.id.PLNBTN);
        PLNBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, myPlanTrip.class);
                startActivity(intent);
            }
        });

      Button  CreateMemoryButton = findViewById(R.id.CreateMemoryButton);
        CreateMemoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateMemory.class);
                startActivity(intent);
            }
        });


        Button settingsButton = findViewById(R.id.Settingsbutton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}