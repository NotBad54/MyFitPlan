package com.example.myfitplan.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.myfitplan.R;
import com.example.myfitplan.entities.UserProfile;
import com.example.myfitplan.utils.WorkoutPlanGenerator;
import com.example.myfitplan.viewmodels.UserProfileViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CreateProfileFragment extends Fragment {
    private TextInputEditText etName, etAge, etWeight, etHeight;
    private Spinner spLevel, spGoal;
    private MaterialButton btnSave;
    private UserProfileViewModel viewModel;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_profile, container, false);
        initViews(view);
        setupViewModel();
        setupSpinners();
        setupSaveButton();
        return view;
    }

    private void initViews(View view) {
        etName = view.findViewById(R.id.et_name);
        etAge = view.findViewById(R.id.et_age);
        etWeight = view.findViewById(R.id.et_weight);
        etHeight = view.findViewById(R.id.et_height);
        spLevel = view.findViewById(R.id.sp_level);
        spGoal = view.findViewById(R.id.sp_goal);
        btnSave = view.findViewById(R.id.btn_save_plan);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
    }

    private void setupSpinners() {
        // Уровень подготовки
        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.levels_array,
                android.R.layout.simple_spinner_item
        );
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLevel.setAdapter(levelAdapter);

        // Цель
        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.goals_array,
                android.R.layout.simple_spinner_item
        );
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGoal.setAdapter(goalAdapter);
    }

    private void setupSaveButton() {
        btnSave.setOnClickListener(v -> saveUserPlan());
    }

    private void saveUserPlan() {
        executor.execute(() -> {
            try {
                // Парсинг данных с проверкой на пустые значения
                String name = etName.getText().toString().trim();
                int age = Integer.parseInt(etAge.getText().toString());
                float weight = Float.parseFloat(etWeight.getText().toString());
                float height = Float.parseFloat(etHeight.getText().toString());
                String level = spLevel.getSelectedItem().toString();
                String goal = spGoal.getSelectedItem().toString();

                // Создание объекта пользователя
                UserProfile user = new UserProfile();
                user.name = name;
                user.age = age;
                user.weight = weight;
                user.height = height;
                user.level = level;
                user.goal = goal;
                user.workoutPlan = generateWorkoutPlan(level, goal);

                // Проверка ViewModel и сохранение
                if (viewModel != null) {
                    viewModel.insertUser(user);
                    viewModel.loadUser();
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "План сохранен!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(requireView()).navigate(R.id.action_createProfile_to_profile);
                    });
                } else {
                    Log.e("CreatePlanFragment", "ViewModel is null!");
                }

            } catch (NumberFormatException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Проверьте введенные данные!", Toast.LENGTH_SHORT).show()
                );
            } catch (Exception e) {
                Log.e("CreatePlanFragment", "Ошибка: " + e.getMessage());
            }
        });
    }

    private String generateWorkoutPlan(String level, String goal) {
        return WorkoutPlanGenerator.generateWorkoutPlan(level, goal);
    }
}