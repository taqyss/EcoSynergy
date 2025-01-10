package com.example.ecosynergy;

import android.content.Context;
import android.content.Intent;

public class NavigationUtils {
    public static Intent createSubCategoryIntent(Context context, String selectedCategory, String hierarchy) {
        Intent intent = new Intent(context, SubCategoryActivity.class);
        intent.putExtra("CATEGORY_NAME", selectedCategory);
        intent.putExtra("HIERARCHY", hierarchy);
        return intent;
    }

    public interface OnCategorySelectedListener {
        void onCategorySelected(String category);
    }
}
