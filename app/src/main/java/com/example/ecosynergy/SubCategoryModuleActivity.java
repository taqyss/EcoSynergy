package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryActivity extends BaseActivity implements NavigationUtils.OnCategorySelectedListener {

    private List<ItemData> currentSubcategories = new ArrayList<>();
    private SubCategoryAdapter subCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_subcategory);

        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("CATEGORY_NAME");
        String hierarchy = intent.getStringExtra("HIERARCHY");

        // Set up the toolbar with back button enabled
        setupToolbar(true);

        // Set toolbar title to the category name
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(categoryName);
        }

        // Set up bottom navigation
        setupBottomNavigation();

        // Initialize ListView and adapter
        ListView listView = findViewById(R.id.subcategory_list);
        subCategoryAdapter = new SubCategoryAdapter(currentSubcategories);
        listView.setAdapter(subCategoryAdapter);

        if (categoryName != null && hierarchy != null) {
            updateSubcategories(categoryName, hierarchy);
        } else {
            Log.e("SubCategoryActivity", "Invalid category or hierarchy data");
        }
    }

    private void updateSubcategories(String categoryName, String hierarchy) {
        List<ItemData> subcategories;

        // Get the subcategories based on the hierarchy
        if ("Basic".equals(hierarchy) || "Intermediate".equals(hierarchy) || "Advanced".equals(hierarchy)) {
            subcategories = DummyData.getSubCategoriesForCategoryModule(categoryName);
        } else {
            subcategories = DummyData.getSubCategoriesForCategoryResource(categoryName);
        }

        // Filter the subcategories based on the level
        List<ItemData> filteredSubcategories = filterSubcategoriesByLevel(subcategories, hierarchy);

        // Update the adapter with the new data
        currentSubcategories.clear();
        currentSubcategories.addAll(filteredSubcategories);
        subCategoryAdapter.notifyDataSetChanged();
    }

    private List<ItemData> filterSubcategoriesByLevel(List<ItemData> subcategories, String level) {
        List<ItemData> filteredList = new ArrayList<>();

        for (ItemData item : subcategories) {
            int itemLevel = item.getLevel();
            switch (level) {
                case "Basic":
                    if (itemLevel == 0) filteredList.add(item);
                    break;
                case "Intermediate":
                    if (itemLevel == 1) filteredList.add(item);
                    break;
                case "Advanced":
                    if (itemLevel == 2) filteredList.add(item);
                    break;
                case "Articles":
                    if (itemLevel == 3) filteredList.add(item);
                    break;
                case "Reports":
                    if (itemLevel == 4) filteredList.add(item);
                    break;
                case "Toolkits":
                    if (itemLevel == 5) filteredList.add(item);
                    break;
                default:
                    Log.e("SubCategoryActivity", "Invalid level specified: " + level);
                    break;
            }
        }

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

    @Override
    public void onCategorySelected(String category) {

    }
}
