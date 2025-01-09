package com.example.ecosynergy;

public class DashboardModuleProgress {

    private String moduleId;
    private String moduleName;
    private int progressPercentage;

    // Required empty constructor for Firebase
    public DashboardModuleProgress() {

    }

    public DashboardModuleProgress(String moduleId, String moduleName, int progressPercentage) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.progressPercentage = progressPercentage;
    }

    // Getters and setters
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

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

}
