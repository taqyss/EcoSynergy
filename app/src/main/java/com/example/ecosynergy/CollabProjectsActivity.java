package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

        // Find the views
        ImageView collabAddIcon = findViewById(R.id.collabAddIcon);
        TextView addNewTitle = findViewById(R.id.addNewTitle);

        // Set click listeners to navigate to "Add Projects" activity
        View.OnClickListener navigateToAddProjects = view -> {
            Intent intent = new Intent(CollabProjectsActivity.this, CollabProjectsAddActivity.class);
            startActivity(intent);
        };

        // Attach the listener to both views
        collabAddIcon.setOnClickListener(navigateToAddProjects);
        addNewTitle.setOnClickListener(navigateToAddProjects);
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_collaboration; // This is the ID of the "Collaboration" menu item
    }
}
