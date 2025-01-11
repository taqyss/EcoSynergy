package com.example.ecosynergy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TodaysPickFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todays_pick, container, false);

        // Initialize RecyclerViews
        RecyclerView didYouKnowRecyclerView = view.findViewById(R.id.recyclerViewDidYouKnow);
        RecyclerView checkThisOutRecyclerView = view.findViewById(R.id.recyclerViewCheckThisOut);
        RecyclerView testYourKnowledgeRecyclerView = view.findViewById(R.id.recyclerViewTestYourKnowlegde);

        // Set up Adapters and Layout Managers
        didYouKnowRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        checkThisOutRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        testYourKnowledgeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Example: Setting dummy data for each section
        didYouKnowRecyclerView.setAdapter(new DidYouKnowAdapter(DummyData.getDummyArticles()));
        checkThisOutRecyclerView.setAdapter(new CheckThisOutAdapter(DummyData.getDummyVideos()));
        testYourKnowledgeRecyclerView.setAdapter(new TestYourKnowledgeAdapter(DummyData.getDummyQuestions()));

        return view;
    }
}
