package com.example.ecosynergy;
import android.os.Bundle;
public class DashboardCertificateActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setupBottomNavigation(); // Setup the bottom navigation bar
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_dashboard; // This is the ID of the "Dashboard" menu item
    }

}
