package com.example.ecosynergy;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CollabProjectsDescActivity extends BaseActivity {
    public static final int RESULT_COLLABORATOR_UPDATED = 1001;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private String projectId;
    private int collaborators;
    private TextView membersTextView;
    private ImageView joinButton;
    private String projectLink;
    private int position;
    private boolean hasJoined;
    private int[] currentAmount = new int[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_project3);

        // Set up the toolbar
        setupToolbar(true);
        getSupportActionBar().setTitle("Project Details");
        setupBottomNavigation();

        // Get project details from intent
        Intent intent = getIntent();
        projectId = intent.getStringExtra("project_id");
        String projectTitle = intent.getStringExtra("project_title");
        String projectDescription = intent.getStringExtra("project_description");
        String projectStatus = intent.getStringExtra("project_status");
        collaborators = intent.getIntExtra("project_collaborators", 0);
        projectLink = intent.getStringExtra("project_link");
        position = intent.getIntExtra("position", -1);
        hasJoined = intent.getBooleanExtra("has_joined", false);

        if (projectId == null || projectTitle == null) {
            Toast.makeText(this, "Project details are missing. Cannot load the project.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI elements
        TextView titleTextView = findViewById(R.id.ProjectTitleProject);
        TextView descriptionTextView = findViewById(R.id.groupProjectDesc);
        TextView statusTextView = findViewById(R.id.inProgressStatus);
        membersTextView = findViewById(R.id.TotalGroupMember);
        ImageView statusCircle = findViewById(R.id.circleInProgress3);
        joinButton = findViewById(R.id.joinButton);

        titleTextView.setText(projectTitle);
        descriptionTextView.setText(projectDescription);
        statusTextView.setText(projectStatus);

        // Update the status circle based on the project status
        if (projectStatus != null) {
            switch (projectStatus.toLowerCase()) {
                case "not started":
                    statusCircle.setImageResource(R.drawable.collab_circle_notstarted);
                    break;
                case "in progress":
                    statusCircle.setImageResource(R.drawable.collab_circle_inprogress);
                    break;
                case "completed":
                    statusCircle.setImageResource(R.drawable.collab_circle_completed);
                    break;
                default:
                    statusCircle.setImageResource(R.drawable.collab_circle_notstarted);
                    break;
            }
        }

        // Set initial values
        currentAmount[0] = collaborators;
        updateMembersText();

        // Check if user has already joined
        if (hasJoined) {
            disableJoinButton("You have already joined this project");
        }

        // Set up the Join button
        joinButton.setOnClickListener(v -> {
            if (hasJoined) {
                Toast.makeText(CollabProjectsDescActivity.this,
                        "You have already joined this project", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentAmount[0] > 0) {
                // Decrease the local collaborator amount
                currentAmount[0]--;
                updateMembersText();

                // Mark as joined
                hasJoined = true;
                disableJoinButton("You have joined this project!");

                // Create intent to pass back the updated data
                Intent resultIntent = new Intent();
                resultIntent.putExtra("position", position);
                resultIntent.putExtra("new_collaborator_amount", currentAmount[0]);
                setResult(RESULT_COLLABORATOR_UPDATED, resultIntent);

                // Open the project link
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(projectLink));
                    startActivity(browserIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(CollabProjectsDescActivity.this,
                            "No browser app found.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(CollabProjectsDescActivity.this,
                            "Invalid link format.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CollabProjectsDescActivity.this,
                        "This project is full.", Toast.LENGTH_SHORT).show();
            }
        });

        // Log the recent activity for the current project
        logRecentActivity(projectId, projectTitle);
    }

    private void updateMembersText() {
        String membersText = currentAmount[0] + " members needed";
        membersTextView.setText(membersText);

        // Disable join button if project is full
        if (currentAmount[0] == 0) {
            disableJoinButton("This project is full");
        }
    }

    private void disableJoinButton(String message) {
        joinButton.setEnabled(false);
        joinButton.setAlpha(0.5f);
        Toast.makeText(CollabProjectsDescActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_collaboration;
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

    private void logRecentActivity(String projectId, String projectTitle) {
        DatabaseReference recentActivitiesRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("recent_activities");

        String activityType = "group_project";
        long timestamp = System.currentTimeMillis();

        Query query = recentActivitiesRef.orderByChild("referenceId").equalTo(projectId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                        activitySnapshot.getRef().child("timestamp").setValue(timestamp);
                    }
                } else {
                    String activityId = recentActivitiesRef.push().getKey();
                    DashboardRecentActivity recentActivity = new DashboardRecentActivity(
                            activityId, activityType, projectTitle, timestamp, projectId
                    );
                    if (activityId != null) {
                        recentActivitiesRef.child(activityId).setValue(recentActivity);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CollabProjectsDescActivity.this, "Failed to log recent activity", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
