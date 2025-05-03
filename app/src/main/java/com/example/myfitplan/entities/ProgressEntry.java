package com.example.myfitplan.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import org.threeten.bp.LocalDate;

@Entity(tableName = "progress_entries",
        foreignKeys = @ForeignKey(
                entity = UserProfile.class,
                parentColumns = "uid",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("userId")})
public class ProgressEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public float weight;
    public LocalDate date;
}