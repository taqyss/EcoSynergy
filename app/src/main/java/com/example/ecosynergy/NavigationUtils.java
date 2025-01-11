package com.example.ecosynergy;

import android.content.Context;
import android.content.Intent;

public class NavigationUtils {
    public static Intent createSubCategoryIntentModule(Context context, String selectedCategory, String hierarchy) {
        Intent intent = new Intent(context, SubCategoryModuleActivity.class);
        intent.putExtra("CATEGORY_NAME", selectedCategory);
        intent.putExtra("HIERARCHY", hierarchy);
        return intent;
    }

    public static Intent createSubCategoryIntentResource(Context context, String selectedCategory, String hierarchy) {
        Intent intent = new Intent(context, SubCategoryResourceActivity.class);
        intent.putExtra("CATEGORY_NAME", selectedCategory);
        intent.putExtra("HIERARCHY", hierarchy);
        return intent;
    }

    public interface OnCategorySelectedListener {
        void onCategorySelected(String category);
    }
}
