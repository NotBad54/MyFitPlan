package com.example.myfitplan.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.myfitplan.R;
import com.example.myfitplan.viewmodels.UserProfileViewModel;
import com.example.myfitplan.utils.DatabaseInitializer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private boolean isFirstLaunch() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        return prefs.getBoolean("is_first_launch", true);
    }
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация упражнений при первом запуске
        if (isFirstLaunch()) {
            DatabaseInitializer.populateExercises(this);
            SharedPreferences.Editor editor = getSharedPreferences("app_prefs", MODE_PRIVATE).edit();
            editor.putBoolean("is_first_launch", false);
            editor.apply();
        }

        // Настройка навигации
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        // Привязка нижнего меню
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null && navController != null) {
            NavigationUI.setupWithNavController(bottomNav, navController);
        }

        // Проверка наличия профиля
        UserProfileViewModel viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        viewModel.hasUser().observe(this, hasUser -> {
            if (hasUser) {
                viewModel.loadUser();
                viewModel.loadNextWorkout();
                navController.navigate(R.id.nav_profile);
            } else {
                // Перенаправление на создание профиля
                navController.navigate(R.id.nav_create_profile);
            }
        });
    }
}