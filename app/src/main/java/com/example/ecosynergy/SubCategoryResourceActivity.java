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
                Log.d("SubCategoryResourceActivity", "Data fetched successfully");

                // Clear the current list
                currentSubcategories.clear();

                boolean subcategoryFound = false;

                // Iterate through dataResources
                for (DataResource dataResource : dataResources) {
                    Log.d("SubCategoryResourceActivity", "Checking dataResource - Category: " + dataResource.getCategory() + ", Branch: " + dataResource.getBranch());

                    if (dataResource.getBranch().equals(branch) && dataResource.getCategory().equals(categoryName)) {
                        Log.d("SubCategoryResourceActivity", "Match found for Category: " + categoryName + ", Branch: " + branch);

                        // Extract and add subcategories from the matched resource
                        List<DataResource.Subcategory> subcategories = dataResource.getSubcategories();
                        if (subcategories != null) {
                            currentSubcategories.addAll(subcategories);
                            subcategoryFound = true;
                        }
                        // Stop the loop after finding a match
                        break;
                    }
                }
                Log.d("SubCategoryResourceActivity", "current subcategories size: " + currentSubcategories.size());
                // Update the adapter once
                subCategoryResourceAdapter.notifyDataSetChanged();

                // Log warnings if no matching subcategories are found
                if (!subcategoryFound) {
                    Log.w("SubCategoryResourceActivity", "No subcategories found for Category: " + categoryName + " and Branch: " + branch);
                } else {
                    Log.d("SubCategoryResourceActivity", "Total subcategories loaded: " + currentSubcategories.size());
                }
            }

            @Override
            public void onSubcategoryFetched(DataResource.Subcategory subcategory) {
                // Optional callback if needed
            }

            @Override
            public void onDataFetchedResource(List<DataResource> dataResources) {
                // Optional callback if needed
            }

            @Override
            public void onSuccess(Object result) {
                Log.d("SubCategoryResourceActivity", "Operation succeeded: " + result);
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
