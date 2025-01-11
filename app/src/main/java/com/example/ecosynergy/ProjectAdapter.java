package com.example.ecosynergy;

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

    public ProjectAdapter(List<Project> projectList) {
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
        holder.totalProjectMembers.setText(String.format("(%d members needed)",
                project.getCollaboratorAmount()));

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
