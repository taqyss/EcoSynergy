package com.example.ecosynergy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;
import java.util.List;

public class DiscussionActivity extends BaseActivity {

    private String currentSubcategoryTitle;
    private FirebaseDataFetcher dataFetcher;
    private List<Comment> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        // Set up Toolbar
        setupToolbar(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Discussion");
        }

        setupBottomNavigation();

        dataFetcher = new FirebaseDataFetcher();

        // Fetch all data modules first
        dataFetcher.fetchDataModules(new FirebaseDataFetcher.FirebaseCallback() {
            @Override
            public void onDataFetched(List<DataModule> dataModules) {

                // Get data passed via Intent
                currentSubcategoryTitle = getIntent().getStringExtra("SUBCATEGORY");

                // Pass data to the fragment
                Bundle arguments = new Bundle();
                arguments.putString("SUBCATEGORY", currentSubcategoryTitle);

                // Fetch Subcategory data from Firebase
                fetchCommentsForSubcategory(currentSubcategoryTitle);
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error here, e.g., log it or show a Toast message
                Log.e("ModulesContentActivity", "Error fetching data: " + errorMessage);
                Toast.makeText(com.example.ecosynergy.DiscussionActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }

        });

        // Load the fragment
        DiscussionFragment fragment = new DiscussionFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public static void openDiscussionActivity(Context context, String subcategory) {
        Intent intent = new Intent(context, DiscussionActivity.class);
        intent.putExtra("SUBCATEGORY", subcategory);
        context.startActivity(intent);
    }

    // Method to fetch comments for the current subcategory
    private void fetchCommentsForSubcategory(String subcategory) {
        dataFetcher.fetchCommentsForSubcategory(subcategory, new FirebaseDataFetcher.CommentsCallback() {
            @Override
            public void onCommentsFetched(List<Comment> fetchedComments) {
                comments.clear();
                comments.addAll(fetchedComments);
                updateFragmentComments(fetchedComments);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("DiscussionActivity", "Error fetching comments: " + errorMessage);
                Toast.makeText(DiscussionActivity.this, "Error fetching comments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to store the new comment
    public void storeNewComment(Comment newComment) {
        dataFetcher.storeNewComment(currentSubcategoryTitle, newComment, new FirebaseDataFetcher.StoreCommentCallback() {
            @Override
            public void onCommentStored() {
                Toast.makeText(DiscussionActivity.this, "Comment posted successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(DiscussionActivity.this, "Error posting comment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to update the fragment with fetched comments
    private void updateFragmentComments(List<Comment> comments) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DiscussionFragment fragment = (DiscussionFragment) fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment != null) {
            fragment.displayComments(comments);
        }
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}