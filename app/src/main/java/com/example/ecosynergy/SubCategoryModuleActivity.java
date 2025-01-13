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

    private void loadSubcategoriesFromFirebaseDataFetcher(String categoryName, String level) {
        firebaseDataFetcher.fetchDataModules(new FirebaseDataFetcher.FirebaseCallback() {
            @Override
            public void onDataFetchedModules(List<DataModule> dataModules) {
                currentSubcategories.clear();
                boolean subcategoryFound = false;

                for (DataModule dataModule : dataModules) {
                    if (dataModule.getLevel().equalsIgnoreCase(level) && dataModule.getCategory().equalsIgnoreCase(categoryName)) {
                        currentSubcategories.addAll(dataModule.getSubcategories());
                        subcategoryFound = true;

                        if (!dataModule.getLevel().equalsIgnoreCase(level) || !dataModule.getCategory().equalsIgnoreCase(categoryName)) {
                            break;
                        }
                    }
                }

                if (!subcategoryFound) {
                    Log.w("SubCategoryModuleActivity", "No subcategories found for Level: " + level + ", Category: " + categoryName);
                }

                if (subCategoryModuleAdapter != null) {
                    subCategoryModuleAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("SubCategoryModuleActivity", "Error fetching data: " + errorMessage);
                Toast.makeText(SubCategoryModuleActivity.this, "Failed to fetch subcategories. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onCategorySelected(String category) {

    }
}
