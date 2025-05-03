package com.example.myfitplan.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;
import com.example.myfitplan.entities.ProgressEntry;
import com.example.myfitplan.entities.UserProfile;
import java.util.List;
import org.threeten.bp.LocalDate;
import androidx.lifecycle.LiveData;

@Dao
public interface ProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProgressEntry entry);
    @Query("SELECT * FROM user_profiles LIMIT 1")
    LiveData<UserProfile> getUserLiveData();
    @Query("SELECT * FROM user_profiles LIMIT 1")
    UserProfile getUser();
    @Query("SELECT * FROM progress_entries WHERE userId = :userId")
    LiveData<List<ProgressEntry>> getEntriesByUser(int userId);
}