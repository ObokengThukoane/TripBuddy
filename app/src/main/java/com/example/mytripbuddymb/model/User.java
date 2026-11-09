package com.example.mytripbuddymb.model;



import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String email;
    private String password;
    private int tripCount;
    private int tesScore; // TripBuddy Engagement Score
    private String profileImagePath;
    private long createdAt;

    public User() {
        this.createdAt = System.currentTimeMillis();
        this.tripCount = 0;
        this.tesScore = 0;
    }

    public User(String username, String email, String password) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTripCount() {
        return tripCount;
    }

    public void setTripCount(int tripCount) {
        this.tripCount = tripCount;
    }

    public int getTesScore() {
        return tesScore;
    }

    public void setTesScore(int tesScore) {
        this.tesScore = tesScore;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public void incrementTripCount() {
        this.tripCount++;
    }

    public void addTesScore(int points) {
        this.tesScore += points;
    }

    public boolean isEligibleForLoyaltyDiscount() {
        return tripCount >= 3;
    }

    public double getLoyaltyDiscountPercentage() {
        if (tripCount >= 3) {
            return 10.0; // 10% discount after 3 trips
        }
        return 0.0;
    }
}

