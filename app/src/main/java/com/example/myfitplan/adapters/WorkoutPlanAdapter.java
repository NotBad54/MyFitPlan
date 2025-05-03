package com.example.myfitplan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitplan.R;
import com.example.myfitplan.entities.Exercise;

import java.util.ArrayList;
import java.util.List;

public class WorkoutPlanAdapter extends RecyclerView.Adapter<WorkoutPlanAdapter.ViewHolder> {
    private List<Exercise> exercises;
    private final OnExerciseCheckedListener listener;
    private final OnExerciseClickListener onExerciseClickListener;

    public interface OnExerciseClickListener {
        void onExerciseClick(Exercise exercise);
    }

    public interface OnExerciseCheckedListener {
        void onExerciseChecked(Exercise exercise, boolean isChecked);
    }

    // Обновленный конструктор
    public WorkoutPlanAdapter(List<Exercise> exercises,
                              OnExerciseCheckedListener checkedListener,
                              OnExerciseClickListener clickListener) {
        this.exercises = exercises != null ? exercises : new ArrayList<>();
        this.listener = checkedListener;
        this.onExerciseClickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout_plan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.tvName.setText(exercise.getName());
        holder.checkBox.setChecked(exercise.isSelected());

        // Отключение слушателя перед обновлением, чтобы избежать циклов
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(exercise.isSelected());

        // Обработчик для CheckBox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            exercise.setSelected(isChecked);
            listener.onExerciseChecked(exercise, isChecked);
        });

        // Обработчик клика на весь элемент
        holder.itemView.setOnClickListener(v -> {
            if (onExerciseClickListener != null) {
                onExerciseClickListener.onExerciseClick(exercise);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void submitList(List<Exercise> newExercises) {
        exercises = newExercises != null ? newExercises : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_exercise_name);
            checkBox = itemView.findViewById(R.id.cb_selected);
        }
    }
}