package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

public class ToolkitsFragment extends Fragment {

    private ToolkitsAdapter adapter;
    private List<String> category;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_toolkits, container, false);

        category = DummyData.getAllCategory();
        listView = rootView.findViewById(R.id.toolkits_list);
        adapter = new ToolkitsAdapter(category);

        adapter = new ToolkitsAdapter(category, selectedCategory -> {
            Intent intent = NavigationUtils.createSubCategoryIntent(getContext(), selectedCategory, "Basic");
            startActivity(intent);
        });

        listView.setAdapter(adapter);

        return rootView;
    }
}