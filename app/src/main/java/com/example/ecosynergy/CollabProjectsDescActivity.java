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

<<<<<<< HEAD
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CollabProjectsDescActivity extends BaseActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private TextView membersTextView;
    private ImageView joinButton;
=======
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CollabProjectsDescActivity extends BaseActivity {

    private String projectId;
    private int collaborators;
    private TextView membersTextView;
>>>>>>> 23749c3832b42f08b2978d9b8e93653f39a0e775

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_project3);

        // Set up the toolbar
        setupToolbar(true);
        getSupportActionBar().setTitle("Project Details");
<<<<<<< HEAD

        // Set up bottom navigation
=======
>>>>>>> 23749c3832b42f08b2978d9b8e93653f39a0e775
        setupBottomNavigation();

        // Get project details from intent
        Intent intent = getIntent();
        projectId = intent.getStringExtra("project_id");
        String projectTitle = intent.getStringExtra("project_title");
        String projectDescription = intent.getStringExtra("project_description");
        String projectStatus = intent.getStringExtra("project_status");
<<<<<<< HEAD
        int collaborators = intent.getIntExtra("project_collaborators", 0);
        String projectLink = intent.getStringExtra("project_link");

        // Initialize UI components
=======
        collaborators = intent.getIntExtra("project_collaborators", 0);

        // UI elements
>>>>>>> 23749c3832b42f08b2978d9b8e93653f39a0e775
        TextView titleTextView = findViewById(R.id.ProjectTitleProject);
        TextView descriptionTextView = findViewById(R.id.groupProjectDesc);
        TextView statusTextView = findViewById(R.id.inProgressStatus);
        membersTextView = findViewById(R.id.TotalGroupMember);
        ImageView statusCircle = findViewById(R.id.circleInProgress3);
        joinButton = findViewById(R.id.joinButton);

        titleTextView.setText(projectTitle);
        descriptionTextView.setText(projectDescription);
        statusTextView.setText(projectStatus);

<<<<<<< HEAD
        // Update the status circle based on the project status
=======
        // Update status circle
>>>>>>> 23749c3832b42f08b2978d9b8e93653f39a0e775
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
<<<<<<< HEAD
                default:
                    statusCircle.setImageResource(R.drawable.collab_circle_notstarted);
                    break;
            }
        }

        // Set members needed text
        String membersText = collaborators + " members needed";
        membersTextView.setText(membersText);
        final int initialCollaborators = intent.getIntExtra("project_collaborators", 0);
        final int[] currentAmount = {initialCollaborators}; // Use an array to modify value
=======
            }
        }

        // Update members text
        updateMembersText();
>>>>>>> 23749c3832b42f08b2978d9b8e93653f39a0e775

        String currentUserId = auth.getCurrentUser().getUid();
        DatabaseReference projectRef = database.child("Projects").child(projectId);
        DatabaseReference currentUserRef = database.child("Users").child(currentUserId);

<<<<<<< HEAD
        // Handle Join button click
        joinButton.setOnClickListener(v -> {
            if (currentAmount[0] > 0) {
                // Decrease the local collaborator amount
                currentAmount[0]--;

                // Update the UI
                String newMembersText = currentAmount[0] + " members needed";
                membersTextView.setText(newMembersText);

                // Disable the button if the project is full
                if (currentAmount[0] == 0) {
                    joinButton.setEnabled(false);
                    joinButton.setAlpha(0.5f); // Dim the button to indicate it's disabled
                    Toast.makeText(CollabProjectsDescActivity.this, "This project is now full.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CollabProjectsDescActivity.this, "You have successfully joined the project!", Toast.LENGTH_SHORT).show();
                }

                // Optionally, open the project link
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(projectLink));
                    startActivity(browserIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(CollabProjectsDescActivity.this, "No browser app found.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(CollabProjectsDescActivity.this, "Invalid link format.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Already full
                Toast.makeText(CollabProjectsDescActivity.this, "This project is full.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void joinProject(DatabaseReference projectRef, DatabaseReference userRef, int currentAmount, String projectId) {
        // Decrease collaborator amount
        projectRef.child("collaboratorAmount").setValue(currentAmount - 1);

        // Add user to the project's usersJoined list
        projectRef.child("usersJoined").child(auth.getCurrentUser().getUid()).setValue(true);

        // Save the project details to the user's joinedProjects node
        userRef.child("joinedProjects").child(projectId).setValue(true);

        // Update the members needed text
        String newMembersText = (currentAmount - 1) + " members needed";
        membersTextView.setText(newMembersText);

        // Show success message
        Toast.makeText(this, "You have successfully joined the project!", Toast.LENGTH_SHORT).show();

        // Disable the join button if no more members are needed
        if (currentAmount - 1 == 0) {
            disableJoinButton();
        }
    }

    private void disableJoinButton() {
        joinButton.setEnabled(false);
        joinButton.setAlpha(0.5f);
=======
        // Join button logic
        ImageView joinButton = findViewById(R.id.joinButton);
        TextView joinText = findViewById(R.id.joinText);

        View.OnClickListener joinClickListener = v -> {
            // Open project link in browser
            if (projectLink != null && !projectLink.isEmpty()) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(projectLink));
                    startActivity(browserIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(CollabProjectsDescActivity.this,
                            "No browser app found", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(CollabProjectsDescActivity.this,
                            "Invalid link format", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CollabProjectsDescActivity.this,
                        "No project link available", Toast.LENGTH_SHORT).show();
            }

            // Increment collaborators in Firebase
            incrementCollaboratorsInFirebase();
        };

        joinButton.setOnClickListener(joinClickListener);
        joinText.setOnClickListener(joinClickListener);
    }

    private void updateMembersText() {
        String membersText = String.format("(%d/%d)", collaborators, collaborators + 1);
        membersTextView.setText(membersText);
    }

    private void incrementCollaboratorsInFirebase() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean projectFound = false;

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot projectsSnapshot = userSnapshot.child("Projects").child(projectId);

                    if (projectsSnapshot.exists()) {
                        projectFound = true;
                        DatabaseReference projectRef = projectsSnapshot.getRef();

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
                        break; // Exit loop once project is found
                    }
                }

                if (!projectFound) {
                    Toast.makeText(CollabProjectsDescActivity.this,
                            "Project not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CollabProjectsDescActivity.this,
                        "Error accessing database: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
>>>>>>> 23749c3832b42f08b2978d9b8e93653f39a0e775
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
<<<<<<< HEAD

=======
>>>>>>> 23749c3832b42f08b2978d9b8e93653f39a0e775
