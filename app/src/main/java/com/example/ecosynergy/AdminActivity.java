package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecosynergy.models.ActivityItem;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page); // Layout for the main page

        // Set up click listeners for each icon
        findViewById(R.id.button_dashboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stay on the main page (this activity)
            }
        });

        findViewById(R.id.button_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Content Management Page
                Intent intent = new Intent(AdminActivity.this , ContentManagementActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button_analytics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Content Management Page
                Intent intent = new Intent(AdminActivity.this , AnalyticDashboardActivity.class);
                startActivity(intent);
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recent_activity_list);

        List<ActivityItem> activities = new ArrayList<>();
        activities.add(new ActivityItem("User XYZ", "Created new module", "3h ago"));
        activities.add(new ActivityItem("User ABC", "Updated content", "2h ago"));
        activities.add(new ActivityItem("User DEF", "Deleted old module", "1h ago"));

        ActivityListAdapter adapter = new ActivityListAdapter(activities);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();

        setupToolbar(true);
        getSupportActionBar();
        setupBottomNavigation();
    }

}
