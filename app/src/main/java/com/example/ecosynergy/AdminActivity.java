package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecosynergy.models.ActivityItem;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends BaseActivity {

    private TextView nameTextView;
    private TextView roleTextView;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page); // Layout for the main page

        // Initialize UI components
        nameTextView = findViewById(R.id.user_name);
        roleTextView = findViewById(R.id.user_role);
        profileImageView = findViewById(R.id.profile_image);

        loadUserData();

        // Set up click listeners for each icon
        findViewById(R.id.button_dashboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this , DashboardActivity.class);
                startActivity(intent);
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

        // Fetch recent activity data from firebase
        loadRecentActivities(adapter);

        setupToolbar(true);
        getSupportActionBar();
        setupBottomNavigation();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    private void loadUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("username").getValue(String.class);
                        String role = dataSnapshot.child("role").getValue(String.class);

                        // Set username
                        nameTextView.setText(username != null ? username: "Admin");

                        // Set email
                        roleTextView.setText(role != null ? role: "Admin");

                        profileImageView.setImageResource(R.drawable.usericon);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseError", "Error fetching user data",  error.toException());
                }
            });

        }
    }

    private void loadRecentActivities(ActivityListAdapter adapter) {
        DatabaseReference activityRef = FirebaseDatabase.getInstance().getReference("activities");

        activityRef.orderByChild("timestamp").limitToLast(10) // Fetch the last 10 activities
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<ActivityItem> activities = new ArrayList<>();
                    for (DataSnapshot activitySnapshot : dataSnapshot.getChildren()) {
                        ActivityItem activity = activitySnapshot.getValue(ActivityItem.class);
                        if (activity != null) {
                            activities.add(activity);
                        }
                        // Update the adapter with the new list of activities
                        adapter.updateActivities(activities);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseError", "Error fetching activities", error.toException());

                }
            });
    }

}
