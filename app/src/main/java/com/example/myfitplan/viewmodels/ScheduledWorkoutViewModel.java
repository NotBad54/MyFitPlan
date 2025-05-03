package com.example.myfitplan.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myfitplan.databases.AppDatabase;
import com.example.myfitplan.daos.ScheduledWorkoutDao;
import com.example.myfitplan.entities.ScheduledWorkout;

import org.threeten.bp.LocalDate;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ScheduledWorkoutViewModel extends AndroidViewModel {
    private final ScheduledWorkoutDao scheduledWorkoutDao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public ScheduledWorkoutViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        scheduledWorkoutDao = db.scheduledWorkoutDao();
    }

    public LiveData<ScheduledWorkout> getScheduledWorkout(int userId, LocalDate date) {
        return scheduledWorkoutDao.getScheduledWorkoutByDate(userId, date);
    }

    public LiveData<ScheduledWorkout> getNextWorkout(int userId) {
        MutableLiveData<ScheduledWorkout> result = new MutableLiveData<>();
        executor.execute(() -> {
            ScheduledWorkout workout = scheduledWorkoutDao.getNextWorkout(userId, LocalDate.now()).getValue();
            result.postValue(workout);
        });
        return result;
    }
}
