package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SubCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("CATEGORY_NAME");
        String hierarchy = intent.getStringExtra("HIERARCHY");

        if ("Basic".equals(hierarchy) || "Intermediate".equals(hierarchy) || "Advanced".equals(hierarchy)) {
            setContentView(R.layout.subcategory_content_modules);
        } else if ("Article".equals(hierarchy) || "Reports".equals(hierarchy) || "Toolkits".equals(hierarchy)) {
            setContentView(R.layout.subcategory_content_articles);
        } else {
            // Default layout
            setContentView(R.layout.fragment_todays_pick);
        }

        if (categoryName != null && hierarchy != null) {
            // Handle category and hierarchy logic
            handleCategory(categoryName, hierarchy);
        } else {
            // Handle invalid or missing data
            System.out.println("Invalid or missing data in Intent extras");
        }
    }

    private void handleCategory(String categoryName, String hierarchy) {
        switch (hierarchy) {
            case "Basic":
                // Logic for Basic
                break;
            case "Intermediate":
                // Logic for Intermediate
                break;
            case "Advanced":
                // Logic for Advanced
                break;
            case "Article":
                // Logic for Article
                break;
            case "Reports":
                // Logic for Reports
                break;
            case "Toolkits":
                // Logic for Toolkits
                break;
            default:
                System.out.println("Invalid hierarchy");
                break;
        }
    }

}
