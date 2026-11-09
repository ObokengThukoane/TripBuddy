package com.example.mytripbuddymb.utils;

import android.content.Context;
import android.content.SharedPreferences;


    public class SharedPreferencesManager {
        private static final String PREF_NAME = "TripBuddyPrefs";
        private static final String KEY_USER_ID = "user_id";
        private static final String KEY_USERNAME = "username";
        private static final String KEY_EMAIL = "email";
        private static final String KEY_IS_LOGGED_IN = "is_logged_in";
        private static final String KEY_TRIP_COUNT = "trip_count";
        private static final String KEY_TES_SCORE = "tes_score";
        private static final String KEY_THEME_MODE = "theme_mode";
        private static final String KEY_BACKGROUND_MUSIC_ENABLED = "background_music_enabled";
        private static final String KEY_LANGUAGE = "language";
        private static final String KEY_FIRST_LAUNCH = "first_launch";
        private static final String KEY_LAST_LOGIN = "last_login";

        private SharedPreferences sharedPreferences;
        private SharedPreferences.Editor editor;

        public SharedPreferencesManager(Context context) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }

        // User Session Management
        public void saveUserSession(int userId, String username, String email) {
            editor.putInt(KEY_USER_ID, userId);
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_EMAIL, email);
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.putLong(KEY_LAST_LOGIN, System.currentTimeMillis());
            editor.apply();
        }

        public void clearUserSession() {
            editor.remove(KEY_USER_ID);
            editor.remove(KEY_USERNAME);
            editor.remove(KEY_EMAIL);
            editor.putBoolean(KEY_IS_LOGGED_IN, false);
            editor.apply();
        }

        public boolean isLoggedIn() {
            return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
        }

        public int getUserId() {
            return sharedPreferences.getInt(KEY_USER_ID, -1);
        }

        public String getUsername() {
            return sharedPreferences.getString(KEY_USERNAME, "");
        }

        public String getEmail() {
            return sharedPreferences.getString(KEY_EMAIL, "");
        }

        public long getLastLogin() {
            return sharedPreferences.getLong(KEY_LAST_LOGIN, 0);
        }

        // Trip Count Management
        public void setTripCount(int tripCount) {
            editor.putInt(KEY_TRIP_COUNT, tripCount);
            editor.apply();
        }

        public int getTripCount() {
            return sharedPreferences.getInt(KEY_TRIP_COUNT, 0);
        }

        public void incrementTripCount() {
            int currentCount = getTripCount();
            setTripCount(currentCount + 1);
        }

        // TES Score Management
        public void setTesScore(int tesScore) {
            editor.putInt(KEY_TES_SCORE, tesScore);
            editor.apply();
        }

        public int getTesScore() {
            return sharedPreferences.getInt(KEY_TES_SCORE, 0);
        }

        public void addTesScore(int points) {
            int currentScore = getTesScore();
            setTesScore(currentScore + points);
        }

        // App Preferences
        public void setThemeMode(String themeMode) {
            editor.putString(KEY_THEME_MODE, themeMode);
            editor.apply();
        }

        public String getThemeMode() {
            return sharedPreferences.getString(KEY_THEME_MODE, "light");
        }

        public void setBackgroundMusicEnabled(boolean enabled) {
            editor.putBoolean(KEY_BACKGROUND_MUSIC_ENABLED, enabled);
            editor.apply();
        }

        public boolean isBackgroundMusicEnabled() {
            return sharedPreferences.getBoolean(KEY_BACKGROUND_MUSIC_ENABLED, true);
        }

        public void setLanguage(String language) {
            editor.putString(KEY_LANGUAGE, language);
            editor.apply();
        }

        public String getLanguage() {
            return sharedPreferences.getString(KEY_LANGUAGE, "en");
        }

        public void setFirstLaunch(boolean isFirstLaunch) {
            editor.putBoolean(KEY_FIRST_LAUNCH, isFirstLaunch);
            editor.apply();
        }

        public boolean isFirstLaunch() {
            return sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true);
        }

        // Loyalty Discount Logic
        public boolean isEligibleForLoyaltyDiscount() {
            return getTripCount() >= 3;
        }

        public double getLoyaltyDiscountPercentage() {
            if (isEligibleForLoyaltyDiscount()) {
                return 10.0; // 10% discount after 3 trips
            }
            return 0.0;
        }


        public void clearAllPreferences() {
            editor.clear();
            editor.apply();
        }


        public boolean isSessionExpired() {
            long lastLogin = getLastLogin();
            long currentTime = System.currentTimeMillis();
            long thirtyDaysInMillis = 30 * 24 * 60 * 60 * 1000L;

            return (currentTime - lastLogin) > thirtyDaysInMillis;
        }
    }


