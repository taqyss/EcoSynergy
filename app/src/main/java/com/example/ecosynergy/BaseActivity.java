package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    /**
     * Sets up the Toolbar with a universal back button.
     * This method must be called in each child activity after setting the content view.
     *
     * @param enableBackButton Whether to enable the back button functionality.
     */
    protected void setupToolbar(boolean enableBackButton) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            if (enableBackButton) {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
            }
        } else {
            Log.w(TAG, "Toolbar not found in the current layout.");
        }
    }

    /**
     * Sets up the bottom navigation bar for the activity.
     * This method highlights the current activity's menu item and sets up listeners for navigation.
     */
    protected void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (bottomNavigationView == null) {
            Log.w(TAG, "Bottom navigation view not found in the current layout.");
            return;
        }

        // Highlight the correct item in the bottom navigation bar
        int currentActivityId = getCurrentActivityId();
        if (currentActivityId != -1) {
            bottomNavigationView.setSelectedItemId(currentActivityId);
        }

        // Set navigation listener for bottom navigation items
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home && currentActivityId != R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_learning && currentActivityId != R.id.nav_learning) {
                startActivity(new Intent(this, LearningActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_collaboration && currentActivityId != R.id.nav_collaboration) {
                startActivity(new Intent(this, CollaborationActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_dashboard && currentActivityId != R.id.nav_dashboard) {
                startActivity(new Intent(this, DashboardActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    /**
     * Specifies the current activity's menu item ID for bottom navigation highlighting.
     * Subclasses should override this method to return their corresponding menu item ID.
     *
     * @return The menu item ID for the current activity.
     */
    protected int getCurrentActivityId() {
        return -1; // Default implementation, override in subclasses
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.d(TAG, "Toolbar back button clicked.");
            onBackPressed(); // Handle the back button click
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract int getCount();

    public abstract Object getItem(int position);

    public abstract long getItemId(int position);

    public abstract View getView(int position, View convertView, ViewGroup parent);
}
