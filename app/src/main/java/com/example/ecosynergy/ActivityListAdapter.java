package com.example.ecosynergy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecosynergy.models.ActivityItem;

import java.util.List;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ViewHolder> {
    private List<ActivityItem> activityList;

    public ActivityListAdapter(List<ActivityItem> activityList) {
        this.activityList = activityList;
    }

    public void updateActivities(List<ActivityItem> activities) {
        this.activityList = activities;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, title , timestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_username);
            title = itemView.findViewById(R.id.tv_title);
            timestamp = itemView.findViewById(R.id.tv_timestamp);
        }
    }


    @NonNull
    @Override
    public ActivityListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.activitylist, parent, false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityListAdapter.ViewHolder holder, int position) {
        ActivityItem activityItem = activityList.get(position);
        holder.username.setText(activityItem.getUsername());
        holder.title.setText(activityItem.getActivity());
        holder.timestamp.setText(activityItem.getTimestamp());

    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }
}
