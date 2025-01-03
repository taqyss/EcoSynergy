package com.example.ecosynergy;

import android.os.Bundle;

public class CollabProjectsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_projects1);

        // Set up the toolbar with back button enabled
        setupToolbar(true);

        //Set a custom title, or leave it blank if there's no need for a title
        getSupportActionBar().setTitle("Work on Projects");

        // Set up bottom navigation
        setupBottomNavigation();
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_collaboration; // This is the ID of the "Collaboration" menu item
    }
}
