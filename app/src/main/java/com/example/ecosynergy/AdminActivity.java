package com.example.ecosynergy;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link the layout file to the activity
        setContentView(R.layout.admin_page);  // Replace with the appropriate layout
        setContentView(R.layout.content_management);
        setContentView(R.layout.analytic_dashboard);

        setupToolbar(true);
        getSupportActionBar();
        setupBottomNavigation();
    }
}
