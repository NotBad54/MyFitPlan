package com.example.myfitplan.viewmodels;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.myfitplan.databases.AppDatabase;
import com.example.myfitplan.entities.UserProfile;
import com.example.myfitplan.entities.WorkoutSchedule;
import org.threeten.bp.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserProfileViewModel extends AndroidViewModel {
    private final AppDatabase database;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<UserProfile> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<WorkoutSchedule> nextWorkout = new MutableLiveData<>();

    public UserProfileViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getDatabase(application);
        loadUser();
    }

    public void insertUser(UserProfile user) {
        executor.execute(() -> {
            database.userProfileDao().insert(user);
            loadUser();
        });
    }

    // Загрузка данных пользователя
    public void loadUser() {
        executor.execute(() -> {
            UserProfile user = database.userProfileDao().getUser();
            userLiveData.postValue(user);
        });
    }

    // Получение LiveData пользователя
    public LiveData<UserProfile> getUser() {
        return userLiveData;
    }

    // Обновление профиля
    public void updateFullProfile(UserProfile user) {
        executor.execute(() -> {
            database.userProfileDao().updateFullProfile(user);
            loadUser();
        });
    }

    // Загрузка ближайшей тренировки
    public void loadNextWorkout(int userId) {
        executor.execute(() -> {
            LocalDate today = LocalDate.now();
            WorkoutSchedule workout = database.workoutDao().getNextWorkoutAfterDate(userId, today);
            nextWorkout.postValue(workout);
        });
    }

    public void loadNextWorkout() {
        executor.execute(() -> {
            UserProfile user = database.userProfileDao().getUser();
            if (user != null) {
                LocalDate today = LocalDate.now();
                WorkoutSchedule workout = database.workoutDao().getNextWorkoutAfterDate(user.uid, today);
                nextWorkout.postValue(workout);
            }
        });
    }

    // Получение LiveData ближайшей тренировки
    public LiveData<WorkoutSchedule> getNextWorkout() {
        return nextWorkout;
    }

    // Получение ID текущего пользователя
    public void getCurrentUserId(UserIdCallback callback) {
        executor.execute(() -> {
            UserProfile user = database.userProfileDao().getUser();
            int userId = (user != null) ? user.uid : -1;
            new Handler(Looper.getMainLooper()).post(() -> callback.onUserIdReceived(userId));
        });
    }

    public LiveData<Boolean> hasUser() {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        executor.execute(() -> {
            int count = database.userProfileDao().getUserCount();
            result.postValue(count > 0);
        });
        return result;
    }

    // Callback для ID пользователя
    public interface UserIdCallback {
        void onUserIdReceived(int userId);
    }

    public void refreshNextWorkout() {
        loadNextWorkout();
    }
}