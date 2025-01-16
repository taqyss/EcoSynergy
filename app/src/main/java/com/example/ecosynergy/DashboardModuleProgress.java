package com.example.ecosynergy;

public class DashboardModuleProgress {

    private String moduleId;
    private String moduleName;
    private int totalItems;
    private int completedItems;
    private long lastUpdated;

    public DashboardModuleProgress() {
        // Default constructor required for calls to DataSnapshot.getValue(DashboardModuleProgress.class)
    }

    public DashboardModuleProgress(String moduleId, String moduleName, int totalItems, int completedItems) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.totalItems = totalItems;
        this.completedItems = completedItems;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getCompletedItems() {
        return completedItems;
    }

    public void setCompletedItems(int completedItems) {
        this.completedItems = completedItems;
    }

    public int getProgressPercentage() {
        return totalItems > 0 ? (completedItems * 100) / totalItems : 0;
    }

    public void setProgressPercentage(int percentage) {
        if (totalItems > 0) {
            this.completedItems = (percentage * totalItems) / 100;
        }
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
