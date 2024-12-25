package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the selected item to Home by default
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Handle navigation item clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    // Stay on MainActivity for Home
                    return true;

                case R.id.nav_learning:
                    startActivity(new Intent(this, LearningActivity.class));
                    return true;

                case R.id.nav_collaboration:
                    startActivity(new Intent(this, CollaborationActivity.class));
                    return true;

                case R.id.nav_dashboard:
                    startActivity(new Intent(this, DashboardActivity.class));
                    return true;

                default:
                    return false;
            }
        });
    }
}


