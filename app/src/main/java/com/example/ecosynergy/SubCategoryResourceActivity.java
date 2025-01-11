package com.example.ecosynergy;

import static com.example.ecosynergy.DataResource.getDataResourcesForCategory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryResourceActivity extends BaseActivity implements NavigationUtils.OnCategorySelectedListener {

    private List<DataResource.Subcategory> currentSubcategories = new ArrayList<>();

    private SubCategoryResourceAdapter subCategoryResourceAdapter;
    private String currentCategory;
    private String currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_subcategory);

        // Get the category name and hierarchy from the intent
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("CATEGORY_NAME");
        String hierarchy = intent.getStringExtra("HIERARCHY");

        // Initialize currentCategory and currentLevel
        if (categoryName != null && hierarchy != null) {
            currentCategory = categoryName;
            currentLevel = hierarchy;

            // Set up the toolbar with back button enabled
            setupToolbar(true);

            // Set toolbar title to the category name
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(categoryName);
            }

            // Set up bottom navigation
            setupBottomNavigation();

            // Log category name to ensure it's correct
            Log.d("SubCategoryResourceAdapter", "Category Resource selected: " + categoryName);

            List<DataResource> dataResource = getDataResourcesForCategory(categoryName);

            Log.d("SubCategoryResourceAdapter", "Data resource for category: " + dataResource.size());

            // Filter subcategories based on the level
            currentSubcategories = filterSubcategoriesByLevel(dataResource, hierarchy);

            // Log filtered subcategories count
            Log.d("SubCategoryResourceAdapter", "Filtered Resource subcategories: " + currentSubcategories.size());

            // Initialize ListView and adapter
            ListView listView = findViewById(R.id.subcategory_list);
            subCategoryResourceAdapter = new SubCategoryResourceAdapter(currentSubcategories);
            subCategoryResourceAdapter.notifyDataSetChanged();
            listView.setAdapter(subCategoryResourceAdapter);  // Set adapter after filtering data
        } else {
            Log.e("SubCategoryResourceAdapter", "Invalid category or hierarchy data");
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

    @Override
    public void onCategorySelected(String category) {
        // Only update if category has changed
        if (!category.equals(currentCategory)) {
            currentCategory = category;
            updateSubcategories(currentCategory, currentLevel);
        }
    }

    private void updateSubcategories(String categoryName, String level) {
        List<DataResource> dataResource = getDataResourcesForCategory(categoryName);

        // Filter the subcategories based on the selected level
        List<DataResource.Subcategory> filteredSubcategories = filterSubcategoriesByLevel(dataResource, level);

        // Only update if the data has changed
        if (!filteredSubcategories.equals(currentSubcategories)) {
            currentSubcategories.clear();
            currentSubcategories.addAll(filteredSubcategories);
            subCategoryResourceAdapter.notifyDataSetChanged();
        }

        Log.d("SubCategoryActivity", "Subcategories updated: " + currentSubcategories.size());
    }

    private List<DataResource.Subcategory> filterSubcategoriesByLevel(List<DataResource> dataResource, String level) {
        List<DataResource.Subcategory> filteredList = new ArrayList<>();

        for (DataResource data : dataResource) {
            Log.d("SubCategoryActivity", "Checking level: " + data.getBranch());
            if (level.equals(data.getBranch())) {
                filteredList.addAll(data.getSubcategories());
                Log.d("SubCategoryActivity", "Added subcategories: " + data.getSubcategories().size());
            }
        }

        Log.d("SubCategoryActivity", "Filtered subcategories count: " + filteredList.size());
        return filteredList;
    }
}
