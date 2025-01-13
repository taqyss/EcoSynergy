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
    private ProjectAdapter projectAdapter;  // Declare the adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_project3);

        // Set up the toolbar
        setupToolbar(true);
        getSupportActionBar().setTitle("Project Details");
        setupBottomNavigation();

        // Get the adapter passed through the intent
        projectAdapter = (ProjectAdapter) getIntent().getSerializableExtra("projectAdapter");

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

        final int initialCollaborators = intent.getIntExtra("project_collaborators", 0);
        final int[] currentAmount = {initialCollaborators}; // Use an array to modify value

// Set initial text
        TextView membersTextView = findViewById(R.id.TotalGroupMember);
        String membersText = currentAmount[0] + " members needed";
        membersTextView.setText(membersText);

// Set up the Join button
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
