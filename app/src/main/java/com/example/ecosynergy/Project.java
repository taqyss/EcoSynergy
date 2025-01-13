package com.example.ecosynergy;

import java.util.HashMap;
import java.util.Map;

public class Project {
    private String projectTitle;
    private String category;
    private int collaboratorAmount;
    private String description;
    private String link;
    private String status;
    private long timestamp;
    private String title;

    private String userId;    // Add this field
    private String userName;  // Add this field
    private int currentMembers;
    private Map<String, Boolean> joinedUsers;
    private String fileUrl;
    private String imageUrl;

    private String fileName;
    private String imageName;

    // Constructor (required for Firebase)
    public Project() {}

    // Parameterized Constructor
    public Project(String projectTitle, String category, int collaboratorAmount, String description, String link, String status) {
        this.projectTitle = projectTitle;
        this.category = category;
        this.collaboratorAmount = collaboratorAmount + 1;
        this.description = description;
        this.link = link;
        this.status = status;
        this.timestamp = System.currentTimeMillis();
        this.currentMembers = 1; // Start with 1 (the creator)
        this.joinedUsers = new HashMap<>();
    }

    // Getters and Setters (optional but useful)
    public String getProjectTitle() { return projectTitle; }
    public void setProjectTitle(String projectTitle) { this.projectTitle = projectTitle; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getCollaboratorAmount() { return collaboratorAmount; }
    public void setCollaboratorAmount(int collaboratorAmount) { this.collaboratorAmount = collaboratorAmount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getTitle() { // Add this getter for title
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCurrentMembers() {
        return currentMembers;
    }

    public void setCurrentMembers(int currentMembers) {
        this.currentMembers = currentMembers;
    }

    public Map<String, Boolean> getJoinedUsers() {
        return joinedUsers;
    }

    public void setJoinedUsers(Map<String, Boolean> joinedUsers) {
        this.joinedUsers = joinedUsers;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }
}
