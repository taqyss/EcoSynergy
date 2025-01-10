package com.example.ecosynergy.models;

public class ActivityItem {
    String username;
    String title;
    String timestamp;

    public ActivityItem(String username, String title, String timestamp) {
        this.username = username;
        this.title = title;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getTimestamp() {
        return timestamp;
    }

}
