package com.example.myfitplan.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.myfitplan.databases.AppDatabase;
import com.example.myfitplan.daos.WorkoutPlanDao;
import com.example.myfitplan.entities.WorkoutPlan;
import com.example.myfitplan.entities.Exercise;
import org.threeten.bp.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WorkoutPlanViewModel extends AndroidViewModel {
    private final WorkoutPlanDao workoutPlanDao;
    private final MutableLiveData<List<Exercise>> currentExercises = new MutableLiveData<>(new ArrayList<>());
    private final Executor executor = Executors.newSingleThreadExecutor();

    public WorkoutPlanViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        workoutPlanDao = db.workoutPlanDao();
    }

    // Добавление упражнения в план
    public void addExerciseToPlan(Exercise exercise) {
        List<Exercise> exercises = currentExercises.getValue();
        if (exercises == null) {
            exercises = new ArrayList<>();
        }
        exercise.setSelected(true);
        exercises.add(exercise);
        currentExercises.postValue(exercises);
    }

    // Удаление упражнения из плана
    public void removeExerciseFromPlan(Exercise exercise) {
        List<Exercise> exercises = currentExercises.getValue();
        if (exercises != null) {
            exercises.remove(exercise);
            currentExercises.postValue(exercises);
        }
    }

    // Получение текущего списка упражнений
    public LiveData<List<Exercise>> getCurrentExercises() {
        return currentExercises;
    }

    // Сохранение плана в избранное
    public void saveAsFavorite(String planName) {
        executor.execute(() -> {
            WorkoutPlan plan = new WorkoutPlan();
            plan.setName(planName);
            plan.setExercises(currentExercises.getValue());
            plan.setFavorite(true);
            workoutPlanDao.insert(plan);
        });
    }

    // Планирование на дату
    public void schedulePlan(LocalDate date) {
        executor.execute(() -> {
            WorkoutPlan plan = new WorkoutPlan();
            plan.setDate(date);
            plan.setExercises(currentExercises.getValue());
            workoutPlanDao.insert(plan);
        });
    }

    // Получение плана по ID
    public LiveData<WorkoutPlan> getPlanById(int planId) {
        return workoutPlanDao.getPlanById(planId);
    }

    // Загрузка избранных планов
    public LiveData<List<WorkoutPlan>> getFavoritePlans() {
        return workoutPlanDao.getFavoritePlans();
    }

    // Загрузка упражнений из плана
    public void loadPlan(WorkoutPlan plan) {
        currentExercises.postValue(plan.getExercises());
    }
}