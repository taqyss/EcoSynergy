package com.example.ecosynergy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ModulesContentActivity extends BaseActivity {

    // Declare Views
    private ImageButton favoriteButton, downloadButton, transcriptButton;
    private TextView detailTitle, detailDescription;
    private RecyclerView upNextRecyclerView;
    private ImageView videoContainer;

    // Declare data structure for "Up Next" subcategories
    private List<DataModule.Subcategory> upNextList;
    private ModulesUpNextAdapter upNextAdapter;

    private FirebaseDataFetcher dataFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcategory_content_modules);

        // Initialize Views
        initializeViews();

        dataFetcher = new FirebaseDataFetcher();

        // Fetch all data modules first
        dataFetcher.fetchDataModules(new FirebaseDataFetcher.FirebaseCallback() {
            @Override
            public void onDataFetched(List<DataModule> dataModules) {
                // Get Intent data
                String currentCategory = getIntent().getStringExtra("Category");
                String currentSubcategory = getIntent().getStringExtra("subcategory");
                int currentSubcategoryId = getIntent().getIntExtra("subcategoryId", -1);

                Log.d("ModulesContentActivity", "currentSubcategoryId: " + currentSubcategoryId);
                Log.d("ModulesContentActivity", "currentCategory: " + currentCategory);
                Log.d("ModulesContentActivity", "subcategory: " + currentSubcategory);

                // Fetch Subcategory data from Firebase
                fetchSubcategoryFromFirebase(currentCategory, currentSubcategory);
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error here, e.g., log it or show a Toast message
                Log.e("ModulesContentActivity", "Error fetching data: " + errorMessage);
                Toast.makeText(ModulesContentActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }

        });

        // Set up Toolbar and Bottom Navigation
        setupToolbar(true);
        String subcategory = getIntent().getStringExtra("subcategory");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(subcategory);
        }
        setupBottomNavigation();

        // Set Button Click Listeners
        setButtonListeners();
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

    // Initialize Views
    private void initializeViews() {
        upNextRecyclerView = findViewById(R.id.RecylcleViewUpNext);
        detailTitle = findViewById(R.id.detail_title);
        detailDescription = findViewById(R.id.detail_description);
        videoContainer = findViewById(R.id.videoContainer);
        favoriteButton = findViewById(R.id.favorite_button);
        downloadButton = findViewById(R.id.download_button);
        transcriptButton = findViewById(R.id.transcript_button);
    }

    // Fetch Subcategory Data from Firebase
    private void fetchSubcategoryFromFirebase(String currentCategory, String currentSubcategory) {
        dataFetcher.fetchSubcategoryById(currentSubcategory, new FirebaseDataFetcher.SubcategoryCallback() {
            @Override
            public void onSubcategoryFetched(DataModule.Subcategory currentSubcategory) {
                Log.d("ModulesContentActivity", "Subcategory fetched:");
                populateSubcategoryContent(currentSubcategory);
                setupUpNextSection(currentCategory, currentSubcategory);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("ModulesContentActivity", errorMessage);
                Toast.makeText(ModulesContentActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
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
        final boolean[] isFavorite = {false}; // Track the state

        favoriteButton.setOnClickListener(view -> {
            if (isFavorite[0]) {
                favoriteButton.setImageResource(R.drawable.ic_favourite);
                Toast.makeText(this, "Removed from favorites!", Toast.LENGTH_SHORT).show();
            } else {
                favoriteButton.setImageResource(R.drawable.ic_favourite_full);
                Toast.makeText(this, "Added to favorites!", Toast.LENGTH_SHORT).show();
            }
            isFavorite[0] = !isFavorite[0]; // Toggle state
        });

        downloadButton.setOnClickListener(view -> {
            int currentId = getIntent().getIntExtra("subcategoryId", -1);
            String videoUrl = DataModule.getURLBasedOnID(currentId);
            if ("URL is not available".equals(videoUrl)) {
                Toast.makeText(this, "Video URL is not available", Toast.LENGTH_SHORT).show();
                return;
            }
            String fileName = "video.mp4"; // Name of the file to save
            downloadVideo(videoUrl, fileName);
        });

        transcriptButton.setOnClickListener(view -> {
            int currentId = getIntent().getIntExtra("subcategoryId", -1);
            String videoUrl = DataModule.getURLBasedOnID(currentId);

            // Check if the URL is valid
            if (videoUrl != null && !videoUrl.isEmpty()) {
                // Display a toast indicating the YouTube video is opening
                Toast.makeText(this, "Opening YouTube...", Toast.LENGTH_SHORT).show();

                // Create an Intent to open the YouTube video
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                youtubeIntent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://com.google.android.youtube")); // Force to open YouTube app if installed
                startActivity(youtubeIntent); // Launch YouTube or a web browser
            } else {
                // Display a toast indicating the link is corrupt
                Toast.makeText(this, "Link is unavailable", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Setup "Up Next" section
    private void setupUpNextSection(String currentCategory, DataModule.Subcategory currentSubcategory) {
        upNextList = new ArrayList<>();

        dataFetcher.fetchDataModules(new  FirebaseDataFetcher.FirebaseCallback() {
            @Override
            public void onDataFetched(List<DataModule> dataModules) {
                // Loop through modules to find the matching category and level
                for (DataModule dataModule : dataModules) {
                    if (dataModule.getCategory().equals(currentCategory)) {
                        for (DataModule.Subcategory subcategory : dataModule.getSubcategories()) {
                            if (subcategory.getId() != currentSubcategory.getId()) {
                                upNextList.add(subcategory); // Add to "Up Next" list
                            }
                        }
                    }
                }

                // Set up LayoutManager before setting the adapter
                LinearLayoutManager layoutManager = new LinearLayoutManager(ModulesContentActivity.this);
                upNextRecyclerView.setLayoutManager(layoutManager);

                // Set up RecyclerView with the adapter
                upNextAdapter = new ModulesUpNextAdapter(upNextList);
                upNextRecyclerView.setAdapter(upNextAdapter);
                upNextAdapter.notifyDataSetChanged();
            }
            @Override
            public void onError(String errorMessage) {
                // Handle error here, e.g., log it or show a Toast message
                Log.e("ModulesContentActivity", "Error fetching data: " + errorMessage);
                Toast.makeText(ModulesContentActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }

        });
    }

    // Download Video Method
    private void downloadVideo(String url, String fileName) {
        // Download logic
    }
}