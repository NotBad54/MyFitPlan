package com.example.myfitplan.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.myfitplan.databases.AppDatabase;
import com.example.myfitplan.daos.ExerciseDao;
import com.example.myfitplan.entities.Exercise;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExerciseViewModel extends AndroidViewModel {
    private final ExerciseDao exerciseDao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public ExerciseViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        exerciseDao = db.exerciseDao();
    }

    public LiveData<List<Exercise>> getAllExercises() {
        return exerciseDao.getAllExercises();
    }

    public LiveData<Exercise> getExerciseById(int id) {
        return exerciseDao.getExerciseById(id);
    }

    public void insertExercise(Exercise exercise) {
        executor.execute(() -> exerciseDao.insert(exercise));
    }

    public void deleteExercise(Exercise exercise) {
        executor.execute(() -> exerciseDao.delete(exercise));
    }
}