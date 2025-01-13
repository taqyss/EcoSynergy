package com.example.ecosynergy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private List<Project> projectList;
    private Context context;

    public ProjectAdapter(Context context, List<Project> projectList) {
        this.context = context;
        this.projectList = projectList;
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
        int initialCollaborators = 1; // You are included initially
        int totalCollaborators = initialCollaborators + project.getCollaboratorAmount();

        // Set total collaborators dynamically (e.g., "1/6 members")
        String totalMembersText = initialCollaborators + "/" + totalCollaborators + " members";
        holder.totalProjectMembers.setText(totalMembersText);

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
            // Pass all necessary project data
            intent.putExtra("project_id", project.getProjectTitle());
            intent.putExtra("project_title", project.getProjectTitle());
            intent.putExtra("project_description", project.getDescription());
            intent.putExtra("project_status", project.getStatus());
            intent.putExtra("project_collaborators", project.getCollaboratorAmount());
            intent.putExtra("project_link", project.getLink()); // Add this line
            context.startActivity(intent);
        });
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