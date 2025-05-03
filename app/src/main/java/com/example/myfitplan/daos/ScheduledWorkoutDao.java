package com.example.myfitplan.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.myfitplan.entities.ScheduledWorkout;
import org.threeten.bp.LocalDate;

import java.util.List;

@Dao
public interface ScheduledWorkoutDao {
    @Insert
    void insert(ScheduledWorkout scheduledWorkout);

    @Query("DELETE FROM scheduled_workouts WHERE userId = :userId AND date = :date")
    void deleteByDate(int userId, LocalDate date);

    @Query("SELECT * FROM scheduled_workouts WHERE userId = :userId AND date BETWEEN :start AND :end")
    LiveData<List<ScheduledWorkout>> getScheduledWorkoutsByDate(int userId, LocalDate start, LocalDate end);

    @Query("SELECT * FROM scheduled_workouts WHERE userId = :userId AND date = :date LIMIT 1")
    LiveData<ScheduledWorkout> getScheduledWorkoutByDate(int userId, LocalDate date);

    @Query("SELECT * FROM scheduled_workouts WHERE userId = :userId AND date >= :date ORDER BY date ASC LIMIT 1")
    LiveData<ScheduledWorkout> getNextWorkout(int userId, LocalDate date);
}