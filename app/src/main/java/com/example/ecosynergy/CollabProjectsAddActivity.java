package com.example.ecosynergy;

import android.os.Bundle;

public class CollabProjectsAddActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_project2);

        // Set up the toolbar with back button enabled
        setupToolbar(true);
        getSupportActionBar().setTitle("Work On Projects");

        // Set up bottom navigation
        setupBottomNavigation();
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_collaboration; // This is the ID of the "Collaboration" menu item
    }
}
