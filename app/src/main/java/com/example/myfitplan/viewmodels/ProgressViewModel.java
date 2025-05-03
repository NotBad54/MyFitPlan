package com.example.myfitplan.viewmodels;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.myfitplan.databases.AppDatabase;
import com.example.myfitplan.entities.ProgressEntry;
import com.example.myfitplan.entities.UserProfile;

import org.threeten.bp.LocalDate;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProgressViewModel extends AndroidViewModel {
    private final AppDatabase database;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private LiveData<List<ProgressEntry>> weightEntries;

    public ProgressViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getDatabase(application);
    }

    public void loadWeightEntries(int userId) {
        weightEntries = database.progressDao().getEntriesByUser(userId);
    }

    public LiveData<List<ProgressEntry>> getWeightEntries() {
        return weightEntries;
    }

    public void addWeightEntry(float weight, LocalDate date, int userId) {
        executor.execute(() -> {
            ProgressEntry entry = new ProgressEntry();
            entry.userId = userId;
            entry.weight = weight;
            entry.date = date;
            database.progressDao().insert(entry);
        });
    }

    public interface UserIdCallback {
        void onUserIdReceived(int userId);
    }

    public void getCurrentUserId(UserIdCallback callback) {
        executor.execute(() -> {
            UserProfile user = database.userProfileDao().getUser();
            int userId = (user != null) ? user.uid : -1;
            new Handler(Looper.getMainLooper()).post(() -> callback.onUserIdReceived(userId));
        });
    }
}