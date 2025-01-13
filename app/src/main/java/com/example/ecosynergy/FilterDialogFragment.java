package com.example.ecosynergy;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class FilterDialogFragment extends DialogFragment {

    public interface FilterListener {
        void onFilterApplied(String category, String status);
    }

    private FilterListener filterListener;

    public void setFilterListener(FilterListener listener) {
        this.filterListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String[] categories = {"All", "Energy access", "Renewable Energy", "Sustainability and Social Impact", "Clean Energy Technology", "Policy and Economics", "Energy Efficiency"};
        String[] statuses = {"All", "Not Started", "In Progress", "Completed"};

        final String[] selectedCategory = {"All"};
        final String[] selectedStatus = {"All"};

        return new AlertDialog.Builder(requireContext())
                .setTitle("Filter Projects")
                .setSingleChoiceItems(categories, 0, (dialogInterface, i) -> selectedCategory[0] = categories[i])
                .setNeutralButton("Status", (dialog, which) -> {
                    // Show a second dialog for selecting status
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Select Status")
                            .setSingleChoiceItems(statuses, 0, (dialog1, i1) -> selectedStatus[0] = statuses[i1])
                            .setPositiveButton("Apply", (dialog1, which1) -> {
                                if (filterListener != null) {
                                    filterListener.onFilterApplied(selectedCategory[0], selectedStatus[0]);
                                }
                            })
                            .show();
                })
                .setPositiveButton("Apply", (dialog, which) -> {
                    if (filterListener != null) {
                        filterListener.onFilterApplied(selectedCategory[0], selectedStatus[0]);
                    }
                })
                .create();
    }
}
