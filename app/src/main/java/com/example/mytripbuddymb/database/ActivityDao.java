package com.example.mytripbuddymb.database;
import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;



import java.util.List;

@Dao
public interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertActivity(Activity activity);

    @Update
    void updateActivity(Activity activity);

    @Delete
    void deleteActivity(Activity activity);

    @Query("SELECT * FROM activities WHERE id = :activityId")
    LiveData<Activity> getActivityById(int activityId);

    @Query("SELECT * FROM activities WHERE tripId = :tripId ORDER BY scheduledDate ASC")
    LiveData<List<Activity>> getActivitiesByTripId(int tripId);

    @Query("SELECT * FROM activities WHERE tripId = :tripId AND category = :category ORDER BY scheduledDate ASC")
    LiveData<List<Activity>> getActivitiesByTripIdAndCategory(int tripId, String category);

    @Query("SELECT * FROM activities WHERE tripId = :tripId AND status = :status ORDER BY scheduledDate ASC")
    LiveData<List<Activity>> getActivitiesByTripIdAndStatus(int tripId, String status);

    @Query("SELECT * FROM activities WHERE tripId = :tripId AND isCustom = 1 ORDER BY createdAt DESC")
    LiveData<List<Activity>> getCustomActivitiesByTripId(int tripId);

    @Query("SELECT * FROM activities WHERE tripId = :tripId AND scheduledDate >= :currentTime ORDER BY scheduledDate ASC")
    LiveData<List<Activity>> getUpcomingActivities(int tripId, long currentTime);

    @Query("SELECT * FROM activities WHERE tripId = :tripId AND scheduledDate < :currentTime ORDER BY scheduledDate DESC")
    LiveData<List<Activity>> getPastActivities(int tripId, long currentTime);

    @Query("SELECT COUNT(*) FROM activities WHERE tripId = :tripId")
    int getActivityCountByTripId(int tripId);

    @Query("SELECT COUNT(*) FROM activities WHERE tripId = :tripId AND status = 'completed'")
    int getCompletedActivityCount(int tripId);

    @Query("SELECT SUM(estimatedCost) FROM activities WHERE tripId = :tripId")
    double getTotalEstimatedCost(int tripId);

    @Query("SELECT SUM(actualCost) FROM activities WHERE tripId = :tripId")
    double getTotalActualCost(int tripId);

    @Query("SELECT SUM(estimatedCost) FROM activities WHERE tripId = :tripId AND category = :category")
    double getEstimatedCostByCategory(int tripId, String category);

    @Query("SELECT SUM(actualCost) FROM activities WHERE tripId = :tripId AND category = :category")
    double getActualCostByCategory(int tripId, String category);

    @Query("SELECT DISTINCT category FROM activities WHERE tripId = :tripId")
    List<String> getUniqueCategories(int tripId);

    @Query("SELECT * FROM activities WHERE tripId = :tripId AND name LIKE '%' || :searchQuery || '%' ORDER BY scheduledDate ASC")
    LiveData<List<Activity>> searchActivities(int tripId, String searchQuery);

    @Query("UPDATE activities SET actualCost = :actualCost WHERE id = :activityId")
    void updateActualCost(int activityId, double actualCost);

    @Query("UPDATE activities SET status = :status WHERE id = :activityId")
    void updateActivityStatus(int activityId, String status);

    @Query("SELECT * FROM activities WHERE tripId = :tripId AND location LIKE '%' || :location || '%' ORDER BY scheduledDate ASC")
    LiveData<List<Activity>> getActivitiesByLocation(int tripId, String location);
}

