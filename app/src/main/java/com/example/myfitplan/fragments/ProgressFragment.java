package com.example.myfitplan.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.myfitplan.R;
import com.example.myfitplan.entities.ProgressEntry;
import com.example.myfitplan.viewmodels.ProgressViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class ProgressFragment extends Fragment {
    private LineChart weightChart;
    private ProgressViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        initViews(view);
        setupViewModel();
        return view;
    }

    private void initViews(View view) {
        weightChart = view.findViewById(R.id.weight_chart);
        view.findViewById(R.id.btn_add_weight).setOnClickListener(v -> showAddWeightDialog());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ProgressViewModel.class);
        viewModel.getCurrentUserId(new ProgressViewModel.UserIdCallback() {
            @Override
            public void onUserIdReceived(int userId) {
                if (userId != -1) {
                    viewModel.loadWeightEntries(userId);
                    viewModel.getWeightEntries().observe(getViewLifecycleOwner(), entries -> {
                        List<Float> weights = new ArrayList<>();
                        for (ProgressEntry entry : entries) weights.add(entry.weight);
                        updateChartData(weights);
                    });
                }
            }
        });
    }

    private void showAddWeightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_weight, null);
        EditText etWeight = dialogView.findViewById(R.id.et_weight);

        builder.setView(dialogView)
                .setTitle("Добавить вес")
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    try {
                        float weight = Float.parseFloat(etWeight.getText().toString());
                        LocalDate date = LocalDate.now();
                        viewModel.getCurrentUserId(userId -> {
                            if (userId != -1) {
                                viewModel.addWeightEntry(weight, date, userId);
                            }
                        });
                    } catch (NumberFormatException e) {
                        Toast.makeText(requireContext(), "Некорректное значение", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void updateChartData(List<Float> weights) {
        if (weights.isEmpty()) return;

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < weights.size(); i++) {
            entries.add(new Entry(i, weights.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Вес (кг)");
        dataSet.setColor(Color.RED);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        weightChart.setData(lineData);
        weightChart.invalidate();
    }
}