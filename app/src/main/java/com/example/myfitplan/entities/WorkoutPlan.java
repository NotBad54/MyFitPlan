package com.example.myfitplan.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.myfitplan.utils.ExerciseListConverter;

import org.threeten.bp.LocalDate;

import java.util.List;

@Entity(tableName = "workout_plans")
@TypeConverters(ExerciseListConverter.class)
public class WorkoutPlan {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private String name;
    private String description;
    private boolean isFavorite;
    private List<Exercise> exercises;
    private LocalDate date;


    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }
}