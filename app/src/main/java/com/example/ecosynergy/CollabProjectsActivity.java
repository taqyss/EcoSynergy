package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CollabProjectsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_project1);

        // Set up the toolbar with back button enabled
        setupToolbar(true);

        //Set a custom title, or leave it blank if there's no need for a title
        getSupportActionBar().setTitle("Work on Projects");

        // Set up bottom navigation
        setupBottomNavigation();

        // Find the views
        ImageView collabAddIcon = findViewById(R.id.collabAddIcon);
        TextView addNewTitle = findViewById(R.id.addNewTitle);
        ImageView roundedRectCollab4 = findViewById(R.id.roundedRectCollab4);

        // Set click listeners to navigate to "Add Projects" activity
        View.OnClickListener navigateToAddProjects = view -> {
            Intent intent = new Intent(CollabProjectsActivity.this, CollabProjectsAddActivity.class);
            startActivity(intent);
        };

        // Attach the listener to both views
        collabAddIcon.setOnClickListener(navigateToAddProjects);
        addNewTitle.setOnClickListener(navigateToAddProjects);
        roundedRectCollab4.setOnClickListener(navigateToAddProjects);

        ImageView roundedRectCollab8 = findViewById(R.id.roundedRectCollab8);
        ImageView roundedRectCollab5 = findViewById(R.id.roundedRectCollab5);
        TextView projectsTitle1 = findViewById(R.id.projectsTitle1);

        View.OnClickListener navigateToDescProjects = view -> {
            Intent intent = new Intent(CollabProjectsActivity.this, CollabProjectsDescActivity.class);
            startActivity(intent);
        };

        roundedRectCollab8.setOnClickListener(navigateToDescProjects);
        roundedRectCollab5.setOnClickListener(navigateToDescProjects);
        projectsTitle1.setOnClickListener(navigateToDescProjects);
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_collaboration; // This is the ID of the "Collaboration" menu item
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
