package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class DashboardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Set up the toolbar with back button enabled
        setupToolbar(false);

        // Set a custom title, or leave it blank if there's no need for a title
        getSupportActionBar().setTitle("Dashboard");

        // Set up bottom navigation
        setupBottomNavigation();

        // Set up BTNCertificate click listener
        ImageButton btnCertificate = findViewById(R.id.BTNCertificate);
        btnCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to DashboardCertificateActivity
                Intent intent = new Intent(DashboardActivity.this, DashboardCertificateActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_dashboard; // This is the ID of the "Dashboard" menu item
    }
}
