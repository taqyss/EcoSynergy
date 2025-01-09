package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NewsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Setup the toolbar with back button
        setupToolbar(true);

        getSupportActionBar().setTitle("News & Announcements"); // Set the title of the activity

        // Setup bottom navigation
        setupBottomNavigation();

        // Set up click listeners for the articles
        TextView newsItem1 = findViewById(R.id.newsItem1);
        TextView newsItem2 = findViewById(R.id.newsItem2);

        newsItem1.setOnClickListener(v -> openNewsDetails(0)); // Open details for the first news
        newsItem2.setOnClickListener(v -> openNewsDetails(1)); // Open details for the second news
    }

    /**
     * Opens the NewsDetailsActivity and passes the selected news index.
     *
     * @param newsIndex The index of the selected news item.
     */
    private void openNewsDetails(int newsIndex) {
        Intent intent = new Intent(this, NewsDetailsActivity.class);
        intent.putExtra("newsIndex", newsIndex); // Pass the news index to the details activity
        startActivity(intent);
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_dashboard; // Highlight the "Dashboard" item in the bottom navigation
    }
}
