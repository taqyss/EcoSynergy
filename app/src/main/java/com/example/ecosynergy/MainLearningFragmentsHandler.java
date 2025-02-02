package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class MainLearningFragmentsHandler extends Fragment {

    private static final String ARG_CATEGORY_TYPE = "category_type";

    private MainAdapter adapter;
    private List<String> category;
    private ListView listView;

    public static List<String> getAllCategory() {
        List<String> modules = new ArrayList<>();
        modules.add("Solar Energy");
        modules.add("Wind Energy");
        modules.add("Hydro Energy");
        modules.add("Geothermal Energy");
        modules.add("Biomass and Bioenergy Energy");
        modules.add("Ocean Energy");

        return modules;
    }

    // Factory method to create an instance of the fragment
    public static MainLearningFragmentsHandler newInstance(String categoryType) {
        MainLearningFragmentsHandler fragment = new MainLearningFragmentsHandler();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_TYPE, categoryType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the single layout
        View rootView = inflater.inflate(R.layout.fragment_main_list, container, false);

        // Retrieve category type from arguments
        String categoryType = getArguments() != null ? getArguments().getString(ARG_CATEGORY_TYPE) : "";

        // Fetch the category data
        category = getAllCategory();
        listView = rootView.findViewById(R.id.main_list);

        // Set up ListView and Adapter
        adapter = new MainAdapter(category, selectedCategory -> {
            // Check categoryType and perform different actions based on its value
            if ("Basic".equals(categoryType) || "Intermediate".equals(categoryType) || "Advanced".equals(categoryType)) {
                // Handle the valid category types
                Intent intent = NavigationUtils.createSubCategoryIntentModule(getContext(), selectedCategory, categoryType);
                startActivity(intent);
            } else {
                // Log or handle an unexpected category type
                Log.w("MainLearningFragmentsHandler", "Unknown category type: " + categoryType);
                Intent intent = NavigationUtils.createSubCategoryIntentResource(getContext(), selectedCategory, categoryType);
                startActivity(intent);
            }
        });

        // Attach adapter to ListView
        listView.setAdapter(adapter);

        return rootView;
    }
}
