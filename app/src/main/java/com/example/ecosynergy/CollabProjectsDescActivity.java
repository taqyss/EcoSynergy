package com.example.ecosynergy;

import android.content.Intent;
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

        // Format members text (adjust as needed based on your data structure)
        String membersText = String.format("(1/%d)", collaborators + 1);
        membersTextView.setText(membersText);

        // Set up join button click listener
        ImageView joinButton = findViewById(R.id.joinButton);
        TextView joinText = findViewById(R.id.joinText);

        View.OnClickListener joinClickListener = v -> {
            // Implement join functionality here
            Toast.makeText(this, "Joining project...", Toast.LENGTH_SHORT).show();
            // Add your Firebase logic to join the project
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
