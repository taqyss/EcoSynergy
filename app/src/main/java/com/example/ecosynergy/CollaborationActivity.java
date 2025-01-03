package com.example.ecosynergy;

import android.os.Bundle;

public class CollaborationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaboration);

        // Set up the toolbar with back button enabled
        setupToolbar(false);

        //Set a custom title, or leave it blank if there's no need for a title
        getSupportActionBar().setTitle("Collaboration");

        // Set up bottom navigation
        setupBottomNavigation();
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_collaboration; // This is the ID of the "Collaboration" menu item
    }

}
