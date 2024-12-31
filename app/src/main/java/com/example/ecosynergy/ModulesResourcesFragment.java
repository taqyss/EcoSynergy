package com.example.ecosynergy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class ModulesResourcesFragment extends Fragment {

    private ListView contentListView;
    private Button modulesButton;
    private Button resourcesButton;

    private ArrayAdapter<String> listAdapter;
    private List<String> dataList;

    // Use newInstance method to create the fragment
    public static ModulesResourcesFragment newInstance() {
        return new ModulesResourcesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_module_resource_main, container, false);

        contentListView = view.findViewById(R.id.content_list);
        modulesButton = view.findViewById(R.id.tab_modules);
        resourcesButton = view.findViewById(R.id.tab_resources);

        // Initialize the data list and adapter
        dataList = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dataList);
        contentListView.setAdapter(listAdapter);

        // Set click listeners for tabs
        modulesButton.setOnClickListener(v -> loadModules());
        resourcesButton.setOnClickListener(v -> loadResources());

        // Load initial content (Modules by default)
        loadModules();

        return view;
    }

    private void loadModules() {
        // Change the tab color or style to indicate it is selected
        modulesButton.setBackgroundResource(R.drawable.chosen_button_shape);
        resourcesButton.setBackgroundColor(getResources().getColor(R.color.primaryColor));

        // Update the content list for Modules
        dataList.clear();
        dataList.add("Module 1");
        dataList.add("Module 2");
        dataList.add("Module 3");  // Example data
        listAdapter.notifyDataSetChanged();
    }

    private void loadResources() {
        // Change the tab color or style to indicate it is selected
        modulesButton.setBackgroundColor(getResources().getColor(R.color.primaryColor));
        resourcesButton.setBackgroundResource(R.drawable.chosen_button_shape);

        // Update the content list for Resources
        dataList.clear();
        dataList.add("Resource 1");
        dataList.add("Resource 2");
        dataList.add("Resource 3");  // Example data
        listAdapter.notifyDataSetChanged();
    }
}
