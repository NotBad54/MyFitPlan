package com.example.myfitplan.utils;

import androidx.room.TypeConverter;

import com.example.myfitplan.entities.Exercise;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class ExerciseListConverter {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static String fromExerciseList(List<Exercise> exercises) {
        return gson.toJson(exercises);
    }

    @TypeConverter
    public static List<Exercise> toExerciseList(String json) {
        Type type = new TypeToken<List<Exercise>>(){}.getType();
        return gson.fromJson(json, type);
    }
}