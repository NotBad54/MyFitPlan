package com.example.myfitplan.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercises")
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String type;
    private String description;
    private int reps;
    private int sets;
    private String imageUri;
    private boolean isSelected;

    // Конструктор
    public Exercise(String name, String type, String description, int reps, int sets, String imageUri) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.reps = reps;
        this.sets = sets;
        this.imageUri = imageUri;
    }

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

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String getImageUri() {
        return imageUri;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}