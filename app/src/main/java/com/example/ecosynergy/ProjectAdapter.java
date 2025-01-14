package com.example.ecosynergy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    private static final int REQUEST_CODE_PROJECT_UPDATE = 1002;
    private List<Project> projectList;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences collaboratorPreferences;

    public ProjectAdapter(Context context, List<Project> projectList) {
        this.context = context;
        this.projectList = projectList;
        this.sharedPreferences = context.getSharedPreferences("joined_projects", Context.MODE_PRIVATE);
        this.collaboratorPreferences = context.getSharedPreferences("collaborator_amounts", Context.MODE_PRIVATE);
        loadSavedCollaboratorAmounts();
    }

    private void loadSavedCollaboratorAmounts() {
        for (Project project : projectList) {
            String projectId = project.getProjectTitle();
            int savedAmount = collaboratorPreferences.getInt(projectId, -1);
            if (savedAmount != -1) {
                project.setCollaboratorAmount(savedAmount);
            }
        }
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_item, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projectList.get(position);

        holder.projectTitle.setText(project.getProjectTitle());
        holder.projectDescription.setText(project.getDescription());

        String totalMembersText = project.getCollaboratorAmount() + " members needed";
        holder.totalProjectMembers.setText(totalMembersText);

        // Check if user has already joined this project
        boolean hasJoined = sharedPreferences.getBoolean(project.getProjectTitle(), false);

        // Update status indicators
        holder.circleNotStarted.setVisibility(View.GONE);
        holder.circleInProgress.setVisibility(View.GONE);
        holder.circleCompleted.setVisibility(View.GONE);

        switch (project.getStatus().toLowerCase()) {
            case "not started":
                holder.circleNotStarted.setVisibility(View.VISIBLE);
                break;
            case "in progress":
                holder.circleInProgress.setVisibility(View.VISIBLE);
                break;
            case "completed":
                holder.circleCompleted.setVisibility(View.VISIBLE);
                break;
        }

        // Add click listener to the entire item view
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CollabProjectsDescActivity.class);
            intent.putExtra("project_id", project.getProjectTitle());
            intent.putExtra("project_title", project.getProjectTitle());
            intent.putExtra("project_description", project.getDescription());
            intent.putExtra("project_status", project.getStatus());
            intent.putExtra("project_collaborators", project.getCollaboratorAmount());
            intent.putExtra("project_link", project.getLink());
            intent.putExtra("position", position);
            intent.putExtra("has_joined", hasJoined);
            ((Activity) context).startActivityForResult(intent, REQUEST_CODE_PROJECT_UPDATE);
        });
    }

    public void updateCollaboratorAmount(int position, int newCollaboratorAmount) {
        if (position >= 0 && position < projectList.size()) {
            Project project = projectList.get(position);
            String projectId = project.getProjectTitle();

            // Update project object
            project.setCollaboratorAmount(newCollaboratorAmount);

            // Save both joined status and new amount to SharedPreferences
            SharedPreferences.Editor joinedEditor = sharedPreferences.edit();
            SharedPreferences.Editor amountEditor = collaboratorPreferences.edit();

            joinedEditor.putBoolean(projectId, true);
            amountEditor.putInt(projectId, newCollaboratorAmount);

            joinedEditor.apply();
            amountEditor.apply();

            // Update RecyclerView
            notifyItemChanged(position);
        }
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView projectTitle, projectDescription, totalProjectMembers;
        ImageView circleNotStarted, circleInProgress, circleCompleted;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            projectTitle = itemView.findViewById(R.id.projectTitle);
            projectDescription = itemView.findViewById(R.id.projectDescription);
            totalProjectMembers = itemView.findViewById(R.id.totalProjectMembers);
            circleNotStarted = itemView.findViewById(R.id.circleNotStarted);
            circleInProgress = itemView.findViewById(R.id.circleInProgress);
            circleCompleted = itemView.findViewById(R.id.circleCompleted);
        }
    }
}
