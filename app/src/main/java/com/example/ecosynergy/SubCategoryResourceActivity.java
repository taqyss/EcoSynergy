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
import com.google.firebase.database.Query;
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
        Log.d("SubCategoryResourceActivity", "Received category name:" + categoryName);
        String hierarchy = intent.getStringExtra("HIERARCHY");

        if (subCategoryResourceAdapter == null) {
            Log.d("SubCategoryResourceActivity", "Initializing SubCategoryResourceAdapter");
            subCategoryResourceAdapter = new SubCategoryResourceAdapter(
                    categoryName,
                    hierarchy,
                    currentSubcategories,
                    null,
                    subcategory -> {
                        Intent detailIntent = new Intent(SubCategoryResourceActivity.this, ModulesContentActivity.class);
                        detailIntent.putExtra("Category", categoryName);
                        detailIntent.putExtra("subcategory", subcategory.getArticleTitle());
                        detailIntent.putExtra("HIERARCHY", hierarchy);
                        startActivity(detailIntent);
                    }
            );

            ListView listView = findViewById(R.id.subcategory_list);
            listView.setAdapter(subCategoryResourceAdapter);
        } else {
            Log.d("SubCategoryResourceActivity", "Updating SubCategoryResourceAdapter");
            subCategoryResourceAdapter.notifyDataSetChanged();
        }

        if (categoryName != null && hierarchy != null) {
            currentCategory = categoryName;
            currentLevel = hierarchy;

            setupToolbar(true);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(categoryName);
            }

            setupBottomNavigation();

            Log.d("SubCategoryResourceActivity", "Loading subcategories for category: " + categoryName);
            loadSubcategories(categoryName, hierarchy);
        } else {
            Log.e("SubCategoryResourceActivity", "Invalid category or hierarchy data");
        }
    }

    private void loadSubcategories(String categoryName, String branch) {
        loadSubcategoriesFromFirebaseDataFetcher(categoryName, branch);
    }
    private void loadSubcategoriesFromFirebaseDataFetcher(String categoryName, String branch) {
        firebaseResourceFetcher.fetchDataResource(new FirebaseResourceFetcher.SubcategoryCallback() {
            @Override
            public void onDataFetched(List<DataResource> dataResources) {
                currentSubcategories.clear();
                Log.d("SubCategoryResourceActivity", "Data fetched successfully");
                boolean subcategoryFound = false;  // Flag to check if subcategory is found

                // Loop through the dataResources and check for category and branch match
                for (DataResource dataResource : dataResources) {
                    Log.d("SubCategoryResourceActivity", "Checking dataResource - Category: " + dataResource.getCategory() + ", Branch: " + dataResource.getBranch());
                    if (dataResource.getCategory().equals(categoryName) && dataResource.getBranch().equals(branch)) {
                        for (DataResource.Subcategory subcategory : dataResource.getSubcategories()) {
                            Log.d("SubCategoryResourceActivity", "Found matching subcategory for Category: " + categoryName + " and Branch: " + branch);

                            currentSubcategories.add(subcategory);

                            Log.d("SubCategoryResourceActivity", "Loaded subcategories: " + currentSubcategories.size());
                            subcategoryFound = true;  // Set flag to true when subcategory is found
                              // Exit after finding the matching subcategory
                        }
                    }
                }
                subCategoryResourceAdapter.notifyDataSetChanged();
                // If no matching subcategory is found, log a warning
                if (!subcategoryFound) {
                    Log.d("SubCategoryResourceActivity", "No subcategory found for Category: " + categoryName + " and Branch: " + branch);
                }
            }

            @Override
            public void onSubcategoryFetched(DataResource.Subcategory subcategory) {

            }

            @Override
            public void onDataFetchedResource(List<DataResource> dataResources) {

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

    private void updateSubcategoryListView() {
        ListView listView = findViewById(R.id.subcategory_list);
        if (subCategoryResourceAdapter != null) {
            subCategoryResourceAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCategorySelected(String category) {
        Log.d("SubCategoryResourceActivity", "Updating the new category: " + category);
        if (!category.equals(currentCategory)) {
            currentCategory = category;
            loadSubcategories(currentCategory, currentLevel);
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
