package com.example.myfitplan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myfitplan.R;
import com.example.myfitplan.entities.WorkoutSchedule;
import org.threeten.bp.LocalDate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private final List<LocalDate> days;
    private LocalDate selectedDate;
    private Set<LocalDate> workoutDates;

    private final OnDayClickListener listener;
    private List<WorkoutSchedule> workouts;
    private LocalDate todayDate = LocalDate.now();

    public CalendarAdapter(List<LocalDate> days,
                           OnDayClickListener listener,
                           Set<LocalDate> workoutDates,
                           LocalDate selectedDate) {
        this.days = days;
        this.listener = listener;
        this.workoutDates = workoutDates != null ? workoutDates : new HashSet<>();
        this.selectedDate = selectedDate;
    }

    public void setWorkouts(List<WorkoutSchedule> workouts) {
        this.workouts = workouts;
        notifyDataSetChanged();
    }

    public void setSelectedDate(LocalDate date) {
        this.selectedDate = date;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_day_item, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        LocalDate date = days.get(position);

        // Безопасная проверка selectedDate
        boolean isSelected = selectedDate != null && date != null && date.equals(selectedDate);
        boolean hasWorkout = workoutDates.contains(date);
        boolean isToday = date != null && date.equals(LocalDate.now());

        // Обновленная логика отображения
        holder.bind(date, hasWorkout, isToday, isSelected);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null && date != null) {
                listener.onDayClick(date);
                setSelectedDate(date);
            }
        });
    }

    private boolean hasWorkout(LocalDate date) {
        if (date == null || workouts == null) return false;
        for (WorkoutSchedule workout : workouts) {
            if (date.equals(workout.date)) return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    static class CalendarViewHolder extends RecyclerView.ViewHolder {
        private final TextView dayText;
        private final View workoutIndicator;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.day_text);
            workoutIndicator = itemView.findViewById(R.id.workout_indicator);
        }

        void bind(LocalDate date, boolean hasWorkout, boolean isToday, boolean isSelected) {
            if (date == null) {
                dayText.setText("");
                workoutIndicator.setVisibility(View.GONE);
                itemView.setBackgroundResource(0);
                return;
            }

            dayText.setText(String.valueOf(date.getDayOfMonth()));
            workoutIndicator.setVisibility(hasWorkout ? View.VISIBLE : View.GONE);

            if (isToday) {
                itemView.setBackgroundResource(R.drawable.today_bg);
            } else if (isSelected) {
                itemView.setBackgroundResource(R.drawable.selected_day_bg);
            } else {
                itemView.setBackgroundResource(R.drawable.day_bg);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        LocalDate date = days.get(position);
        boolean hasWorkout = hasWorkout(date);
        boolean isToday = date != null && date.equals(LocalDate.now());
        boolean isSelected = date != null && date.equals(selectedDate);

        holder.bind(date, hasWorkout, isToday, isSelected);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null && date != null) {
                listener.onDayClick(date);
            }
        });
    }
    public interface OnDayClickListener {
        void onDayClick(LocalDate date);
    }
}