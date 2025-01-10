package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

public class BasicFragment extends Fragment {

    private BasicAdapter adapter;
    private List<String> category;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_basic, container, false);

        // Initialize category list
        category = DummyData.getAllCategory();
        listView = rootView.findViewById(R.id.basic_list);

        adapter = new BasicAdapter(category, selectedCategory -> {
            Intent intent = NavigationUtils.createSubCategoryIntent(getContext(), selectedCategory, "Basic");
            startActivity(intent);
        });

        // Set the adapter to the ListView
        listView.setAdapter(adapter);

        return rootView;
    }
}
