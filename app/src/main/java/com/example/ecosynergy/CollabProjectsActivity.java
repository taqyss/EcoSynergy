package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
// For Firebase Authentication (to get the current user)
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// For Firebase Realtime Database (to retrieve data)
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollabProjectsActivity extends BaseActivity {private RecyclerView projectsRecyclerView;
    private ProjectAdapter projectAdapter;
    private List<Project> projectList;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_project1);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize RecyclerView
        projectsRecyclerView = findViewById(R.id.recyclerViewProjects);
        projectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        projectList = new ArrayList<>();
        projectAdapter = new ProjectAdapter(projectList);
        projectsRecyclerView.setAdapter(projectAdapter);

        // Set up the toolbar and navigation (your existing code)
        setupToolbar(true);
        getSupportActionBar().setTitle("Work on Projects");
        setupBottomNavigation();

        // Your existing click listeners
        setupNavigationListeners();

        // Load projects from Firebase
        loadAllProjects();
    }

    private void setupNavigationListeners() {
        ImageView collabAddIcon = findViewById(R.id.collabAddIcon);
        TextView addNewTitle = findViewById(R.id.addNewTitle);
        ImageView roundedRectCollab4 = findViewById(R.id.roundedRectCollab4);

        View.OnClickListener navigateToAddProjects = view -> {
            Intent intent = new Intent(CollabProjectsActivity.this, CollabProjectsAddActivity.class);
            startActivity(intent);
        };

        collabAddIcon.setOnClickListener(navigateToAddProjects);
        addNewTitle.setOnClickListener(navigateToAddProjects);
        roundedRectCollab4.setOnClickListener(navigateToAddProjects);
    }

    private void loadAllProjects() {
        // Reference to all projects
        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                projectList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Get projects for each user
                    DataSnapshot projectsSnapshot = userSnapshot.child("Projects");
                    for (DataSnapshot projectSnapshot : projectsSnapshot.getChildren()) {
                        Project project = projectSnapshot.getValue(Project.class);
                        if (project != null) {
                            // Add user information to the project
                            project.setUserId(userSnapshot.getKey());
                            // Get user's name if available
                            String userName = userSnapshot.child("name").getValue(String.class);
                            project.setUserName(userName != null ? userName : "Anonymous");
                            projectList.add(project);
                        }
                    }
                }
                // Sort projects by most recent first (if you have a timestamp)
                Collections.reverse(projectList);
                projectAdapter.notifyDataSetChanged();
            }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(CollabProjectsActivity.this,
                                "Error loading projects: " + databaseError.getMessage(),
                                Toast.LENGTH_SHORT).show();
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
