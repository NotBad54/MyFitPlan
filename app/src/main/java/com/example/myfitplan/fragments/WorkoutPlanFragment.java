package com.example.myfitplan.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myfitplan.R;
import com.example.myfitplan.adapters.WorkoutPlanAdapter;
import com.example.myfitplan.entities.WorkoutPlan;
import com.example.myfitplan.viewmodels.ExerciseViewModel;
import com.example.myfitplan.viewmodels.WorkoutPlanViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import org.threeten.bp.LocalDate;
import java.util.ArrayList;

public class WorkoutPlanFragment extends Fragment {
    private RecyclerView recyclerView;
    private WorkoutPlanAdapter adapter;
    private WorkoutPlanViewModel workoutPlanVM;
    private ExerciseViewModel exerciseVM;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_plan, container, false);
        initViews(view);
        setupViewModels();
        setupRecyclerView(view);
        setupButtons(view);
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rv_all_exercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setupViewModels() {
        workoutPlanVM = new ViewModelProvider(this).get(WorkoutPlanViewModel.class);
        exerciseVM = new ViewModelProvider(this).get(ExerciseViewModel.class);

        exerciseVM.getAllExercises().observe(getViewLifecycleOwner(), exercises -> {
            if (getView() == null) return;
            if (exercises != null) {
                adapter.submitList(exercises);
            }
        });
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.rv_all_exercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new WorkoutPlanAdapter(new ArrayList<>(), (exercise, isChecked) -> {
            exercise.setSelected(isChecked);
            if (isChecked) {
                workoutPlanVM.addExerciseToPlan(exercise);
            } else {
                workoutPlanVM.removeExerciseFromPlan(exercise);
            }
        }, exercise -> {
            // Навигация к деталям упражнения
            Bundle args = new Bundle();
            args.putInt("exercise_id", exercise.getId());
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_workoutPlan_to_exerciseDetail, args);
        }
        );
        recyclerView.setAdapter(adapter);
    }

    private void setupButtons(View view) {
        view.findViewById(R.id.btn_save_favorite).setOnClickListener(v -> showSaveDialog());
        view.findViewById(R.id.btn_load_favorite).setOnClickListener(v -> showFavoritePlansDialog());
        view.findViewById(R.id.btn_schedule).setOnClickListener(v -> showDatePicker());
    }

    private void showSaveDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_plan_name, null);
        EditText etPlanName = dialogView.findViewById(R.id.et_plan_name);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Название плана")
                .setView(dialogView)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    String planName = etPlanName.getText().toString().trim();
                    if (!planName.isEmpty()) {
                        workoutPlanVM.saveAsFavorite(planName);
                        Toast.makeText(requireContext(), "План сохранен!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void showFavoritePlansDialog() {
        workoutPlanVM.getFavoritePlans().observe(getViewLifecycleOwner(), plans -> {
            if (plans == null || plans.isEmpty()) {
                Toast.makeText(requireContext(), "Нет сохраненных планов", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] planNames = plans.stream()
                    .map(WorkoutPlan::getName)
                    .toArray(String[]::new);

            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Выберите план")
                    .setItems(planNames, (dialog, which) -> {
                        WorkoutPlan selectedPlan = plans.get(which);
                        workoutPlanVM.loadPlan(selectedPlan);
                        adapter.submitList(selectedPlan.getExercises());
                    })
                    .show();
        });
    }

    private void showDatePicker() {
        LocalDate today = LocalDate.now();
        new DatePickerDialog(requireContext(), (dateView, year, month, day) -> {
            LocalDate selectedDate = LocalDate.of(year, month + 1, day);
            if (selectedDate.isBefore(LocalDate.now())) {
                Toast.makeText(requireContext(), "Нельзя выбрать прошедшую дату", Toast.LENGTH_SHORT).show();
                return;
            }
            workoutPlanVM.schedulePlan(selectedDate);
            NavHostFragment.findNavController(this).navigateUp();
            Toast.makeText(requireContext(), "Тренировка запланирована на " + selectedDate, Toast.LENGTH_SHORT).show();
        }, today.getYear(), today.getMonthValue() - 1, today.getDayOfMonth()).show();
    }
}