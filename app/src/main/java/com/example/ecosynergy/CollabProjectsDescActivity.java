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

public class CollabProjectsDescActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_project3);

        // Set up the toolbar with back button enabled
        setupToolbar(true);

        //Set a custom title, or leave it blank if there's no need for a title
        getSupportActionBar().setTitle("Work on Projects");

        // Set up bottom navigation
        setupBottomNavigation();

        getSupportActionBar().setTitle("Project Details");

        // Get project details from intent
        Intent intent = getIntent();
        String projectId = intent.getStringExtra("project_id");
        String projectTitle = intent.getStringExtra("project_title");
        String projectDescription = intent.getStringExtra("project_description");
        String projectStatus = intent.getStringExtra("project_status");
        int collaborators = intent.getIntExtra("project_collaborators", 0);

        // Update UI elements with project details
        TextView titleTextView = findViewById(R.id.ProjectTitleProject);
        TextView descriptionTextView = findViewById(R.id.groupProjectDesc);
        TextView statusTextView = findViewById(R.id.inProgressStatus);
        TextView membersTextView = findViewById(R.id.TotalGroupMember);
        ImageView statusCircle = findViewById(R.id.circleInProgress3);

        titleTextView.setText(projectTitle);
        descriptionTextView.setText(projectDescription);
        statusTextView.setText(projectStatus);

        // Update the circle based on status
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
                    // Keep default image or set a default status
                    statusCircle.setImageResource(R.drawable.collab_circle_notstarted);
                    break;
            }
        }

        // Format members text (adjust as needed based on your data structure)
        String membersText = String.format("(1/%d)", collaborators + 1);
        membersTextView.setText(membersText);

        String projectLink = getIntent().getStringExtra("project_link");

        ImageView joinButton = findViewById(R.id.joinButton);
        TextView joinText = findViewById(R.id.joinText);

        View.OnClickListener joinClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        };
        joinButton.setOnClickListener(joinClickListener);
        joinText.setOnClickListener(joinClickListener);
    }


    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_collaboration; // This is the ID of the "Collaboration" menu item
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