package com.example.myfitplan.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.myfitplan.daos.ScheduledWorkoutDao;
import com.example.myfitplan.daos.WorkoutPlanDao;
import com.example.myfitplan.databases.AppDatabase;
import com.example.myfitplan.entities.ScheduledWorkout;
import com.example.myfitplan.entities.WorkoutPlan;
import org.threeten.bp.LocalDate;

import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {
    private final ScheduledWorkoutDao scheduledWorkoutDao;
    private final WorkoutPlanDao workoutPlanDao;

    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getDatabase(application);
        scheduledWorkoutDao = database.scheduledWorkoutDao();
        workoutPlanDao = database.workoutPlanDao();
    }

    // Получение списка тренировок за месяц
    public LiveData<java.util.List<ScheduledWorkout>> getScheduledWorkoutsByDate(int userId, LocalDate start, LocalDate end) {
        return scheduledWorkoutDao.getScheduledWorkoutsByDate(userId, start, end);
    }

    // Получение тренировки по дате
    public LiveData<ScheduledWorkout> getScheduledWorkoutByDate(int userId, LocalDate date) {
        return scheduledWorkoutDao.getScheduledWorkoutByDate(userId, date);
    }

    public LiveData<List<ScheduledWorkout>> getScheduledWorkouts(int userId, LocalDate start, LocalDate end) {
        return scheduledWorkoutDao.getScheduledWorkoutsByDate(userId, start, end);
    }

    // Метод для загрузки тренировок
    public void loadWorkoutsForMonth(LocalDate date, int userId) {
        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = date.withDayOfMonth(date.lengthOfMonth());
        getScheduledWorkouts(userId, start, end).observeForever(workouts -> {
            // Обновляем адаптер
        });
    }
}