package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntermediateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntermediateFragment extends Fragment {

    private IntermediateAdapter adapter;
    private List<String> category;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_intermediate, container, false);

        category = DummyData.getAllCategory();
        listView = rootView.findViewById(R.id.intermediate_list);
        adapter = new IntermediateAdapter(category);

        adapter = new IntermediateAdapter(category, selectedCategory -> {
            Intent intent = NavigationUtils.createSubCategoryIntent(getContext(), selectedCategory, "Basic");
            startActivity(intent);
        });

        listView.setAdapter(adapter);

        return rootView;
    }
}