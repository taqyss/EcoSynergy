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

public class SubCategoryResourceActivity extends BaseActivity implements NavigationUtils.OnCategorySelectedListener {

    private List<DataResource.Subcategory> currentSubcategories = new ArrayList<>();
    private SubCategoryResourceAdapter subCategoryResourceAdapter;
    private String currentCategory;
    private String currentLevel;
    private FirebaseResourceFetcher firebaseResourceFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_subcategory);

        firebaseResourceFetcher = new FirebaseResourceFetcher();

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

            Log.d("SubCategoryResourceActivity", "Loading subcategories for category: " + categoryName);
            loadSubcategoriesFromFirebaseDataFetcher(categoryName, hierarchy);

            ListView listView = findViewById(R.id.subcategory_list);

            DatabaseReference subcategoryRef = FirebaseDatabase.getInstance()
                    .getReference("dataResources")
                    .child(currentLevel)
                    .child(currentCategory)
                    .child("subcategories");

            subcategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Create adapter after retrieving the data
                    subCategoryResourceAdapter = new SubCategoryResourceAdapter(
                            currentCategory,
                            currentLevel,
                            currentSubcategories,
                            dataSnapshot,
                            new SubCategoryResourceAdapter.OnSubcategoryClickListener() {
                                @Override
                                public void onSubcategoryClick(DataResource.Subcategory subcategory) {
                                    Intent detailIntent = new Intent(SubCategoryResourceActivity.this, ModulesContentActivity.class);
                                    detailIntent.putExtra("Category", currentCategory);
                                    detailIntent.putExtra("subcategory", subcategory.getArticleTitle());
                                    detailIntent.putExtra("HIERARCHY", currentLevel);
                                    startActivity(detailIntent);
                                }
                            });

                    listView.setAdapter(subCategoryResourceAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("SubCategoryResourceActivity", "Error fetching subcategories: " + databaseError.getMessage());
                    Toast.makeText(SubCategoryResourceActivity.this, "Failed to load subcategories.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("SubCategoryResourceActivity", "Invalid category or hierarchy data");
        }
    }

    private void loadSubcategoriesFromFirebaseDataFetcher(String categoryName, String branch) {
        firebaseResourceFetcher.fetchDataResource(new FirebaseResourceFetcher.FirebaseCallback() {
            @Override
            public void onDataFetched(List<DataResource> dataResources) {
                boolean subcategoryFound = false;  // Flag to check if subcategory is found

                // Loop through the dataResources and check for category and branch match
                for (DataResource dataResource : dataResources) {
                    Log.d("SubCategoryResourceActivity", "Checking dataResource - Category: " + dataResource.getCategory() + ", Branch: " + dataResource.getBranch());
                    if (dataResource.getCategory().equals(categoryName) && dataResource.getBranch().equals(branch)) {
                        for (DataResource.Subcategory subcategory : dataResource.getSubcategories()) {
                            Log.d("SubCategoryResourceActivity", "Found matching subcategory for Category: " + categoryName + " and Branch: " + branch);

                            // Clear the current list and add the matching subcategory
                            currentSubcategories.clear();
                            currentSubcategories.add(subcategory);

                            // Log how many subcategories were loaded
                            Log.d("SubCategoryResourceActivity", "Loaded subcategories: " + currentSubcategories.size());

                            // Notify the adapter of the change
                            subCategoryResourceAdapter.notifyDataSetChanged();
                            subcategoryFound = true;  // Set flag to true when subcategory is found
                            break;  // Exit after finding the matching subcategory
                        }
                    }
                }

                // If no matching subcategory is found, log a warning
                if (!subcategoryFound) {
                    Log.d("SubCategoryResourceActivity", "No subcategory found for Category: " + categoryName + " and Branch: " + branch);
                }
            }

            @Override
            public void onSuccess(Object result) {
                // Handle success
            }

            @Override
            public void onError(Exception error) {
                Log.e("SubCategoryResourceActivity", "Error fetching data", error);
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
        return null; // Implement custom view for subcategory items if needed
    }
}
