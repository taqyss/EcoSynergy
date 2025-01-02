package com.example.ecosynergy;

import android.os.Bundle;

public class NewsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Set up the toolbar with back button
        setupToolbar(true);

        //Set a custom title
        getSupportActionBar().setTitle("News & Announcements");

        // Set up bottom navigation
        setupBottomNavigation();
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_dashboard;
    }
}
