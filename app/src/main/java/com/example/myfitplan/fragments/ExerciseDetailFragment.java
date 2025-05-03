package com.example.myfitplan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.example.myfitplan.R;
import com.example.myfitplan.entities.Exercise;
import com.example.myfitplan.viewmodels.ExerciseViewModel;
import com.example.myfitplan.viewmodels.WorkoutPlanViewModel;

public class ExerciseDetailFragment extends Fragment {
    private ExerciseViewModel exerciseViewModel;
    private WorkoutPlanViewModel workoutPlanViewModel;
    private Exercise exercise;
    private SeekBar sbReps, sbSets;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exerciseViewModel = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);
        workoutPlanViewModel = new ViewModelProvider(requireActivity()).get(WorkoutPlanViewModel.class);

        Bundle args = getArguments();
        if (args == null || !args.containsKey("exercise_id")) {
            navigateBack();
            return;
        }

        int exerciseId = args.getInt("exercise_id");
        exerciseViewModel.getExerciseById(exerciseId).observe(this, exercise -> {
            if (exercise != null) {
                this.exercise = exercise;
                updateUI();
            } else {
                navigateBack();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_detail, container, false);

        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvDescription = view.findViewById(R.id.tv_description);
        sbReps = view.findViewById(R.id.sb_reps);
        sbSets = view.findViewById(R.id.sb_sets);
        TextView tvRepsValue = view.findViewById(R.id.tv_reps_value);
        TextView tvSetsValue = view.findViewById(R.id.tv_sets_value);

        if (exercise != null) {
            tvName.setText(exercise.getName());
            tvDescription.setText(exercise.getDescription());
            sbReps.setProgress(exercise.getReps());
            sbSets.setProgress(exercise.getSets());
        }

        view.findViewById(R.id.btn_add_to_plan).setOnClickListener(v -> {
            exercise.setReps(sbReps.getProgress());
            exercise.setSets(sbSets.getProgress());
            workoutPlanViewModel.addExerciseToPlan(exercise);
            NavHostFragment.findNavController(this).navigateUp();
        });
        return view;
    }

    private void updateUI() {
        if (getView() == null) return;
        TextView tvName = getView().findViewById(R.id.tv_name);
        TextView tvDescription = getView().findViewById(R.id.tv_description);
        tvName.setText(exercise.getName());
        tvDescription.setText(exercise.getDescription());
        sbReps.setProgress(exercise.getReps());
        sbSets.setProgress(exercise.getSets());
    }

    private void addExerciseToPlan() {
        if (exercise == null) return;
        exercise.setReps(sbReps.getProgress());
        exercise.setSets(sbSets.getProgress());
        workoutPlanViewModel.addExerciseToPlan(exercise);
        navigateBack();
    }

    private void navigateBack() {
        NavHostFragment.findNavController(this).navigateUp();
    }
}