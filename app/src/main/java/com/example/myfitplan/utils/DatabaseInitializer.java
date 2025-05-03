package com.example.myfitplan.utils;

import android.content.Context;
import com.example.myfitplan.databases.AppDatabase;
import com.example.myfitplan.daos.ExerciseDao;
import com.example.myfitplan.entities.Exercise;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DatabaseInitializer {
    private static final Executor executor = Executors.newSingleThreadExecutor();

    public static void populateExercises(Context context) {
        executor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(context);
            ExerciseDao exerciseDao = db.exerciseDao();

            // Проверяем, пуста ли таблица упражнений
            if (exerciseDao.getCount() == 0) {
                // Создаем список начальных упражнений
                List<Exercise> defaultExercises = Arrays.asList(
                        new Exercise(
                                "Приседания",
                                "Силовая",
                                "Упражнение для ног и ягодиц. 3 подхода по 15 повторений.",
                                15,
                                3,
                                "drawable/img_squat"
                        ),
                        new Exercise(
                                "Отжимания",
                                "Силовая",
                                "Укрепление груди и трицепсов. 4 подхода по 12 повторений.",
                                12,
                                4,
                                "drawable/img_pushup"
                        ),
                        new Exercise(
                                "Планка",
                                "Статика",
                                "Укрепление мышц кора. Держать 60 секунд.",
                                60,
                                1,
                                "drawable/img_plank"
                        )
                );

                // Вставляем упражнения в базу данных
                exerciseDao.insertAll(defaultExercises);
            }
        });
    }
}