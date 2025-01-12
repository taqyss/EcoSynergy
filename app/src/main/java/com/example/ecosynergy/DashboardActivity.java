package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*; //FIREBASE NOT DETECTED
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
        activitiesRef = database.getReference("users").child(userId).child("recent_activities");
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

        // Initially hide all cards
        for (View card : activityCards) {
            card.setVisibility(View.GONE);
        }
        for (View card : progressCards) {
            card.setVisibility(View.GONE);
        }
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


    private void loadRecentActivities() {  //FIREBASE not detected
        activitiesRef.orderByChild("timestamp").limitToLast(4)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int index = 0;
                        for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                            DashboardRecentActivity activity = activitySnapshot.getValue(DashboardRecentActivity.class);
                            if (activity != null && index < activityCards.length) {
                                updateActivityCard(index, activity);
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

    private void updateActivityCard(int index, DashboardRecentActivity activity) {
        activityCards[index].setVisibility(View.VISIBLE);
        cardTitles[index].setText(activity.getActivityType());
        cardSubtitles[index].setText(activity.getTitle());
        timeTexts[index].setText(getTimeAgo(activity.getTimestamp()));

        // Set appropriate icon based on activity type
        int iconResource = getActivityIcon(activity.getActivityType());
        activityIcons[index].setImageResource(iconResource);

        // Set click listener
        activityCards[index].setOnClickListener(v -> navigateToActivity(activity));
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
        Intent intent;
        switch (activity.getActivityType().toLowerCase()) {
            case "discussion":
                intent = new Intent(this, CollabDiscussActivity.class);
                break;
            case "group_project":
                intent = new Intent(this, CollabProjectsActivity.class);
                break;
            case "article":
            case "module":
                intent = new Intent(this, LearningActivity.class);
                break;
            default:
                return;
        }
        intent.putExtra("contentId", activity.getReferenceId());
        startActivity(intent);
    }

    private void navigateToModule(DashboardModuleProgress progress) {
        Intent intent = new Intent(this, LearningActivity.class);
        intent.putExtra("moduleId", progress.getModuleId());
        startActivity(intent);
    }

    private String getTimeAgo(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        if (hours < 1) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            return minutes + " Minutes Ago";
        } else if (hours < 24) {
            return hours + " Hours Ago";
        } else {
            long days = TimeUnit.MILLISECONDS.toDays(diff);
            return days + " Days Ago";
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
