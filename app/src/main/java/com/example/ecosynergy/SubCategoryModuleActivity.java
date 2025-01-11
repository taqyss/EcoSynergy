package com.example.ecosynergy;

import static com.example.ecosynergy.DataModule.getDataModulesForCategory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryModuleActivity extends BaseActivity implements NavigationUtils.OnCategorySelectedListener {

    private List<DataModule.Subcategory> currentSubcategories = new ArrayList<>();

    private SubCategoryModuleAdapter subCategoryModuleAdapter;
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
            Log.d("SubCategoryActivity", "Category selected: " + categoryName);

            // Initialize data modules for the selected category
            List<DataModule> dataModules = getDataModulesForCategory(categoryName);

            // Log number of data modules
            Log.d("SubCategoryActivity", "Data modules for category: " + dataModules.size());

            // Filter subcategories based on the level
            currentSubcategories = filterSubcategoriesByLevel(dataModules, hierarchy);

            // Log filtered subcategories count
            Log.d("SubCategoryActivity", "Filtered subcategories: " + currentSubcategories.size());

            // Initialize ListView and adapter
            ListView listView = findViewById(R.id.subcategory_list);
            subCategoryModuleAdapter = new SubCategoryModuleAdapter(currentSubcategories);
            subCategoryModuleAdapter.notifyDataSetChanged();
            listView.setAdapter(subCategoryModuleAdapter);  // Set adapter after filtering data
        } else {
            Log.e("SubCategoryActivity", "Invalid category or hierarchy data");
        }
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
        // Get updated data modules for the selected category
        List<DataModule> dataModules = getDataModulesForCategory(categoryName);

        // Filter the subcategories based on the selected level
        List<DataModule.Subcategory> filteredSubcategories = filterSubcategoriesByLevel(dataModules, level);

        // Update the adapter with the new subcategories
        currentSubcategories.clear();
        currentSubcategories.addAll(filteredSubcategories);
        subCategoryModuleAdapter.notifyDataSetChanged();

        Log.d("SubCategoryActivity", "Subcategories updated: " + currentSubcategories.size());
    }

    private List<DataModule.Subcategory> filterSubcategoriesByLevel(List<DataModule> dataModules, String level) {
        List<DataModule.Subcategory> filteredList = new ArrayList<>();

        // Iterate over data modules and filter subcategories based on the level
        for (DataModule dataModule : dataModules) {
            Log.d("SubCategoryActivity", "Checking level: " + dataModule.getLevel());
            if (level.equals(dataModule.getLevel())) {
                filteredList.addAll(dataModule.getSubcategories());
                Log.d("SubCategoryActivity", "Added subcategories: " + dataModule.getSubcategories().size());
            }
        }

        Log.d("SubCategoryActivity", "Filtered subcategories count: " + filteredList.size());
        return filteredList;
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
