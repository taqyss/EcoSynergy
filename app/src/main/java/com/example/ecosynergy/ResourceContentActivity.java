package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        firebaseResourceFetcher = new FirebaseResourceFetcher();

        firebaseResourceFetcher.fetchDataResource(new FirebaseResourceFetcher.SubcategoryCallback() {
            @Override
            public void onDataFetched(List<DataResource> dataResources) {

                // Get Intent data
                String category = getIntent().getStringExtra("Category");
                Log.d("ResourceContentActivity", "Category: " + category);
                String subcategory = getIntent().getStringExtra("subcategory");
                articleTitle = getIntent().getStringExtra("articleTitle");
                String branch = getIntent().getStringExtra("HIERARCHY");
                currentSubcategoryId = getIntent().getIntExtra("subcategoryId", -1);


                if (category != null && subcategory != null && branch != null) {
                    Log.d("ResourceContentActivity", "Category: " + category);
                    Log.d("ResourceContentActivity", "Subcategory: " + subcategory);
                    Log.d("ResourceContentActivity", "Hierarchy: " + branch);

                    // Fetch and display the content based on the passed data

                    Log.d("ResourceContentActivity", "Fetching subcategory content for:" + category);
                    Log.d("ResourceContentActivity", "Fetching subcategory title for:" + articleTitle);
                    fetchSubcategoryContent(category, articleTitle, branch);
                } else {
                    Log.e("ResourceContentActivity", "Category or article title missing from intent.");
                }

                if (category != null && subcategory != null && branch != null) {
                    fetchSubcategoryContent(category, articleTitle, branch);
                } else {
                    Log.e("ResourceContentActivity", "Category or article title missing from intent.");
                }

                fetchSubcategoryContent(category, articleTitle, branch);
            }

            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onSubcategoryFetched(DataResource.Subcategory subcategory) {

            }

            @Override
            public void onDataFetchedResource(List<DataResource> dataResources) {

            }

            @Override
            public void onError(Exception error) {

            }

        });


        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        favoritesRef = FirebaseDatabase.getInstance().getReference("Favorites").child(userId);

        if (currentSubcategoryId == -1) {
            Log.e("ResourceContentActivity", "Invalid subcategory ID.");
            return;
        }

        DataResource.Subcategory currentSubcategory = DataResource.getSubcategoryById(currentSubcategoryId, currentCategory);
        if (currentSubcategory != null) {
            populateSubcategoryContent(currentSubcategory);
        } else {
            Log.e("ResourceContentActivity", "Subcategory not found.");
        }

        setupToolbar(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(subcategory);
        }

        setupBottomNavigation();
        setButtonListeners(currentSubcategoryId, currentSubcategory);
        setupUpNextSection(currentCategory, currentSubcategory);
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
        textToSpeechButton = findViewById(R.id.tts_button);
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
}
