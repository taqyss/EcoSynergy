package com.example.ecosynergy;

import android.os.Bundle;

public class CollabProjectsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_projects);
        setupBottomNavigation(); // Setup the bottom navigation bar
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_collaboration; // This is the ID of the "Collaboration" menu item
    }
}
