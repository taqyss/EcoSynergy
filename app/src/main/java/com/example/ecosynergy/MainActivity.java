package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private LinearLayout continueSection;
    private TextView continueModuleTitle, continueModuleSubtitle, continueModuleTime;
    private ImageView continueModuleIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBottomNavigation(); // Setup the bottom navigation bar

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        TextView homeMessage = findViewById(R.id.home_message);
        TextView streakText = findViewById(R.id.streakText);

        // News Section Navigation
        LinearLayout newsSection = findViewById(R.id.newsSection);
        newsSection.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewsActivity.class);
            startActivity(intent);
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Fetch data from Firebase and update the UI
            fetchUserData(userId, homeMessage, streakText);

            // Load the latest module for the continue section
            loadLatestModule(userId);
        }

        // Initialize continueSection UI
        continueSection = findViewById(R.id.continueSection);
        continueModuleTitle = findViewById(R.id.continueModuleTitle);
        continueModuleSubtitle = findViewById(R.id.continueModuleSubtitle);
        continueModuleTime = findViewById(R.id.continueModuleTime);
        continueModuleIcon = findViewById(R.id.continueModuleIcon);

        // Handle continueSection click
        continueSection.setOnClickListener(v -> {
            String referenceId = (String) continueSection.getTag();
            if (referenceId != null) {
                navigateToModule(referenceId, continueModuleTitle.getText().toString(), continueModuleSubtitle.getText().toString());
            } else {
                Toast.makeText(MainActivity.this, "No recent module found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadLatestModule(String userId) {
        DatabaseReference recentActivitiesRef = databaseReference.child(userId).child("recent_activities");

        recentActivitiesRef.orderByChild("timestamp").limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                                String activityType = activitySnapshot.child("activityType").getValue(String.class);
                                if (activityType != null && activityType.startsWith("module")) {
                                    String moduleTitle = activitySnapshot.child("title").getValue(String.class);
                                    String referenceId = activitySnapshot.child("referenceId").getValue(String.class);
                                    long timestamp = activitySnapshot.child("timestamp").getValue(Long.class);

                                    // Update continueSection UI
                                    continueModuleTitle.setText(activityType.replace("module - ", "").trim());
                                    continueModuleSubtitle.setText(moduleTitle);
                                    continueModuleTime.setText(getTimeAgo(timestamp));
                                    continueModuleIcon.setImageResource(R.drawable.learning_icon);
                                    continueSection.setTag(referenceId); // Store the module ID for navigation
                                    continueSection.setVisibility(View.VISIBLE);
                                    return;
                                }
                            }
                        }
                        // Hide the section if no recent module is found
                        continueSection.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("MainActivity", "Error loading latest module: " + error.getMessage());
                        continueSection.setVisibility(View.GONE);
                    }
                });
    }

    private void navigateToModule(String referenceId, String category, String title) {
        try {
            Intent intent = new Intent(this, ModulesContentActivity.class);
            intent.putExtra("subcategoryId", Integer.parseInt(referenceId)); // Pass the reference ID
            intent.putExtra("Category", category); // Module category
            intent.putExtra("subcategory", title); // Module title
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Error navigating to module: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "Error navigating to module: ", e);
        }
    }

    private String getTimeAgo(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        if (diff < 60 * 60 * 1000) {
            return (diff / (60 * 1000)) + " minutes ago";
        } else if (diff < 24 * 60 * 60 * 1000) {
            return (diff / (60 * 60 * 1000)) + " hours ago";
        } else {
            return (diff / (24 * 60 * 60 * 1000)) + " days ago";
        }
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

    private void fetchUserData(String userId, TextView homeMessage, TextView streakText) {
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("username").getValue(String.class);
                    homeMessage.setText("Welcome back, " + username + "!");

                    // Handle streak logic
                    String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    if (snapshot.child("lastLogin").exists()) {
                        String lastLogin = snapshot.child("lastLogin").getValue(String.class);

                        if (!today.equals(lastLogin)) {
                            updateStreak(userId, snapshot, today, streakText);
                        } else {
                            int streak = snapshot.child("streak").getValue(Integer.class);
                            streakText.setText("You Have a " + streak + " Day Streak!");
                            updateLeafIcons(streak);
                        }
                    } else {
                        initializeStreak(userId, today, streakText);
                    }
                } else {
                    homeMessage.setText("Welcome back!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Error: " + error.getMessage());
            }
        });
    }

    private void updateStreak(String userId, DataSnapshot snapshot, String today, TextView streakText) {
        int streak = 1; // Default streak value
        try {
            String lastLogin = snapshot.child("lastLogin").getValue(String.class);
            Date lastLoginDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(lastLogin);
            Date todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(today);

            Calendar calLast = Calendar.getInstance();
            Calendar calToday = Calendar.getInstance();

            calLast.setTime(lastLoginDate);
            calToday.setTime(todayDate);

            calLast.add(Calendar.DAY_OF_YEAR, 1); // Check for consecutive login
            if (calLast.get(Calendar.DAY_OF_YEAR) == calToday.get(Calendar.DAY_OF_YEAR) &&
                    calLast.get(Calendar.YEAR) == calToday.get(Calendar.YEAR)) {
                streak = snapshot.child("streak").exists() ? snapshot.child("streak").getValue(Integer.class) + 1 : 1;
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error parsing dates: " + e.getMessage());
        }

        // Update Firebase and UI
        databaseReference.child(userId).child("lastLogin").setValue(today);
        databaseReference.child(userId).child("streak").setValue(streak);
        streakText.setText("You Have a " + streak + " Day Streak!");
        updateLeafIcons(streak);
    }

    private void initializeStreak(String userId, String today, TextView streakText) {
        databaseReference.child(userId).child("lastLogin").setValue(today);
        databaseReference.child(userId).child("streak").setValue(1);
        streakText.setText("You Have a 1 Day Streak!");
        updateLeafIcons(1);
    }

    private void updateLeafIcons(int streak) {
        ImageView[] leaves = new ImageView[]{
                findViewById(R.id.leaf1),
                findViewById(R.id.leaf2),
                findViewById(R.id.leaf3),
                findViewById(R.id.leaf4),
                findViewById(R.id.leaf5),
                findViewById(R.id.leaf6),
                findViewById(R.id.leaf7)
        };

        for (int i = 0; i < leaves.length; i++) {
            if (i < streak) {
                leaves[i].setVisibility(View.VISIBLE);
            } else {
                leaves[i].setVisibility(View.GONE);
            }
        }
    }
}
