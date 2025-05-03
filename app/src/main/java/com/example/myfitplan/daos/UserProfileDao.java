package com.example.myfitplan.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myfitplan.entities.UserProfile;

import org.threeten.bp.LocalDate;

@Dao
public interface UserProfileDao {
    @Insert
    void insert(UserProfile user);

    @Update
    void updateFullProfile(UserProfile user);

    @Query("SELECT * FROM user_profiles LIMIT 1")
    LiveData<UserProfile> getUserLiveData();

    @Query("SELECT COUNT(*) FROM user_profiles")
    int getUserCount();

    @Query("SELECT * FROM user_profiles LIMIT 1")
    UserProfile getUser();

    @Query("UPDATE user_profiles SET weight = :newWeight, height = :newHeight, lastUpdated = :updateDate WHERE uid = :userId")
    void updateProfile(int userId, float newWeight, float newHeight, LocalDate updateDate);
}
