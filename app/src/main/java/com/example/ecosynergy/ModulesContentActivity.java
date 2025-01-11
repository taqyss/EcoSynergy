package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ModulesContentActivity extends BaseActivity {

    // Declare Views
    private ImageButton favoriteButton, downloadButton, transcriptButton;
    private TextView detailTitle, detailDescription, discussionTextView;
    private RecyclerView upNextRecyclerView;
    private ImageView videoContainer;

    // Declare data structure for "Up Next" subcategories
    private List<DataModule.Subcategory> upNextList;
    private ModulesUpNextAdapter upNextAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcategory_content_modules);

        // Initialize Views
        initializeViews();

        // Get Intent data
        int currentSubcategoryId = getIntent().getIntExtra("subcategoryId", -1);
        String currentCategory = getIntent().getStringExtra("Category");
        String subcategory = getIntent().getStringExtra("subcategory");

        Log.d("ModulesContentActivity", "currentSubcategoryId: " + currentSubcategoryId);
        Log.d("ModulesContentActivity", "currentCategory: " + currentCategory);
        Log.d("ModulesContentActivity", "subcategory: " + subcategory);

        // Validate Subcategory ID
        if (currentSubcategoryId == -1) {
            Log.e("ModulesContentActivity", "Invalid subcategory ID.");
            return;
        }

        // Fetch Subcategory data
        DataModule.Subcategory currentSubcategory = DataModule.getSubcategoryById(currentSubcategoryId, currentCategory);
        Log.d("ModulesContentActivity", "Current Subcategory: " + currentSubcategory);

        if (currentSubcategory != null) {
            // Populate Subcategory Content
            populateSubcategoryContent(currentSubcategory);
        } else {
            Log.e("ModulesContentActivity", "Subcategory not found.");
        }

        // Set up Toolbar
        setupToolbar(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(subcategory);
        }

        // Set Button Click Listeners
        setButtonListeners();

        // Set up "Up Next" section
        setupUpNextSection(currentCategory, currentSubcategory);
    }

    // Initialize Views
    private void initializeViews() {
        upNextRecyclerView = findViewById(R.id.RecylcleViewUpNext);
        detailTitle = findViewById(R.id.detail_title);
        detailDescription = findViewById(R.id.detail_description);
        videoContainer = findViewById(R.id.videoContainer);
        favoriteButton = findViewById(R.id.favorite_button);
        downloadButton = findViewById(R.id.download_button);
        transcriptButton = findViewById(R.id.transcript_button);
        discussionTextView = findViewById(R.id.DiscussionUpNext);
    }

    // Populate Subcategory Content
    private void populateSubcategoryContent(DataModule.Subcategory currentSubcategory) {
        detailTitle.setText(currentSubcategory.getVideoTitle());
        detailDescription.setText(currentSubcategory.getVideoDescription());
        // Optionally, load video thumbnail using Glide
        // Glide.with(this).load(currentSubcategory.getVideoThumbnailUrl()).into(videoContainer);
    }

    // Set Button Click Listeners
    private void setButtonListeners() {
        favoriteButton.setOnClickListener(view -> {
            Toast.makeText(this, "Favorite button clicked!", Toast.LENGTH_SHORT).show();
        });

        downloadButton.setOnClickListener(view -> {
            Toast.makeText(this, "Download button clicked!", Toast.LENGTH_SHORT).show();
        });

        transcriptButton.setOnClickListener(view -> {
            Toast.makeText(this, "Transcript button clicked!", Toast.LENGTH_SHORT).show();
        });
    }

    // Setup "Up Next" section
    private void setupUpNextSection(String currentCategory, DataModule.Subcategory currentSubcategory) {
        upNextList = new ArrayList<>();
        List<DataModule.Subcategory> allSubcategories = DataModule.getSubcategoriesForCategory(currentCategory);

        // Add subcategories following the current one
        boolean foundCurrent = false;
        for (DataModule.Subcategory subcategory : allSubcategories) {
            if (foundCurrent) {
                upNextList.add(subcategory);
            }
            if (subcategory.getId() == currentSubcategory.getId()) {
                foundCurrent = true;
            }
        }

        RecyclerView upNextRecyclerView = findViewById(R.id.RecylcleViewUpNext);

        // Set up LayoutManager before setting the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        upNextRecyclerView.setLayoutManager(layoutManager);

        // Set up RecyclerView with the adapter
        upNextAdapter = new ModulesUpNextAdapter(upNextList);
        upNextAdapter.setOnSubcategoryClickListener(subcategory -> {
            // Handle subcategory click, navigate to another activity
            Intent intent = new Intent(ModulesContentActivity.this, ModulesContentActivity.class);
            intent.putExtra("subcategoryId", subcategory.getId());
            intent.putExtra("subcategory", subcategory.getTitle());
            intent.putExtra("Category", currentCategory); // Pass the category again
            startActivity(intent);
        });

        // Set click listener for "Discussion"
//        discussionTextView.setOnClickListener(v -> {
//            Intent intent = new Intent(ModulesContentActivity.this, DiscussionFragment.class);
//            intent.putExtra("subcategory_id", currentSubcategory.getId());
//            intent.putExtra("subcategory_title", currentSubcategory.getTitle());
//            startActivity(intent);
//        });

        upNextRecyclerView.setAdapter(upNextAdapter);
        upNextAdapter.notifyDataSetChanged();
    }

    // Additional methods for Adapter implementation (if needed)
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
