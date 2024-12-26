package com.example.ecosynergy;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBottomNavigation(); // Setup the bottom navigation bar
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_home; // This is the ID of the "Home" menu item
    }
}
