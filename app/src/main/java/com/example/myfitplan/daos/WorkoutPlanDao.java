package com.example.myfitplan.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myfitplan.entities.WorkoutPlan;

import java.util.List;

@Dao
public interface WorkoutPlanDao {
    @Insert
    void insert(WorkoutPlan plan);

    @Update
    void update(WorkoutPlan plan);

    @Query("SELECT * FROM workout_plans WHERE id = :planId")
    LiveData<WorkoutPlan> getPlanById(int planId);

    @Query("SELECT * FROM workout_plans WHERE isFavorite = 1")
    LiveData<List<WorkoutPlan>> getFavoritePlans();

}
