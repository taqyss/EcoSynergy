package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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
            Intent intent = new Intent(CollabProjectsAddActivity.this, CollabSearchUserActivity.class);
            startActivity(intent);
        };

        collabSearchUserIcon.setOnClickListener(navigateToSearchUserProjects);


        TextView totalCollaborators = findViewById(R.id.totalCollaborators);
        ImageView collabCircle1 = findViewById(R.id.collabCircle1);
        ImageView plusIcon = findViewById(R.id.plusIcon);
        ImageView minusIcon = findViewById(R.id.minusIcon);
        ImageView collabCircle2 = findViewById(R.id.collabCircle2);

        final int MAX_COUNT = 20;
        final int MIN_COUNT = 0;

        // Decrement Click Listener
        View.OnClickListener decrementClickListener = v -> {
            int currentCount = Integer.parseInt(totalCollaborators.getText().toString());
            if (currentCount > MIN_COUNT) {
                totalCollaborators.setText(String.valueOf(currentCount - 1));
            } else {
                Toast.makeText(this, "Minimum limit reached!", Toast.LENGTH_SHORT).show();
            }
        };

// Increment Click Listener
        View.OnClickListener incrementClickListener = v -> {
            int currentCount = Integer.parseInt(totalCollaborators.getText().toString());
            if (currentCount < MAX_COUNT) {
                totalCollaborators.setText(String.valueOf(currentCount + 1));
            } else {
                Toast.makeText(this, "Maximum limit reached!", Toast.LENGTH_SHORT).show();
            }
        };
        // Attach the same listener to both views
        collabCircle1.setOnClickListener(incrementClickListener);
        plusIcon.setOnClickListener(incrementClickListener);
        collabCircle2.setOnClickListener(decrementClickListener);
        minusIcon.setOnClickListener(decrementClickListener);
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_collaboration; // This is the ID of the "Collaboration" menu item
    }
}
