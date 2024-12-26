package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Highlight the correct item
        int currentActivityId = getCurrentActivityId();
        if (currentActivityId != -1) {
            bottomNavigationView.setSelectedItemId(currentActivityId);
        }

        // Set navigation listener
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

    // Override this method in child activities to specify the current activity's menu item ID
    protected int getCurrentActivityId() {
        return -1;
    }
}
