package com.example.ecosynergy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodaysPickFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todays_pick, container, false);

        // Initialize RecyclerViews
        RecyclerView didYouKnowRecyclerView = view.findViewById(R.id.recyclerViewDidYouKnow);
        RecyclerView testYourKnowledgeRecyclerView = view.findViewById(R.id.recyclerViewTestYourKnowlegde);
        RecyclerView checkThisOutRecyclerView = view.findViewById(R.id.recyclerViewCheckThisOut);

        // Set up Layout Managers
        didYouKnowRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        testYourKnowledgeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        checkThisOutRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DataTodaysPick dataTodaysPick = new DataTodaysPick();

        // Fetch articles asynchronously
        dataTodaysPick.fetchArticles(new DataTodaysPick.DataCallback<List<Article>>() {
            @Override
            public void onDataFetched(List<Article> data) {
                // Handle the fetched articles
                Log.d("ArticlesFetched", "Fetched " + data.size() + " articles.");
                didYouKnowRecyclerView.setAdapter(new DidYouKnowAdapter(data));
            }
        });

        // Fetch videos asynchronously
        dataTodaysPick.fetchVideos(new DataTodaysPick.DataCallback<List<Video>>() {
            @Override
            public void onDataFetched(List<Video> data) {
                // Handle the fetched videos
                Log.d("VideosFetched", "Fetched " + data.size() + " videos.");
                // Ensure videos are passed to the adapter properly
                CheckThisOutAdapter checkThisOutAdapter = new CheckThisOutAdapter(data);
                checkThisOutRecyclerView.setAdapter(checkThisOutAdapter);
            }
        });

        // Fetch questions asynchronously
        dataTodaysPick.fetchQuestions(new DataTodaysPick.DataCallback<List<Questions>>() {
            @Override
            public void onDataFetched(List<Questions> data) {
                Log.d("QuestionsFetched", "Fetched " + data.size() + " questions.");
                testYourKnowledgeRecyclerView.setAdapter(new TestYourKnowledgeAdapter(data));
            }
        });

        return view;
    }

}
