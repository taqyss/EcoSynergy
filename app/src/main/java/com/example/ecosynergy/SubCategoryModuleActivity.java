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

            List<DataModule> dataModules = getDataModulesForCategory(categoryName);
            currentSubcategories = filterSubcategoriesByLevel(dataModules, hierarchy);

            // Initialize the adapter with click listener
            ListView listView = findViewById(R.id.subcategory_list);
            subCategoryModuleAdapter = new SubCategoryModuleAdapter(currentCategory, currentSubcategories, subcategory -> {
                // Handle subcategory click
                Log.d("SubCategoryActivity", "Clicked Subcategory: " + subcategory.getTitle());

                // Example: Navigate to another activity
                Intent detailIntent = new Intent(SubCategoryModuleActivity.this, ModulesContentActivity.class);
                detailIntent.putExtra("Category", categoryName);
                detailIntent.putExtra("subcategory", subcategory.getTitle());  // Add this line for category
                detailIntent.putExtra("subcategoryId", subcategory.getId());  // Add this line for subcategory ID
                startActivity(detailIntent);
            });

            listView.setAdapter(subCategoryModuleAdapter);
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
        Log.d("SubCategoryActivity", "Updating subcategories for category: " + categoryName);
        List<DataModule> dataModule = getDataModulesForCategory(categoryName);

        // Filter the subcategories based on the selected level
        List<DataModule.Subcategory> filteredSubcategories = filterSubcategoriesByLevel(dataModule, level);

        // Only update if the data has changed
        if (!filteredSubcategories.equals(currentSubcategories)) {
            currentSubcategories.clear();
            currentSubcategories.addAll(filteredSubcategories);
            subCategoryModuleAdapter.notifyDataSetChanged();
        }

        Log.d("SubCategoryActivity", "Subcategories updated: " + currentSubcategories.size());
    }

    private List<DataModule.Subcategory> filterSubcategoriesByLevel(List<DataModule> dataModule, String level) {
        List<DataModule.Subcategory> filteredList = new ArrayList<>();

        for (DataModule data : dataModule) {
            Log.d("SubCategoryActivity", "Checking level: " + data.getLevel());
            if (level.equals(data.getLevel())) {
                filteredList.addAll(data.getSubcategories());
                Log.d("SubCategoryActivity", "Added subcategories: " + data.getSubcategories().size());
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