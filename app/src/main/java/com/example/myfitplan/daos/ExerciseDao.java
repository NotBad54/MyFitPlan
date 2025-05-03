package com.example.myfitplan.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myfitplan.entities.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert
    void insert(Exercise exercise);

    @Insert
    void insertAll(List<Exercise> exercises);

    @Delete
    void delete(Exercise exercise);

    @Query("SELECT COUNT(*) FROM exercises")
    int getCount();

    @Query("SELECT * FROM exercises")
    LiveData<List<Exercise>> getAllExercises();

    @Query("SELECT * FROM exercises WHERE id = :id LIMIT 1")
    LiveData<Exercise> getExerciseById(int id);
}
