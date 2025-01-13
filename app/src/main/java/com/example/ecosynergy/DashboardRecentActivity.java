package com.example.ecosynergy;

public class DashboardRecentActivity {

    private String activityId;
    private String activityType;
    private String title;
    private long timestamp;
    private String referenceId; // Store subcategoryId for modules

    // Required empty constructor for Firebase
    public DashboardRecentActivity() {}

    public DashboardRecentActivity(String activityId, String activityType, String title, long timestamp, String referenceId) {
        this.activityId = activityId;
        this.activityType = activityType;
        this.title = title;
        this.timestamp = timestamp;
        this.referenceId = referenceId;
    }

    // Getters and setters
    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

}
