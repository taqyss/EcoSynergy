package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class MainLearningFragmentsHandler extends Fragment {

    private static final String ARG_CATEGORY_TYPE = "category_type";

    private MainAdapter adapter;
    private List<String> category;
    private ListView listView;

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
        category = DummyData.getAllCategory();

        // Set up ListView and Adapter
        listView = rootView.findViewById(R.id.main_list);
        adapter = new MainAdapter(category, selectedCategory -> {
            Intent intent = NavigationUtils.createSubCategoryIntent(getContext(), selectedCategory, categoryType);
            startActivity(intent);
        });

        // Attach adapter to ListView
        listView.setAdapter(adapter);

        return rootView;
    }
}
