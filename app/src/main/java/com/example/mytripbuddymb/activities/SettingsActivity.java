package com.example.mytripbuddymb.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.example.mytripbuddymb.R;
import com.example.mytripbuddymb.utils.SharedPreferencesManager;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchTheme, switchBackgroundMusic;
    private View tvLogout;
    private SharedPreferencesManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        prefsManager = new SharedPreferencesManager(this);

        initializeViews();
        setupToolbar();
        loadCurrentSettings();
        setupClickListeners();
    }

    @SuppressLint("WrongViewCast")
    private void initializeViews() {
        switchTheme = findViewById(R.id.switch_theme);
        switchBackgroundMusic = findViewById(R.id.switch_background_music);
        tvLogout = findViewById(R.id.tv_logout);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadCurrentSettings() {
        // Load theme setting
        String currentTheme = prefsManager.getThemeMode();
        switchTheme.setChecked("dark".equals(currentTheme));

        // Load background music setting
        switchBackgroundMusic.setChecked(prefsManager.isBackgroundMusicEnabled());

        // Language row not present in this layout; skipping
    }

    private void setupClickListeners() {
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String themeMode = isChecked ? "dark" : "light";
            prefsManager.setThemeMode(themeMode);
            applyTheme(themeMode);
        });

        switchBackgroundMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefsManager.setBackgroundMusicEnabled(isChecked);
        });

        tvLogout.setOnClickListener(v -> logout());
    }

    private void applyTheme(String themeMode) {
        if ("dark".equals(themeMode)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void showLanguageDialog() {
        String[] languages = {"English", "Spanish", "French", "German"};
        String[] languageCodes = {"en", "es", "fr", "de"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Select Language")
                .setItems(languages, (dialog, which) -> {
                    String selectedLanguage = languageCodes[which];
                    prefsManager.setLanguage(selectedLanguage);
                    android.widget.Toast.makeText(this,
                            "Language changed to " + languages[which],
                            android.widget.Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void showAboutDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("About TripBuddy")
                .setMessage("TripBuddy v1.0\n\n" +
                        "Your personal travel companion that helps you:\n" +
                        "• Plan and organize trips\n" +
                        "• Create and share memories\n" +
                        "• Track expenses and budgets\n" +
                        "• Earn loyalty rewards\n\n" +
                        "Developed for Deliverable 5")
                .setPositiveButton("OK", null)
                .show();
    }

    private void logout() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    prefsManager.clearUserSession();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private String getLanguageDisplayName(String languageCode) {
        switch (languageCode) {
            case "es": return "Spanish";
            case "fr": return "French";
            case "de": return "German";
            default: return "English";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

