package com.example.myfitplan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.Objects;

public class EditProfileFragment extends Fragment {

    private EditText etName, etAge, etWeight, etHeight;
    private Spinner spLevel, spGoal;
    private UserProfileViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        setupViews(view);
        setupViewModel();
        return view;
    }

    private void setupViews(View view) {
        etName = view.findViewById(R.id.et_edit_name);
        etAge = view.findViewById(R.id.et_edit_age);
        etWeight = view.findViewById(R.id.et_edit_weight);
        etHeight = view.findViewById(R.id.et_edit_height);
        spLevel = view.findViewById(R.id.sp_edit_level);
        spGoal = view.findViewById(R.id.sp_edit_goal);

        // Настройка спиннеров
        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.levels_array,
                android.R.layout.simple_spinner_item
        );
        spLevel.setAdapter(levelAdapter);

        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.goals_array,
                android.R.layout.simple_spinner_item
        );
        spGoal.setAdapter(goalAdapter);

        MaterialButton btnSave = view.findViewById(R.id.btn_save_changes);
        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                etName.setText(user.name);
                etAge.setText(String.valueOf(user.age));
                etWeight.setText(String.valueOf(user.weight));
                etHeight.setText(String.valueOf(user.height));
                spLevel.setSelection(getIndex(spLevel, user.level));
                spGoal.setSelection(getIndex(spGoal, user.goal));
            }
        });

        // Загрузка данных при первом открытии
        viewModel.loadUser();
    }

    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }

    private void saveChanges() {
        try {
            String name = Objects.requireNonNull(etName.getText()).toString().trim();
            String ageText = Objects.requireNonNull(etAge.getText()).toString();
            String weightText = Objects.requireNonNull(etWeight.getText()).toString();
            String heightText = Objects.requireNonNull(etHeight.getText()).toString();

            // Проверка пустых полей
            if (name.isEmpty() || ageText.isEmpty() || weightText.isEmpty() || heightText.isEmpty()) {
                throw new IllegalArgumentException("Заполните все поля");
            }

            // Парсинг числовых значений
            int age = Integer.parseInt(ageText);
            float weight = Float.parseFloat(weightText);
            float height = Float.parseFloat(heightText);
            String level = spLevel.getSelectedItem().toString();
            String goal = spGoal.getSelectedItem().toString();

            // Получить текущего пользователя
            UserProfile currentUser = viewModel.getUser().getValue();
            if (currentUser == null) {
                Toast.makeText(requireContext(), "Ошибка: пользователь не найден", Toast.LENGTH_SHORT).show();
                return;
            }

            // Обновление профиля
            UserProfile updatedUser = new UserProfile();
            updatedUser.uid = currentUser.uid;
            updatedUser.name = name;
            updatedUser.age = age;
            updatedUser.weight = weight;
            updatedUser.height = height;
            updatedUser.level = level;
            updatedUser.goal = goal;
            updatedUser.workoutPlan = WorkoutPlanGenerator.generateWorkoutPlan(level, goal);

            viewModel.updateFullProfile(updatedUser);
            Navigation.findNavController(requireView()).navigateUp();

        } catch (IllegalArgumentException e) {
            String message = e.getMessage() != null ? e.getMessage() : "Некорректные данные";
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Ошибка сохранения: " + e.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
        }
    }
}