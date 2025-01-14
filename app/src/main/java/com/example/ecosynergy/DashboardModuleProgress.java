package com.example.ecosynergy;

public class DashboardModuleProgress {

    private String moduleId;
    private String moduleName;
    private int totalItems;
    private int completedItems;
    private long lastUpdated;
    // Required empty constructor for Firebase
    public DashboardModuleProgress() {

    }

    public DashboardModuleProgress(String moduleId, String moduleName, int totalItems, int completedItems) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.totalItems = totalItems;
        this.completedItems = completedItems;
        this.lastUpdated = System.currentTimeMillis();
    }

    public int getProgressPercentage() {
        return totalItems > 0 ? (completedItems * 100) / totalItems : 0;
    }

    // Getters and setters
    public String getModuleId() { return moduleId; }
    public void setModuleId(String moduleId) { this.moduleId = moduleId; }
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
    public int getCompletedItems() { return completedItems; }
    public void setCompletedItems(int completedItems) { this.completedItems = completedItems; }
    public long getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }

}
