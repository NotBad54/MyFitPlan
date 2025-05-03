package com.example.myfitplan;

import android.app.Application;
import androidx.room.Room;
import com.example.myfitplan.databases.AppDatabase;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class MyFitPlanApp extends Application {
    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        database = Room.databaseBuilder(this, AppDatabase.class, "myfitplan-db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}