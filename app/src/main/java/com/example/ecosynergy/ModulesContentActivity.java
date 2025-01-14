package com.example.ecosynergy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class ModulesContentActivity extends BaseActivity {

    // Declare Views
    private ImageButton favoriteButton, downloadButton, transcriptButton;
    private TextView detailTitle, detailDescription;
    private RecyclerView upNextRecyclerView;
    private VideoView videoContainer;  // Changed from ImageView to VideoView

    // Declare data structure for "Up Next" subcategories
    private List<DataModule.Subcategory> upNextList;
    private ModulesUpNextAdapter upNextAdapter;

    private FirebaseDataFetcher dataFetcher;
    private DatabaseReference recentActivitiesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcategory_content_modules);

        // Initialize Views
        initializeViews();

        // Initialize Firebase reference
        setupFirebase();

        dataFetcher = new FirebaseDataFetcher();

        // Fetch all data modules first
        dataFetcher.fetchDataModules(new FirebaseDataFetcher.FirebaseCallback() {
            @Override
            public void onDataFetchedModules(List<DataModule> dataModules) {
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

    private void setupFirebase() {
        recentActivitiesRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("recent_activities");
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
        detailDescription = findViewById(R.id.detail_description); // Initialize VideoView
        favoriteButton = findViewById(R.id.favorite_button);
        downloadButton = findViewById(R.id.download_button);
    }

    // Fetch Subcategory Data from Firebase
    private void fetchSubcategoryFromFirebase(String currentCategory, String currentSubcategory) {
        // Fetch subcategory ID from the intent
        int currentSubcategoryId = getIntent().getIntExtra("subcategoryId", -1);

        // Make sure category and subcategory are not empty before calling
        if (currentCategory == null || currentSubcategory == null) {
            Log.e("ModulesContentActivity", "Category or Subcategory is null.");
            return;
        }

        // Fetch subcategory data by title
        dataFetcher.fetchSubcategoryById(currentSubcategory, new FirebaseDataFetcher.SubcategoryCallback() {
            @Override
            public void onSubcategoryFetched(DataModule.Subcategory fetchedSubcategory) {
                Log.d("ModulesContentActivity", "Subcategory fetched: " + fetchedSubcategory.getTitle());

                // Populate the UI with fetched subcategory data
                populateSubcategoryContent(fetchedSubcategory);

                // Set up the "Up Next" section (based on current category and subcategory)
                setupUpNextSection(currentCategory, fetchedSubcategory);

                // Log recent activity for tracking user interactions with the module
                String moduleLevel = currentCategory;  // Category (level) of the subcategory
                String moduleName = fetchedSubcategory.getTitle();  // Subcategory title
                int subcategoryId = fetchedSubcategory.getId();  // Unique subcategory ID

                logRecentActivity(moduleLevel, moduleName, subcategoryId);
            }

            @Override
            public void onError(String errorMessage) {
                // Handle errors: Log and show a toast message
                Log.e("ModulesContentActivity", errorMessage);
                Toast.makeText(ModulesContentActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Populate Subcategory Content
    YouTubePlayerView youTubePlayerView;
    private void populateSubcategoryContent(DataModule.Subcategory currentSubcategory) {
        // Set video title and description
        detailTitle.setText(currentSubcategory.getVideoTitle());
        detailDescription.setText(currentSubcategory.getVideoDescription());

        YouTubePlayerView youTubePlayerView = findViewById(R.id.videoContainer);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                // Now the YouTube player is ready for interaction
                String videoUrl = currentSubcategory.getContentUrl();
                Log.d("ModulesContentActivity", "Video URL: " + videoUrl);

                if (videoUrl != null && !videoUrl.isEmpty()) {
                    String videoId = null;

                    // Check if the URL is in the shortened format (youtu.be)
                    if (videoUrl.contains("youtu.be")) {
                        // Extract video ID from shortened URL (https://youtu.be/VIDEO_ID?...)
                        String[] urlParts = videoUrl.split("/");
                        if (urlParts.length > 1) {
                            videoId = urlParts[1].split("\\?")[0];  // Extract video ID before any query parameters
                            Log.d("ModulesContentActivity", "Extracted video ID: " + videoId.toString());
                        }
                    } else if (videoUrl.contains("youtube.com")) {
                        // Extract video ID from standard YouTube URL (https://www.youtube.com/watch?v=VIDEO_ID...)
                        String[] urlParts = videoUrl.split("v=");
                        if (urlParts.length > 1) {
                            videoId = urlParts[1].split("&")[0];  // Extract video ID before any additional query parameters
                            Log.d("ModulesContentActivity", "Extracted video ID: " + videoId);
                        }
                    }

                    Log.d("ModulesContentActivity", "Video URL: " + videoUrl);
                    Log.d("ModulesContentActivity", "Extracted video ID: " + videoId);

                    if (videoId != null && !videoId.isEmpty()) {
                        try {
                            // Cue the video using the YouTubePlayer
                            youTubePlayer.cueVideo(videoId, 0);  // 0 seconds offset (you can modify this if needed)
                        } catch (Exception e) {
                            Log.e("ModulesContentActivity", "Error initializing YouTube Player", e);
                        }
                    } else {
                        Toast.makeText(ModulesContentActivity.this, "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ModulesContentActivity.this, "Video URL is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (youTubePlayerView != null) {
            youTubePlayerView.release();  // Release the player to avoid interaction with dead objects
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (youTubePlayerView != null) {
            youTubePlayerView.release();
        }
    }

    // Set Button Click Listeners
    private void setButtonListeners() {
        final boolean[] isFavorite = {false};

        favoriteButton.setOnClickListener(view -> {
            if (isFavorite[0]) {
                favoriteButton.setImageResource(R.drawable.ic_favourite);
                Toast.makeText(this, "Removed from favorites!", Toast.LENGTH_SHORT).show();
            } else {
                favoriteButton.setImageResource(R.drawable.ic_favourite_full);
                Toast.makeText(this, "Added to favorites!", Toast.LENGTH_SHORT).show();
            }
            isFavorite[0] = !isFavorite[0];  // Toggle state
        });

        downloadButton.setOnClickListener(view -> {
            int currentId = getIntent().getIntExtra("subcategoryId", -1);
            String videoUrl = DataModule.getURLBasedOnID(currentId);
            if ("URL is not available".equals(videoUrl)) {
                Toast.makeText(this, "Video URL is not available", Toast.LENGTH_SHORT).show();
                return;
            }
            String fileName = "video.mp4";
            downloadVideo(videoUrl, fileName);
        });
    }

    private void setupUpNextSection(String currentCategory, DataModule.Subcategory currentSubcategory) {
        upNextList = new ArrayList<>();

        dataFetcher.fetchDataModules(new FirebaseDataFetcher.FirebaseCallback() {
            @Override
            public void onDataFetchedModules(List<DataModule> dataModules) {
                for (DataModule dataModule : dataModules) {
                    if (dataModule.getCategory().equals(currentCategory)) {
                        for (DataModule.Subcategory subcategory : dataModule.getSubcategories()) {
                            if (subcategory.getId() != currentSubcategory.getId()) {
                                upNextList.add(subcategory);
                            }
                        }
                    }
                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(ModulesContentActivity.this);
                upNextRecyclerView.setLayoutManager(layoutManager);
                upNextAdapter = new ModulesUpNextAdapter(upNextList);
                upNextRecyclerView.setAdapter(upNextAdapter);
                upNextAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("ModulesContentActivity", "Error fetching data: " + errorMessage);
                Toast.makeText(ModulesContentActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadVideo(String url, String fileName) {
        // Implement the download logic here
    }

    private void logRecentActivity(String moduleLevel, String moduleName, int subcategoryId) {
        String activityType = "module - " + moduleLevel;
        String activityTitle = moduleName;
        long timestamp = System.currentTimeMillis();

        Query query = recentActivitiesRef.orderByChild("title").equalTo(activityTitle);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                        activitySnapshot.getRef().child("timestamp").setValue(timestamp);
                    }
                } else {
                    String activityId = recentActivitiesRef.push().getKey();
                    DashboardRecentActivity recentActivity = new DashboardRecentActivity(
                            activityId, activityType, activityTitle, timestamp, String.valueOf(subcategoryId)
                    );
                    if (activityId != null) {
                        recentActivitiesRef.child(activityId).setValue(recentActivity);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ModulesContentActivity", "Error logging recent activity: " + error.getMessage());
            }
        });
    }
}
