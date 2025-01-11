package com.example.ecosynergy.models;

public class ActivityItem {
    String username;
    String activity;
    String timestamp;

    public ActivityItem() {
    }

    public ActivityItem(String username, String activity, String timestamp) {
        this.username = username;
        this.activity = activity;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getActivity() {
        return activity;
    }

    public String getTimestamp() {
        return timestamp;
    }

}
