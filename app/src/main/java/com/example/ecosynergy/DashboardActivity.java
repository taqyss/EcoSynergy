package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*; //FIREBASE NOT DETECTED
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.*;

public class DashboardActivity extends BaseActivity {

    private FirebaseDatabase database;
    private DatabaseReference activitiesRef;
    private DatabaseReference progressRef; // FIREBASE NOT DETECTED
    private String userId;

    // UI Elements for Activity Cards
    private View[] activityCards;
    private TextView[] cardTitles;
    private TextView[] cardSubtitles;
    private TextView[] timeTexts;
    private ImageButton[] activityIcons;

    // UI Elements for Progress Cards
    private View[] progressCards;
    private TextView[] progressTitles;
    private ProgressBar[] progressBars;
    private TextView[] progressPercentages;

    private TextView noActivityMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Set up the toolbar with back button enabled
        setupToolbar(false);
        getSupportActionBar().setTitle("Dashboard");
        setupBottomNavigation();

        // Initialize Firebase
        initializeFirebase();

        // Initialize UI elements
        initializeUIElements();

        // Set up certificate button click listener
        setupCertificateButton();

        // Set up admin mode button click listener
        setupAdminButton();

        // Set up logout button click listener
        setupLogoutButton();

        // Load data from Firebase
        loadRecentActivities();
        loadModuleProgress();
    }

    private void initializeFirebase() {
        database = FirebaseDatabase.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        activitiesRef = database.getReference("Users").child(userId).child("recent_activities");
        progressRef = database.getReference("users").child(userId).child("module_progress");
    } // FIREBASE NOT DETECTED

    private void initializeUIElements() {
        // Initialize activity cards arrays
        activityCards = new View[]{
                findViewById(R.id.activityCard1),
                findViewById(R.id.activityCard2),
                findViewById(R.id.activityCard3),
                findViewById(R.id.activityCard4)
        };

        cardTitles = new TextView[]{
                findViewById(R.id.card1Title),
                findViewById(R.id.card2Title),
                findViewById(R.id.card3Title),
                findViewById(R.id.card4Title)
        };

        cardSubtitles = new TextView[]{
                findViewById(R.id.card1Subtitle),
                findViewById(R.id.card2Subtitle),
                findViewById(R.id.card3Subtitle),
                findViewById(R.id.card4Subtitle)
        };

        timeTexts = new TextView[]{
                findViewById(R.id.textView2),
                findViewById(R.id.textView3),
                findViewById(R.id.textView4),
                findViewById(R.id.textView5)
        };

        activityIcons = new ImageButton[]{
                findViewById(R.id.imageButton4),
                findViewById(R.id.imageButton6),
                findViewById(R.id.imageButton8),
                findViewById(R.id.imageButton5)
        };

        // Initialize progress cards arrays
        progressCards = new View[]{
                findViewById(R.id.progressCard1),
                findViewById(R.id.progressCard2)
        };

        progressTitles = new TextView[]{
                findViewById(R.id.TitleProgressCard1),
                findViewById(R.id.TitleProgressCard2)
        };

        progressBars = new ProgressBar[]{
                findViewById(R.id.progressBar),
                findViewById(R.id.progressBar2)
        };

        progressPercentages = new TextView[]{
                findViewById(R.id.textView),
                findViewById(R.id.textView14)
        };

        noActivityMessage = findViewById(R.id.noActivityMessage);

        // Hide all activity cards initially
        for (View card : activityCards) {
            card.setVisibility(View.GONE);
        }
        noActivityMessage.setVisibility(View.GONE);
    }

    private void setupCertificateButton() {
        ImageButton btnCertificate = findViewById(R.id.BTNCertificate);
        btnCertificate.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, DashboardCertificateActivity.class);
            startActivity(intent);
        });
    }

    private void setupLogoutButton() {
        Button btnLogout = findViewById(R.id.BTNLogout);
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void setupAdminButton() {
        ImageButton btnAdminMode = findViewById(R.id.BTNAdminMode);

        // Hide button by default
        btnAdminMode.setVisibility(View.GONE);

        // Check user role in Firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Boolean isAdmin = snapshot.child("isAdmin").getValue(Boolean.class);
                    if (isAdmin != null && isAdmin) {
                        btnAdminMode.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        btnAdminMode.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AdminActivity.class);
            startActivity(intent);
        });
    }


    /*private void saveRecentActivity(String type, String title) {
        String activityId = activitiesRef.push().getKey();
        long timestamp = System.currentTimeMillis();

        DashboardRecentActivity activity = new DashboardRecentActivity(
                activityId,
                type,
                title,
                timestamp,
                null
        );

        if (activityId != null) {
            activitiesRef.child(activityId).setValue(activity);
        }
    }*/


    @Override
    protected void onResume() {
        super.onResume();
        loadRecentActivities(); // Reload recent activities when the dashboard resumes
    }

    private void loadRecentActivities() {
        activitiesRef.orderByChild("timestamp").limitToLast(4)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                            noActivityMessage.setVisibility(View.GONE);
                            int index = 0;

                            List<DashboardRecentActivity> activities = new ArrayList<>();
                            for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                                DashboardRecentActivity activity = activitySnapshot.getValue(DashboardRecentActivity.class);
                                if (activity != null) {
                                    activities.add(activity);
                                }
                            }

                            // Sort activities by timestamp in descending order
                            activities.sort((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));

                            // Display the latest 4 activities
                            for (DashboardRecentActivity activity : activities) {
                                if (index < activityCards.length) {
                                    updateActivityCard(index, activity);
                                    index++;
                                }
                            }

                            // Hide unused cards
                            for (int i = index; i < activityCards.length; i++) {
                                activityCards[i].setVisibility(View.GONE);
                            }
                        } else {
                            noActivityMessage.setVisibility(View.VISIBLE);
                            for (View card : activityCards) {
                                card.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
    }

    private void updateActivityCard(int index, DashboardRecentActivity activity) {
        activityCards[index].setVisibility(View.VISIBLE);
        cardTitles[index].setText(activity.getActivityType());
        cardSubtitles[index].setText(activity.getTitle());
        timeTexts[index].setText(getTimeAgo(activity.getTimestamp()));

        int iconResource = getActivityIcon(activity.getActivityType());
        activityIcons[index].setImageResource(iconResource);

        // Set click listener for activity card
        activityCards[index].setOnClickListener(v -> {
            Log.d("DashboardActivity", "Activity Card Clicked: " + activity.getActivityType());
            navigateToActivity(activity);
        });
    }




    private int getActivityIcon(String activityType) {
        switch (activityType.toLowerCase()) {
            case "discussion":
            case "group_project":
                return R.drawable.collab_icon;
            case "article":
            case "module":
                return R.drawable.learning_icon;
            default:
                return R.drawable.learning_icon;
        }
    }

    private void loadModuleProgress() {  //FIREBASE not detected
        progressRef.orderByChild("timestamp").limitToLast(2)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int index = 0;
                        for (DataSnapshot progressSnapshot : snapshot.getChildren()) {
                            DashboardModuleProgress progress = progressSnapshot.getValue(DashboardModuleProgress.class);
                            if (progress != null && index < progressCards.length) {
                                updateProgressCard(index, progress);
                                index++;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
    }

    private void updateProgressCard(int index, DashboardModuleProgress progress) {
        progressCards[index].setVisibility(View.VISIBLE);
        progressTitles[index].setText(progress.getModuleName());
        progressBars[index].setProgress(progress.getProgressPercentage());
        progressPercentages[index].setText(progress.getProgressPercentage() + "%");

        // Set click listener
        progressCards[index].setOnClickListener(v -> navigateToModule(progress));
    }

    private void navigateToActivity(DashboardRecentActivity activity) {
        Log.d("DashboardActivity", "Navigating to activity: " + activity.getActivityType());
        Log.d("DashboardActivity", "Title: " + activity.getTitle() + ", ReferenceId: " + activity.getReferenceId());

        Intent intent;
        String activityType = activity.getActivityType().toLowerCase();

        if (activityType.equals("article")) {
            intent = new Intent(this, ResourceContentActivity.class);

            // Pass required data to ResourceContentActivity
            intent.putExtra("subcategoryId", Integer.parseInt(activity.getReferenceId())); // Use Reference ID
            intent.putExtra("subcategory", activity.getTitle()); // Article title
            intent.putExtra("Category", "Articles"); // Assuming articles are categorized under "Articles"

            Log.d("DashboardActivity", "Navigating to Article with SubcategoryId: " + activity.getReferenceId());
            startActivity(intent);
            return;
        } else if (activityType.startsWith("module")) {
            intent = new Intent(this, ModulesContentActivity.class);

            // Extract module category and pass to ModulesContentActivity
            String moduleCategory = activity.getActivityType().substring(8); // Skip "module - "
            intent.putExtra("subcategoryId", Integer.parseInt(activity.getReferenceId()));
            intent.putExtra("Category", moduleCategory.trim());
            intent.putExtra("subcategory", activity.getTitle());

            Log.d("DashboardActivity", "Module Category: " + moduleCategory);
            startActivity(intent);
            return;
        } else if (activityType.startsWith("group_project")) {
            intent = new Intent(this, CollabProjectsDescActivity.class);

            intent.putExtra("project_id", activity.getReferenceId());
            intent.putExtra("project_title", activity.getTitle());
            startActivity(intent);
            return;

        } else if (activityType.equals("discussion")) {
            intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
            return;
        } else {
            Toast.makeText(this, "Unhandled activity type: " + activity.getActivityType(), Toast.LENGTH_SHORT).show();
        }
    }


    private void navigateToModule(DashboardModuleProgress progress) {
        Intent intent = new Intent(this, LearningActivity.class);
        intent.putExtra("moduleId", progress.getModuleId());
        startActivity(intent);
    }

    private String getTimeAgo(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        if (diff < 60 * 60 * 1000) {
            return TimeUnit.MILLISECONDS.toMinutes(diff) + " Minutes Ago";
        } else if (diff < 24 * 60 * 60 * 1000) {
            return TimeUnit.MILLISECONDS.toHours(diff) + " Hours Ago";
        } else {
            return TimeUnit.MILLISECONDS.toDays(diff) + " Days Ago";
        }
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_dashboard;
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
}
