package com.example.myfitplan.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;
import com.example.myfitplan.entities.WorkoutSchedule;
import java.util.List;
import org.threeten.bp.LocalDate;

@Dao
public interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WorkoutSchedule workout);

    @Query("SELECT * FROM workout_schedules WHERE userId = :userId AND date = :date LIMIT 1")
    WorkoutSchedule getWorkoutByDate(int userId, LocalDate date);

    @Query("SELECT * FROM workout_schedules WHERE userId = :userId AND date BETWEEN :start AND :end")
    List<WorkoutSchedule> getWorkoutsByUserAndDate(int userId, LocalDate start, LocalDate end);

    @Query("SELECT * FROM workout_schedules WHERE userId = :userId AND date >= :today ORDER BY date ASC LIMIT 1")
    WorkoutSchedule getNextWorkout(int userId, LocalDate today);

    @Query("SELECT * FROM workout_schedules WHERE userId = :userId AND date >= :date ORDER BY date ASC LIMIT 1")
    WorkoutSchedule getNextWorkoutAfterDate(int userId, LocalDate date);

    @Delete
    void delete(WorkoutSchedule workout);

}