package com.example.myfitplan.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import org.threeten.bp.LocalDate;

@Entity(tableName = "workout_schedules",
        foreignKeys = @ForeignKey(
                entity = UserProfile.class,
                parentColumns = "uid",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("userId")})
public class WorkoutSchedule {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;

    public LocalDate date;
    public String plan;
    public boolean isCustom;

}