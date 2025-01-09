package com.example.ecosynergy;
import android.os.Bundle;
public class DashboardCertificateActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_certificate);

        // Set up the toolbar with back button enabled
        setupToolbar(true);

        //Set a custom title, or leave it blank if there's no need for a title
        getSupportActionBar().setTitle("Badge & Certificate");

        // Set up bottom navigation
        setupBottomNavigation();
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_dashboard; // This is the ID of the "Dashboard" menu item
    }

}
