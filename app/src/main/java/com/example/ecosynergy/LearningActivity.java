package com.example.ecosynergy;

import android.os.Bundle;

public class LearningActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
        setupBottomNavigation(); // Setup the bottom navigation bar
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_learning; // This is the ID of the "Learning" menu item
    }
}
