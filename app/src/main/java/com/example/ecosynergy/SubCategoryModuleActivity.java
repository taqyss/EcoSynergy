package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryModuleActivity extends BaseActivity implements NavigationUtils.OnCategorySelectedListener {

    private List<DataModule.Subcategory> currentSubcategories = new ArrayList<>();
    private SubCategoryModuleAdapter subCategoryModuleAdapter;
    private String currentCategory;
    private String currentLevel;
    private FirebaseDataFetcher firebaseDataFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_subcategory);

        firebaseDataFetcher = new FirebaseDataFetcher();

        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("CATEGORY_NAME");
        String hierarchy = intent.getStringExtra("HIERARCHY");

        if (categoryName != null && hierarchy != null) {
            currentCategory = categoryName;
            currentLevel = hierarchy;

            setupToolbar(true);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(categoryName);
            }

            setupBottomNavigation();

            Log.d("SubCategoryActivity", "Loading subcategories for category: " + categoryName);
            loadSubcategoriesFromFirebaseDataFetcher(categoryName, hierarchy);

            ListView listView = findViewById(R.id.subcategory_list);

            DatabaseReference questionSetRef = FirebaseDatabase.getInstance()
                    .getReference("dataModules")
                    .child(currentLevel)
                    .child(currentCategory)
                    .child("questionSets");

            questionSetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean hasQuestionSets = dataSnapshot.exists();

                    subCategoryModuleAdapter = new SubCategoryModuleAdapter(currentCategory, currentLevel, currentSubcategories, dataSnapshot, hasQuestionSets, (subcategory, isQuiz) -> {
                        Log.d("SubCategoryActivity", "Clicked Subcategory: " + subcategory.getTitle());

                        if (isQuiz) {
                            // Launch QuizActivity if question sets exist
                            QuizActivity.openQuizActivity(SubCategoryModuleActivity.this, currentCategory, currentLevel, dataSnapshot);
                        } else {
                            // Navigate to ModulesContentActivity
                            Intent detailIntent = new Intent(SubCategoryModuleActivity.this, ModulesContentActivity.class);
                            detailIntent.putExtra("Category", currentCategory);
                            detailIntent.putExtra("subcategory", subcategory.getTitle());
                            detailIntent.putExtra("HIERARCHY", currentLevel);
                            startActivity(detailIntent);
                        }
                    });

                    listView.setAdapter(subCategoryModuleAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("SubCategoryActivity", "Error checking question sets: " + databaseError.getMessage());
                    Toast.makeText(SubCategoryModuleActivity.this, "Failed to check for question sets.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("SubCategoryActivity", "Invalid category or hierarchy data");
        }
    }

    private void loadSubcategoriesFromFirebaseDataFetcher(String categoryName, String level) {
        firebaseDataFetcher.fetchDataModules(new FirebaseDataFetcher.FirebaseCallback() {
            @Override
            public void onDataFetchedModules(List<DataModule> dataModules) {
                currentSubcategories.clear();
                boolean subcategoryFound = false; // Flag to check if subcategory is found

                // Loop through the dataModules and filter by both level and category
                for (DataModule dataModule : dataModules) {
                    Log.d("SubCategoryModuleActivity", "Checking dataModule - Level: " + dataModule.getLevel() + ", Category: " + dataModule.getCategory());

                    // Filter by both level and category
                    if (dataModule.getLevel().equalsIgnoreCase(level) && dataModule.getCategory().equalsIgnoreCase(categoryName)) {
                        Log.d("SubCategoryModuleActivity", "Found matching dataModule");
                        currentSubcategories.addAll(dataModule.getSubcategories());
                        subcategoryFound = true; // Set flag to true when data is found
                    }
                }

                // If no matching subcategories are found, log a warning
                if (!subcategoryFound) {
                    Log.d("SubCategoryModuleActivity", "No subcategories found for Level: " + level + ", Category: " + categoryName);
                }

                // Notify the adapter about the data change
                if (subCategoryModuleAdapter != null) {
                    subCategoryModuleAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("SubCategoryModuleActivity", "Error fetching data: " + errorMessage);
                Toast.makeText(SubCategoryModuleActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCategorySelected(String category) {
        if (!category.equals(currentCategory)) {
            currentCategory = category;
            loadSubcategoriesFromFirebaseDataFetcher(currentCategory, currentLevel);
        }
    }

    @Override
    public int getCount() {
        return currentSubcategories.size();
    }

    @Override
    public Object getItem(int position) {
        return currentSubcategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
