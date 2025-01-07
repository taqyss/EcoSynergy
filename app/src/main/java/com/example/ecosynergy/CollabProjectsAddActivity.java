package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


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

        ImageView collabSearchUserIcon = findViewById(R.id.collabSearcUserIcon);
        View.OnClickListener navigateToSearchUserProjects = view -> {
            Intent intent = new Intent(CollabProjectsAddActivity.this, CollabDiscussActivity.class);
            startActivity(intent);
        };

        collabSearchUserIcon.setOnClickListener(navigateToSearchUserProjects);

    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_collaboration; // This is the ID of the "Collaboration" menu item
    }
}
