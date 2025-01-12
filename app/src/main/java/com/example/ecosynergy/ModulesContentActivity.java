package com.example.ecosynergy;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ModulesContentActivity extends BaseActivity {

    // Declare Views
    private static final int REQUEST_CODE_PERMISSION = 1236767; // code permission - storage
    private ImageButton favoriteButton, downloadButton, transcriptButton;
    private TextView detailTitle, detailDescription, discussionTextView;
    private RecyclerView upNextRecyclerView;
    private ImageView videoContainer;

    // Declare data structure for "Up Next" subcategories
    private List<DataModule.Subcategory> upNextList;
    private ModulesUpNextAdapter upNextAdapter;

    private DownloadManager manager;

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

        // Set up Toolbar and Bottom Navigation
        setupToolbar(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(subcategory);
        }
        setupBottomNavigation();

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

        // Set up LayoutManager before setting the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        upNextRecyclerView.setLayoutManager(layoutManager);

        // Set up RecyclerView with the adapter
        upNextAdapter = new ModulesUpNextAdapter(upNextList);
        upNextAdapter.setOnSubcategoryClickListener(subcategory -> {
            // Handle subcategory click
            Intent intent = new Intent(ModulesContentActivity.this, ModulesContentActivity.class);
            intent.putExtra("subcategoryId", subcategory.getId());
            intent.putExtra("subcategory", subcategory.getTitle());
            intent.putExtra("Category", currentCategory); // Pass the category again
            startActivity(intent);
        });

        upNextRecyclerView.setAdapter(upNextAdapter);
        upNextAdapter.notifyDataSetChanged();
    }

    // Download Video Method
    private void downloadVideo(String url, String fileName) {
        // Create a DownloadManager.Request for the video URL
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Downloading Video");
        request.setDescription("Please wait while the video is being downloaded.");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // Set the destination path within the Movies directory on external storage
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, fileName);

        // Get the system's DownloadManager service
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            try {
                // Enqueue the download request
                long downloadId = downloadManager.enqueue(request);
                Log.d("Download", "Download started with ID: " + downloadId);
                Toast.makeText(this, "Download started!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("DownloadError", "Error during video download: " + e.getMessage());
                Toast.makeText(this, "Failed to download video", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Additional Adapter Methods (if needed)
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
