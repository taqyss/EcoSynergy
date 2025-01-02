package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the dynamic welcome message
        TextView homeMessage = findViewById(R.id.home_message);
        homeMessage.setText("Welcome back, John"); // Placeholder for user login integration

        // Setup buttons
        LinearLayout newsSection = findViewById(R.id.newsSection);
        newsSection.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewsActivity.class);
            startActivity(intent);
        });


        LinearLayout continueSection = findViewById(R.id.continueSection);

        // Navigate to News & Announcements
        newsSection.setOnClickListener(v -> {
            // Placeholder for NewsActivity (to be created later)
            Intent intent = new Intent(MainActivity.this, NewsActivity.class);
            startActivity(intent);
        });

        // Navigate to Learning Activity
        continueSection.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LearningActivity.class);
            startActivity(intent);
        });

        // Setup bottom navigation
        setupBottomNavigation();
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_home; // ID of the "Home" menu item
    }
}
