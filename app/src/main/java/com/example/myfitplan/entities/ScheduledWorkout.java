package com.example.myfitplan.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import org.threeten.bp.LocalDate;

@Entity(tableName = "scheduled_workouts",
        foreignKeys = {
                @ForeignKey(entity = UserProfile.class, parentColumns = "uid", childColumns = "userId"),
                @ForeignKey(entity = WorkoutPlan.class, parentColumns = "id", childColumns = "planId")
        },
        indices = {@Index("userId"), @Index("planId")})
public class ScheduledWorkout {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public int planId;
    public LocalDate date;

    // Геттеры
    public int getPlanId() {
        return planId;
    }

    public LocalDate getDate() {
        return date;
    }

    // Сеттеры
    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}