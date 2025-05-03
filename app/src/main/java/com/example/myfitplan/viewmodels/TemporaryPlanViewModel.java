package com.example.myfitplan.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myfitplan.entities.Exercise;

import java.util.ArrayList;
import java.util.List;

public class TemporaryPlanViewModel extends ViewModel {
    private final MutableLiveData<List<Exercise>> temporaryExercises = new MutableLiveData<>(new ArrayList<>());

    public void addExercise(Exercise exercise) {
        List<Exercise> exercises = temporaryExercises.getValue();
        exercises.add(exercise);
        temporaryExercises.setValue(exercises);
    }

    public LiveData<List<Exercise>> getTemporaryExercises() {
        return temporaryExercises;
    }

    public void clear() {
        temporaryExercises.setValue(new ArrayList<>());
    }
}
