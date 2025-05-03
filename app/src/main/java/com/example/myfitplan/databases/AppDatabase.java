package com.example.myfitplan.databases;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myfitplan.daos.ExerciseDao;
import com.example.myfitplan.daos.ProgressDao;
import com.example.myfitplan.daos.ScheduledWorkoutDao;
import com.example.myfitplan.daos.UserProfileDao;
import com.example.myfitplan.daos.WorkoutDao;
import com.example.myfitplan.daos.WorkoutPlanDao;
import com.example.myfitplan.entities.Exercise;
import com.example.myfitplan.entities.ScheduledWorkout;
import com.example.myfitplan.entities.UserProfile;
import com.example.myfitplan.entities.ProgressEntry;
import com.example.myfitplan.entities.WorkoutPlan;
import com.example.myfitplan.entities.WorkoutSchedule;
import com.example.myfitplan.utils.DatabaseInitializer;
import com.example.myfitplan.utils.ExerciseListConverter;


@Database(entities = {UserProfile.class, ProgressEntry.class, WorkoutSchedule.class, Exercise.class, WorkoutPlan.class, ScheduledWorkout.class}, version = 5)
@TypeConverters({Converters.class, ExerciseListConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    public abstract UserProfileDao userProfileDao();
    public abstract ProgressDao progressDao();
    public abstract WorkoutDao workoutDao();
    public abstract ExerciseDao exerciseDao();
    public abstract WorkoutPlanDao workoutPlanDao();
    public abstract ScheduledWorkoutDao scheduledWorkoutDao();

    public static synchronized AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "myfitplan_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            DatabaseInitializer.populateExercises(context);
                        }
                    })
                    .build();
        }
        return INSTANCE;
    }
}