package com.example.myfitplan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myfitplan.R;
import com.example.myfitplan.adapters.CalendarAdapter;
import com.example.myfitplan.entities.Exercise;
import com.example.myfitplan.entities.ScheduledWorkout;
import com.example.myfitplan.entities.WorkoutPlan;
import com.example.myfitplan.viewmodels.UserProfileViewModel;
import com.example.myfitplan.viewmodels.WorkoutPlanViewModel;
import com.example.myfitplan.viewmodels.WorkoutViewModel;
import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;
import org.threeten.bp.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CalendarFragment extends Fragment {

    private RecyclerView calendarRecyclerView;
    private TextView tvWorkoutDetails;
    private WorkoutViewModel workoutViewModel;
    private LocalDate currentDate;
    private LocalDate selectedDate = LocalDate.now();
    private WorkoutPlanViewModel workoutPlanViewModel;
    private UserProfileViewModel userProfileViewModel;
    private Set<LocalDate> workoutDates = new HashSet<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        currentDate = LocalDate.now();
        initViews(view);
        setupViewModel();
        setupButtonListeners();
        setupCalendar();
        return view;
    }

    private void initViews(View view) {
        calendarRecyclerView = view.findViewById(R.id.calendar_recycler);
        monthYearText = view.findViewById(R.id.month_year_text);
        btnPrevious = view.findViewById(R.id.previous_month_button);
        btnNext = view.findViewById(R.id.next_month_button);
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 7));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        setupCalendar();
    }

    private void setupViewModel() {
        workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);
        workoutPlanViewModel = new ViewModelProvider(requireActivity()).get(WorkoutPlanViewModel.class);
        userProfileViewModel = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);
    }

    private void setupCalendar() {
        monthYearText.setText(formatMonthYear(currentDate));
        List<LocalDate> daysInMonth = getDaysInMonth(currentDate);

        CalendarAdapter adapter = new CalendarAdapter(daysInMonth, date -> {
            selectedDate = date;
            updateCalendarSelection(date);
            loadWorkoutPlan(date);
        });

        calendarRecyclerView.setAdapter(adapter);
        loadWorkoutsForMonth(currentDate);
    }

    private void updateCalendar() {
        TextView monthYearText = getView().findViewById(R.id.month_year_text);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("ru"));
        monthYearText.setText(currentDate.format(formatter));

        requireView().findViewById(R.id.previous_month_button).setOnClickListener(v -> {
            currentDate = currentDate.minusMonths(1);
            updateCalendar();
        });

        requireView().findViewById(R.id.next_month_button).setOnClickListener(v -> {
            currentDate = currentDate.plusMonths(1);
            updateCalendar();
        });

        List<LocalDate> daysInMonth = getDaysInMonth(currentDate);
        CalendarAdapter adapter = new CalendarAdapter(daysInMonth, this::onDayClick, workoutDates, selectedDate);
        calendarRecyclerView.setAdapter(adapter);
        loadWorkoutsForMonth(currentDate);
    }

    private List<LocalDate> getDaysInMonth(LocalDate date) {
        List<LocalDate> days = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        LocalDate firstOfMonth = date.withDayOfMonth(1);

        // Начинаем неделю с понедельника (1 = Пн, 7 = Вс)
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        int emptyDays = (dayOfWeek == 1) ? 0 : dayOfWeek - 1;

        for (int i = 0; i < emptyDays; i++) {
            days.add(null);
        }

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            days.add(LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), day));
        }

        return days;
    }

    private void onDayClick(LocalDate date) {
        userProfileViewModel.getCurrentUserId(userId -> {
            workoutViewModel.getScheduledWorkoutByDate(userId, date).observe(getViewLifecycleOwner(), scheduledWorkout -> {
                if (scheduledWorkout != null) {
                    workoutPlanViewModel.getPlanById(scheduledWorkout.getPlanId()).observe(getViewLifecycleOwner(), plan -> {
                        if (plan != null) {
                            displayWorkoutDetails(plan);
                        }
                    });
                } else {
                    showScheduleDialog(date);
                }
            });
        });
    }

    private void showScheduleDialog(LocalDate date) {
        new AlertDialog.Builder(requireContext())
                .setMessage("Запланировать тренировку?")
                .setPositiveButton("Да", (d, w) -> navigateToCreatePlan(date))
                .setNegativeButton("Нет", null)
                .show();
    }

    private void navigateToCreatePlan(LocalDate date) {
        Bundle args = new Bundle();
        args.putSerializable("selected_date", date);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_calendar_to_workout_plan, args);
    }

    private void displayWorkoutDetails(WorkoutPlan plan) {
        StringBuilder details = new StringBuilder("План: ").append(plan.getName()).append("\n");
        for (Exercise exercise : plan.getExercises()) {
            details.append("- ")
                    .append(exercise.getName())
                    .append(" (")
                    .append(exercise.getSets())
                    .append("x")
                    .append(exercise.getReps())
                    .append(")\n");
        }
        tvWorkoutDetails.setText(details.toString());
    }

    private void loadWorkoutsForMonth(LocalDate date) {
        userProfileViewModel.getCurrentUserId(userId -> {
            LocalDate start = date.withDayOfMonth(1);
            LocalDate end = date.withDayOfMonth(date.lengthOfMonth());
            workoutViewModel.getScheduledWorkoutsByDate(userId, start, end).observe(getViewLifecycleOwner(), workouts -> {
                if (workouts != null) {
                    workoutDates.clear();
                    for (ScheduledWorkout workout : workouts) {
                        workoutDates.add(workout.getDate());
                    }
                    updateCalendar();
                }
            });
        });
    }
}