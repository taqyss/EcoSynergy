package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;

public class CollaborationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaboration);

        // Set up the toolbar with back button enabled
        setupToolbar(false);
        getSupportActionBar().setTitle("Collaboration");

        // Set up bottom navigation
        setupBottomNavigation();

        // Add click listeners for arrow icons
        findViewById(R.id.arrowIconCollab1).setOnClickListener(v -> {
            Intent intent = new Intent(CollaborationActivity.this, CollabProjectsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.arrowIconCollab2).setOnClickListener(v -> {
            Intent intent = new Intent(CollaborationActivity.this, CollabDiscussActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.arrowIconCollab3).setOnClickListener(v -> {
            Intent intent = new Intent(CollaborationActivity.this, CollabPartnersActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_collaboration; // This is the ID of the "Collaboration" menu item
    }
}

