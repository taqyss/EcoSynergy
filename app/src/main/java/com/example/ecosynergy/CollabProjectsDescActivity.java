package com.example.ecosynergy;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class CollabProjectsDescActivity extends BaseActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private String projectId;
    private int collaborators;
    private TextView membersTextView;
    private ImageView joinButton;
    private String projectLink;

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

        // Update members text
        updateMembersText();

        // Set up Join button click listener
        joinButton.setOnClickListener(v -> joinProject());
    }

    private void updateMembersText() {
        String membersText = String.format("(%d/%d)", collaborators, collaborators + 1);
        membersTextView.setText(membersText);
    }

    private void joinProject() {
        // Open project link in browser
        if (projectLink != null && !projectLink.isEmpty()) {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(projectLink));
                startActivity(browserIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No browser app found", Toast.LENGTH_SHORT).show();
                return;
            } catch (Exception e) {
                Toast.makeText(this, "Invalid link format", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, "No project link available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Increment collaborators count in Firebase
        incrementCollaboratorsInFirebase();
    }

    private void incrementCollaboratorsInFirebase() {
        DatabaseReference projectRef = database.child("Projects").child(projectId);

        projectRef.child("collaboratorAmount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot collaboratorSnapshot) {
                Integer currentCollaborators = collaboratorSnapshot.getValue(Integer.class);
                if (currentCollaborators != null) {
                    projectRef.child("collaboratorAmount").setValue(currentCollaborators + 1);
                    collaborators++;
                    updateMembersText();
                    Toast.makeText(CollabProjectsDescActivity.this,
                            "You have joined the project!", Toast.LENGTH_SHORT).show();

                    // Disable Join button if project is full
                    if (collaborators == currentCollaborators + 1) {
                        disableJoinButton();
                    }
                } else {
                    Toast.makeText(CollabProjectsDescActivity.this,
                            "Failed to retrieve collaborator data.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CollabProjectsDescActivity.this,
                        "Failed to join project: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void disableJoinButton() {
        joinButton.setEnabled(false);
        joinButton.setAlpha(0.5f); // Dim the button to indicate it's disabled
        Toast.makeText(this, "This project is now full.", Toast.LENGTH_SHORT).show();
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
}
