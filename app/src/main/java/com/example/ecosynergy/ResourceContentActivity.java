package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceContentActivity extends BaseActivity {

    private ImageButton favoriteButton, shareButton, textToSpeechButton;
    private TextView detailTitle, detailDescription;
    private RecyclerView upNextRecyclerView;

    private List<DataResource.Subcategory> upNextList;
    private ResourcesUpNextAdapter upNextAdapter;

    private boolean isFavorite = false; // Track the favorite status
    private FirebaseAuth mAuth;
    private DatabaseReference favoritesRef;

    int currentSubcategoryId;

    String currentCategory, subcategory, branch, articleTitle;
    private FirebaseResourceFetcher firebaseResourceFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcategory_content_articles);

        initializeViews();

        // Firebase initialization for recent activities
        DatabaseReference recentActivitiesRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("recent_activities");

        // Fetch intent data
        String category = getIntent().getStringExtra("Category");
        String articleTitle = getIntent().getStringExtra("articleTitle");
        int subcategoryId = getIntent().getIntExtra("subcategoryId", -1);
        String subcategory = getIntent().getStringExtra("subcategory");
        String branch = getIntent().getStringExtra("HIERARCHY");

        // Validate intent data
        if (category == null || articleTitle == null || subcategoryId == -1 || subcategory == null) {
            Log.e("ResourceContentActivity", "Missing or invalid intent data.");
            Toast.makeText(this, "Unable to load article. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Log recent activity for the article
        logRecentActivity(recentActivitiesRef, "article", articleTitle, subcategoryId);

        // Populate UI with article data
        DataResource.Subcategory currentSubcategory = DataResource.getSubcategoryById(subcategoryId, category);
        if (currentSubcategory != null) {
            populateSubcategoryContent(currentSubcategory);
        } else {
            Log.e("ResourceContentActivity", "Subcategory not found.");
        }

        // Fetch article content
        firebaseResourceFetcher = new FirebaseResourceFetcher();
        fetchSubcategoryContent(category, articleTitle, branch);

        // Setup UI
        setupToolbar(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(subcategory);
        }
        setupBottomNavigation();
        setButtonListeners(subcategoryId, currentSubcategory);
        setupUpNextSection(category, currentSubcategory);
    }


    private void fetchSubcategoryContent(String category, String articleTitle, String branch) {
        firebaseResourceFetcher.fetchSubcategoryContent(category, articleTitle, branch, new FirebaseResourceFetcher.SubcategoryCallback() {

            @Override
            public void onError(Exception error) {
                Log.e("ResourceContentActivity", "Error fetching subcategory: " + error.getMessage());
                Toast.makeText(ResourceContentActivity.this, "Error fetching content", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDataFetched(List<DataResource> dataResources) {}

            @Override
            public void onSuccess(Object result) {}

            @Override
            public void onSubcategoryFetched(DataResource.Subcategory subcategory) {
                populateSubcategoryContent(subcategory);
            }

            @Override
            public void onDataFetchedResource(List<DataResource> dataResources) {

            }
        });
    }

    private void initializeViews() {
        detailTitle = findViewById(R.id.articleTitle);
        detailDescription = findViewById(R.id.articleContent);
        firebaseResourceFetcher = new FirebaseResourceFetcher();
        upNextRecyclerView = findViewById(R.id.RecylcleViewUpNext);
        detailTitle = findViewById(R.id.detail_title);
        detailDescription = findViewById(R.id.detail_description);
        favoriteButton = findViewById(R.id.favorite_button);
        shareButton = findViewById(R.id.share_button);
    }

    private void populateSubcategoryContent(DataResource.Subcategory currentSubcategory) {
        detailTitle.setText(currentSubcategory.getArticleTitle());
        detailDescription.setText(currentSubcategory.getArticleContent());
    }

    private void setButtonListeners(int subcategoryId, DataResource.Subcategory currentSubcategory) {
        favoriteButton.setOnClickListener(view -> {
            toggleFavorite(subcategoryId, currentSubcategory.getArticleTitle());
        });

        shareButton.setOnClickListener(view -> {
            shareContent(currentSubcategory.getArticleTitle(), currentSubcategory.getArticleContent());
        });
    }

    private void toggleFavorite(int subcategoryId, String articleTitle) {
        isFavorite = !isFavorite;

        if (isFavorite) {
            favoriteButton.setImageResource(R.drawable.ic_favourite_full);
            saveFavorite(subcategoryId, articleTitle);
            Toast.makeText(this, "Added to favorites!", Toast.LENGTH_SHORT).show();
        } else {
            favoriteButton.setImageResource(R.drawable.ic_favourite);
            removeFavorite(subcategoryId);
            Toast.makeText(this, "Removed from favorites!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveFavorite(int subcategoryId, String articleTitle) {
        Map<String, Object> favoriteData = new HashMap<>();
        favoriteData.put("id", subcategoryId);
        favoriteData.put("title", articleTitle);

        favoritesRef.child(String.valueOf(subcategoryId)).setValue(favoriteData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Favorites", "Favorite saved successfully.");
                    } else {
                        Log.e("Favorites", "Failed to save favorite.", task.getException());
                    }
                });
    }

    private void removeFavorite(int subcategoryId) {
        favoritesRef.child(String.valueOf(subcategoryId)).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Favorites", "Favorite removed successfully.");
                    } else {
                        Log.e("Favorites", "Failed to remove favorite.", task.getException());
                    }
                });
    }

    private void shareContent(String title, String content) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);

        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    private void setupUpNextSection(String currentCategory, DataResource.Subcategory currentSubcategory) {
        upNextList = new ArrayList<>();
        List<DataResource.Subcategory> allSubcategories = DataResource.getSubcategoriesForCategory(currentCategory);

        boolean foundCurrent = false;
        for (DataResource.Subcategory subcategory : allSubcategories) {
            if (foundCurrent) {
                upNextList.add(subcategory);
            }
            if (subcategory.getId() == currentSubcategory.getId()) {
                foundCurrent = true;
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        upNextRecyclerView.setLayoutManager(layoutManager);

        upNextAdapter = new ResourcesUpNextAdapter(upNextList);
        upNextAdapter.setOnSubcategoryClickListener(subcategory -> {
            Intent intent = new Intent(ResourceContentActivity.this, ResourceContentActivity.class);
            intent.putExtra("subcategoryId", subcategory.getId());
            intent.putExtra("subcategory", subcategory.getArticleTitle());
            intent.putExtra("Category", currentCategory);
            startActivity(intent);
        });

        upNextRecyclerView.setAdapter(upNextAdapter);
        upNextAdapter.notifyDataSetChanged();
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

    private void logRecentActivity(DatabaseReference recentActivitiesRef, String activityType, String title, int referenceId) {
        long timestamp = System.currentTimeMillis();
        Query query = recentActivitiesRef.orderByChild("referenceId").equalTo(String.valueOf(referenceId));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Update timestamp for existing activity
                    for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                        activitySnapshot.getRef().child("timestamp").setValue(timestamp);
                    }
                } else {
                    // Add new recent activity
                    String activityId = recentActivitiesRef.push().getKey();
                    DashboardRecentActivity recentActivity = new DashboardRecentActivity(
                            activityId, activityType, title, timestamp, String.valueOf(referenceId)
                    );
                    if (activityId != null) {
                        recentActivitiesRef.child(activityId).setValue(recentActivity);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ResourceContentActivity", "Error logging recent activity: " + error.getMessage());
            }
        });
    }

}
