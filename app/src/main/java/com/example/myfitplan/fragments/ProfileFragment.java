package com.example.myfitplan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.myfitplan.R;
import com.example.myfitplan.viewmodels.UserProfileViewModel;
import com.google.android.material.button.MaterialButton;
import org.threeten.bp.format.DateTimeFormatter;


public class ProfileFragment extends Fragment {
    private UserProfileViewModel viewModel;
    private TextView tvName, tvWeight, tvAge, tvHeight, tvLevel, tvGoal, tvNextWorkout, tvWorkoutPlan;
    private MaterialButton btnEditProfile;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setupViews(view);
        setupViewModel();
        return view;
    }

    private void setupViews(View view) {
        tvName = view.findViewById(R.id.tv_profile_name);
        tvWeight = view.findViewById(R.id.tv_profile_weight);
        tvAge = view.findViewById(R.id.tv_profile_age);
        tvHeight = view.findViewById(R.id.tv_profile_height);
        tvLevel = view.findViewById(R.id.tv_profile_level);
        tvGoal = view.findViewById(R.id.tv_profile_goal);
        tvNextWorkout = view.findViewById(R.id.tv_next_workout);
        tvWorkoutPlan = view.findViewById(R.id.tv_workout_plan);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnEditProfile.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_profile_to_editProfile);
        });
    }


    private void setupViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);

        // Наблюдение за данными пользователя
        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                // Обновление UI с данными пользователя
                tvName.setText(user.name);
                tvWeight.setText(String.format("%.1f кг", user.weight));
                tvAge.setText(String.valueOf(user.age));
                tvHeight.setText(String.format("%.1f см", user.height));
                tvLevel.setText(user.level);
                tvGoal.setText(user.goal);
                tvWorkoutPlan.setText(
                        user.workoutPlan != null ?
                                "Рекомендации:\n" + user.workoutPlan :
                                "План тренировок не задан"
                );

                // Загрузить ближайшую тренировку при наличии пользователя
                viewModel.loadNextWorkout(user.uid); // Передать user.uid
            }
        });


        // Наблюдение за ближайшей тренировкой
        viewModel.getNextWorkout().observe(getViewLifecycleOwner(), workout -> {
            if (workout != null) {
                String dateFormatted = workout.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                tvNextWorkout.setText("Ближайшая тренировка: " + dateFormatted + "\nПлан: " + workout.plan);
            } else {
                tvNextWorkout.setText("Ближайших тренировок нет");
            }
        });

        // Явная загрузка пользователя при инициализации
        viewModel.loadUser();
    }

}