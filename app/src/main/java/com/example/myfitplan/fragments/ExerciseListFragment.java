package com.example.myfitplan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitplan.R;
import com.example.myfitplan.adapters.ExerciseAdapter;
import com.example.myfitplan.viewmodels.ExerciseViewModel;
import com.example.myfitplan.viewmodels.WorkoutPlanViewModel;

import java.util.ArrayList;

public class ExerciseListFragment extends Fragment {
    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private ExerciseViewModel exerciseViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);
        recyclerView = view.findViewById(R.id.rv_exercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Убрано возвращаемое значение из лямбды
        adapter = new ExerciseAdapter(new ArrayList<>(), exercise -> {
            WorkoutPlanViewModel planViewModel =
                    new ViewModelProvider(requireActivity()).get(WorkoutPlanViewModel.class);
            planViewModel.addExerciseToPlan(exercise);
        });

        recyclerView.setAdapter(adapter);

        exerciseViewModel = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);
        exerciseViewModel.getAllExercises().observe(getViewLifecycleOwner(), exercises -> {
            adapter.updateExercises(exercises);
        });

        return view;
    }
}
