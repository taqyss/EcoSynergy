package com.example.ecosynergy;

import android.content.Intent;
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

public class ResourceContentActivity extends BaseActivity {

    // Declare Views
    private ImageButton favoriteButton, shareButton, textToSpeechButton;
    private TextView detailTitle, detailDescription, discussionTextView;
    private RecyclerView upNextRecyclerView;

    // Declare data structure for "Up Next" subcategories
    private List<DataResource.Subcategory> upNextList;
    private ResourcesUpNextAdapter upNextAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcategory_content_articles);

        // Initialize Views
        initializeViews();

        // Get Intent data
        int currentSubcategoryId = getIntent().getIntExtra("subcategoryId", -1);
        String currentCategory = getIntent().getStringExtra("Category");
        String subcategory = getIntent().getStringExtra("subcategory");

        Log.d("ResourceContentActivity", "currentSubcategoryId: " + currentSubcategoryId);
        Log.d("ResourceContentActivity", "currentCategory: " + currentCategory);
        Log.d("ResourceContentActivity", "subcategory: " + subcategory);

        // Validate Subcategory ID
        if (currentSubcategoryId == -1) {
            Log.e("ResourceContentActivity", "Invalid subcategory ID.");
            return;
        }

        // Fetch Subcategory data
        DataResource.Subcategory currentSubcategory = DataResource.getSubcategoryById(currentSubcategoryId, currentCategory);
        Log.d("ResourceContentActivity", "Current Subcategory: " + currentSubcategory);

        if (currentSubcategory != null) {
            // Populate Subcategory Content
            populateSubcategoryContent(currentSubcategory);
        } else {
            Log.e("ResourceContentActivity", "Subcategory not found.");
        }

        // Set up Toolbar
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
        favoriteButton = findViewById(R.id.favorite_button);
        shareButton = findViewById(R.id.share_button);
        textToSpeechButton = findViewById(R.id.tts_button);
    }

    // Populate Subcategory Content
    private void populateSubcategoryContent(DataResource.Subcategory currentSubcategory) {
        detailTitle.setText(currentSubcategory.getArticleTitle());
        detailDescription.setText(currentSubcategory.getArticleContent());
    }

    // Set Button Click Listeners
    private void setButtonListeners() {
        favoriteButton.setOnClickListener(view -> {
            Toast.makeText(this, "Favorite button clicked!", Toast.LENGTH_SHORT).show();
        });

        shareButton.setOnClickListener(view -> {
            Toast.makeText(this, "Share button clicked!", Toast.LENGTH_SHORT).show();
        });

        textToSpeechButton.setOnClickListener(view -> {
            Toast.makeText(this, "Text-to-Speech button clicked!", Toast.LENGTH_SHORT).show();
        });
    }

    // Setup "Up Next" section
    private void setupUpNextSection(String currentCategory, DataResource.Subcategory currentSubcategory) {
        upNextList = new ArrayList<>();
        List<DataResource.Subcategory> allSubcategories = DataResource.getSubcategoriesForCategory(currentCategory);

        // Add subcategories following the current one
        boolean foundCurrent = false;
        for (DataResource.Subcategory subcategory : allSubcategories) {
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
        upNextAdapter = new ResourcesUpNextAdapter(upNextList);
        upNextAdapter.setOnSubcategoryClickListener(subcategory -> {
            TextView discussionTextView = findViewById(R.id.DiscussionUpNext);
            if (discussionTextView != null) {
                discussionTextView.setOnClickListener(v -> {
                    DiscussionActivity.openDiscussionActivity(ResourceContentActivity.this, currentCategory, subcategory.getArticleTitle());
                });
            }

            // Handle subcategory click, navigate to another activity
            Intent intent = new Intent(ResourceContentActivity.this, ResourceContentActivity.class);
            intent.putExtra("subcategoryId", subcategory.getId());
            intent.putExtra("subcategory", subcategory.getArticleTitle());
            intent.putExtra("Category", currentCategory); // Pass the category again
            startActivity(intent);
        });

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
