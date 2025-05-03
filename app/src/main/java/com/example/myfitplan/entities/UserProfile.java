package com.example.myfitplan.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.threeten.bp.LocalDate;

@Entity(tableName = "user_profiles")
public class UserProfile {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    public String name;
    public int age;
    public float weight;
    public float height;
    public String level; // "Начинающий", "Активный", "Опытный"
    public String goal;        // "Похудение", "Поддержание", "Набор массы"
    public String workoutPlan;
    public LocalDate lastUpdated;
}